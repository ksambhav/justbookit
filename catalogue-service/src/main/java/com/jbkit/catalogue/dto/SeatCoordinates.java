package com.jbkit.catalogue.dto;

public record SeatCoordinates(String row, int number) {

    public String label() {
        return "%s_%d".formatted(row, number);
    }

}
