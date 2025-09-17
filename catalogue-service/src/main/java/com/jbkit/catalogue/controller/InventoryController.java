package com.jbkit.catalogue.controller;

import com.jbkit.catalogue.api.InventoryManager;
import com.jbkit.catalogue.dto.AddTheatreRequest;
import com.jbkit.catalogue.dto.AddVenueRequest;
import com.jbkit.catalogue.dto.ScheduleShowRequest;
import com.jbkit.catalogue.dto.SeatCoordinates;
import com.jbkit.catalogue.model.Theatre;
import com.jbkit.catalogue.model.Venue;
import com.jbkit.catalogue.repo.TheatreRepo;
import com.jbkit.catalogue.repo.VenueRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * These are very low Traffic API, used only to onboard Venue and threatres in them
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
class InventoryController {

    private final InventoryManager inventoryManager;
    private final TheatreRepo theatreRepo;
    private final VenueRepo venueRepo;

    @PostMapping("/venue")
    Map<String, String> addVenue(@RequestBody @Valid AddVenueRequest request) {
        String id = inventoryManager.addNewVenue(request);
        return Map.of("id", id);
    }

    @PostMapping("/venue/{venueId}/theatre")
    public void addTheatre(@PathVariable String venueId, @Valid @RequestBody AddTheatreRequest request) {
        inventoryManager.addTheatre(venueId, request);
    }

    @PostMapping("/shows")
    void createScheduledShow(@RequestBody @Valid ScheduleShowRequest request) {
        inventoryManager.scheduleShows(request);
    }

    @GetMapping("/mock")
    public void generateMock() {
        CompletableFuture.runAsync(() -> {
            final Set<SeatCoordinates> seatCoordinates = new HashSet<>(200);
            for (int i = 65; i <= 97; i++) {
                for (int j = 1; j <= 30; j++) {
                    seatCoordinates.add(new SeatCoordinates(String.valueOf((char) i), j));
                }
            }
            theatreRepo.deleteAll();
            List<Theatre> theatres = new ArrayList<>();
            List<Venue> venues = venueRepo.findAll();
            for (Venue venue : venues) {
                for (int i = 1; i <= 2; i++) {
                    Theatre theatre = new Theatre();
                    theatre.setVenue(venue);
                    theatre.setSeats(seatCoordinates);
                    theatre.setName("Theatre %d".formatted(i));
                    theatres.add(theatre);
                }
            }
            theatreRepo.saveAll(theatres);
//            screeningInstanceRepo.deleteAll();
//            movieScreeningRepo.deleteAll();
//            LocalDate date = LocalDate.now().minusDays(1);
//            LocalDate endDate = date.plusDays(1);
//            for (Theatre theatre : theatres) {
//                LocalTime t = LocalTime.of(8, 0);
//                for (int i = 0; i < 4; i++) {
//                    AddNewShowRequest request = new AddNewShowRequest(MockData.top200Movies[RandomUtils.secure().randomInt(0, MockData.top200Movies.length)], "http://tinyurl", t, t.plusMinutes(RandomUtils.secure().randomInt(2, 10)), date, endDate);
//                    inventoryManager.scheduleShows(theatre.getId().toString(), request);
//                    t = t.plusMinutes(150);
//                }
//            }
            log.info("Saved {} theatres", theatres.size());
        });
    }

}
