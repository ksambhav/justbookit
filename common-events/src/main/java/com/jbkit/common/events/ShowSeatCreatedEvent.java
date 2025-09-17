package com.jbkit.common.events;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

// Event-Carried State Transfer
//@Externalized(target = "sss_created_event")
public record ShowSeatCreatedEvent(
        String ussi,
        UUID movieId,
        UUID theatreId,
        LocalDate showDate,
        LocalTime startTime,
        String seatLabel,
        String movieTitle, String theatreName, String venueName) {
}
