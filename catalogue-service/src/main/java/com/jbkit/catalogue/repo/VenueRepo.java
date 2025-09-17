package com.jbkit.catalogue.repo;

import com.jbkit.catalogue.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VenueRepo extends JpaRepository<Venue, UUID> {
}
