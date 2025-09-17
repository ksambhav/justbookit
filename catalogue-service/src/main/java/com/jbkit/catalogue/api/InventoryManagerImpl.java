package com.jbkit.catalogue.api;

import com.jbkit.catalogue.dto.*;
import com.jbkit.catalogue.model.*;
import com.jbkit.catalogue.repo.*;
import com.jbkit.common.events.ShowSeatCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class InventoryManagerImpl implements InventoryManager {

    private final VenueRepo venueRepo;
    private final TheatreRepo theatreRepo;
    private final MovieRepo movieRepo;
    private final ScheduledShowRepo scheduledShowRepo;
    private final ScheduledShowSeatRepo scheduledShowSeatRepo;
    private final VectorStore vectorStore;
    private final ApplicationEventPublisher eventPublisher;
    private final KafkaTemplate<String, ShowSeatCreatedEvent> seatCreatedEventKafkaTemplate;

    private static ScheduledShowSeat getScheduledShowSeat(ScheduledShow show, String label) {
        final ScheduledShowId scheduledShowId = show.getScheduledShowId();
        ScheduleShowSeatRef scheduleShowSeatRef = new ScheduleShowSeatRef(
                scheduledShowId.getMovieId(),
                scheduledShowId.getTheatreId(),
                scheduledShowId.getShowDate(),
                scheduledShowId.getStartTime(),
                label);
        return new ScheduledShowSeat(
                scheduleShowSeatRef.ussi(),
                scheduleShowSeatRef,
                show.getId()
        );
    }

    @Override
    public String addNewVenue(AddVenueRequest request) {
        var saved = venueRepo.save(new Venue(request.name(), request.city(), request.address()));
        return saved.getId().toString();
    }

    @Override
    public void addTheatre(String venueId, AddTheatreRequest request) {
        Venue venue = venueRepo.findById(UUID.fromString(venueId)).orElseThrow();
        List<Theatre> list = request.theatres().stream().map(r -> new Theatre(
                venue,
                r.name(),
                r.seats()
        )).toList();
        theatreRepo.saveAll(list);
    }

    @Override
    public void scheduleShows(ScheduleShowRequest request) {
        final var movie = movieRepo.findById(UUID.fromString(request.movieId())).orElseThrow();
        final var theatre = theatreRepo.findById(UUID.fromString(request.theatreId())).orElseThrow();
        List<ScheduledShow> shows = createShows(movie, theatre, request);
        shows = scheduledShowRepo.saveAll(shows);
        // Add to vector store for AI assistance
        vectorStore.add(shows.stream().map(s -> new org.springframework.ai.document.Document(
                "Movie Name: %s, City: %s, Venue: %s, Show Date: %s, Start Time: %s".formatted(
                        s.getMovieTitle(),
                        s.getCity(),
                        s.getVenue(),
                        s.getScheduledShowId().getShowDate(),
                        s.getScheduledShowId().getStartTime()
                )
        )).toList());
        Set<SeatCoordinates> seats = theatre.getSeats();
        List<ScheduledShowSeat> sss = new LinkedList<ScheduledShowSeat>();
        for (ScheduledShow show : shows) {
            sss.addAll(createShowSeats(seats, show));
        }
        // for better performance, we can use batch insert with JDBC template
        sss = scheduledShowSeatRepo.saveAll(sss);
        // Through Kafka Connect Debezium source connector, these changes will be captured and sent to the topic. NOT part of DEMO
        // pushing through Spring Events
        sss.stream().map(s -> new ShowSeatCreatedEvent(
                s.getId(),
                s.getShowSeatFk().getMovieId(),
                s.getShowSeatFk().getTheatreId(),
                s.getShowSeatFk().getShowDate(),
                s.getShowSeatFk().getStartTime(),
                s.getShowSeatFk().getSeatId(),
                movie.getTitle(),
                theatre.getName(),
                theatre.getVenue().getName()
        )).forEach(e -> seatCreatedEventKafkaTemplate.send("sss-created-v3", e.theatreId().toString(), e));
    }

    private List<ScheduledShowSeat> createShowSeats(Set<SeatCoordinates> seats, ScheduledShow show) {
        return seats.stream()
                .map(s -> getScheduledShowSeat(show, s.label()))
                .toList();
    }

    private List<ScheduledShow> createShows(Movie movie, Theatre theatre, ScheduleShowRequest request) {
        final List<ScheduledShow> scheduledShows = new ArrayList<>();
        validate(request);
        LocalDate date = request.firstShowDate();
        LocalDate lastShowDate = request.lastShowDate();
        List<ShowTimings> showTimings = request.showTimings();
        while (!date.isAfter(lastShowDate)) {
            for (ShowTimings showTiming : showTimings) {
                LocalTime start = showTiming.start();
                LocalTime end = showTiming.end();
                ScheduledShowId scheduledShowId = new ScheduledShowId(
                        movie.getId(),
                        theatre.getId(),
                        date,
                        start
                );
                ScheduledShow show = new ScheduledShow(
                        scheduledShowId.usi(),
                        scheduledShowId,
                        end,
                        theatre.getVenue().getCity(),
                        theatre.getVenue().getName(),
                        movie.getTitle(),
                        LocalDateTime.of(date, start), false, false
                );
                log.debug("Created show object = {}", show);
                scheduledShows.add(show);

            }
            date = date.plusDays(1);
        }
        return scheduledShows;
    }

    private void validate(ScheduleShowRequest request) {
        //TODO Validation
        // time are in order, there in gap in them, non overlapping
    }

}
