package com.jbkit.catalogue.dto;

import jakarta.validation.constraints.NotBlank;

public record AddVenueRequest(
        @NotBlank String name,
        @NotBlank String city,
        @NotBlank String address) {

}
