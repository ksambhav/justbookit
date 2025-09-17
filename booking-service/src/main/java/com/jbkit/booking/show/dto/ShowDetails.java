package com.jbkit.booking.show.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record ShowDetails(
        @NotBlank String city,
        @NotBlank String venueId,
        @NotBlank String theatreId,
        @NotNull LocalDate showDate,
        @NotNull LocalTime showTime,
        @NotBlank String movieTitle,
        @NotBlank String venueName
) {
}
