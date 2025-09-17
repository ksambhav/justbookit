package com.jbkit.catalogue.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record ScheduleShowRequest(
        @NotBlank String movieId,
        @NotBlank String theatreId,
        @NotNull LocalDate firstShowDate,
        @NotNull LocalDate lastShowDate,
        @NotEmpty List<ShowTimings> showTimings
) {
}
