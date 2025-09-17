package com.jbkit.catalogue.repo;

import com.jbkit.catalogue.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MovieRepo extends JpaRepository<Movie, UUID> {
}
