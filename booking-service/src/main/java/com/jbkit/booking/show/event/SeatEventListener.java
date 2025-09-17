package com.jbkit.booking.show.event;

import com.jbkit.booking.show.api.SeatInventoryRepo;
import com.jbkit.booking.show.model.SeatInventory;
import com.jbkit.common.events.ShowSeatCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class SeatEventListener {

    private final SeatInventoryRepo seatInventoryRepo;

    /**
     * This step can be optimized by using Debezium to capture DB changes directly from the Kafka topic.
     *
     * @param event
     */
    @KafkaListener(topics = "sss-created-v3", groupId = "booking-service", containerFactory = "seatCreateEventKafkaListenerContainerFactory")
    public void handleSeatEvent(ShowSeatCreatedEvent event) {
        log.info("Received seat event: {}", event);
        seatInventoryRepo.save(map(event));
        log.info("Seat inventory updated for event");
    }

    private SeatInventory map(ShowSeatCreatedEvent event) {
        return new SeatInventory(
                event.ussi(),
                event.movieId(),
                event.theatreId(),
                event.showDate(),
                event.startTime(),
                event.seatLabel(),
                event.movieTitle(),
                event.theatreName(),
                event.venueName(), SeatInventory.SeatStatus.AVAILABLE
        );
    }
}
