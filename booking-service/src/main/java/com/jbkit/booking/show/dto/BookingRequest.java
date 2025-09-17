package com.jbkit.booking.show.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record BookingRequest(
        @NotNull @Valid ShowDetails showDetails,
        Set<String> requestedSeats
) {
}
