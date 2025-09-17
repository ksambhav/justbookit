package com.jbkit.catalogue.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record AddNewShowRequest(
        String movieTitle,
        String moviePosterUrl,
        LocalTime startTime,
        LocalTime endTime,
        LocalDate startDate,
        LocalDate endDate
) {
}
