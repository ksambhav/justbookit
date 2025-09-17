package com.jbkit.catalogue.api;

import com.jbkit.catalogue.dto.AddTheatreRequest;
import com.jbkit.catalogue.dto.AddVenueRequest;
import com.jbkit.catalogue.dto.ScheduleShowRequest;
import jakarta.validation.Valid;

public interface InventoryManager {
    String addNewVenue(@Valid AddVenueRequest request);

    void addTheatre(String venueId, @Valid AddTheatreRequest request);

    void scheduleShows(ScheduleShowRequest request);
}
