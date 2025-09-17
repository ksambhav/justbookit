package com.jbkit.catalogue.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record ShowTimings(@NotNull LocalTime start, @NotNull LocalTime end) {
}
