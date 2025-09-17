package com.jbkit.catalogue.dto;

import java.util.Set;

public record TheatreRequest(String name, Set<SeatCoordinates> seats) {
}
