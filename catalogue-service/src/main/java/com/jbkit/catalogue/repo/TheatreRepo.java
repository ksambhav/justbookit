package com.jbkit.catalogue.repo;

import com.jbkit.catalogue.model.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TheatreRepo extends JpaRepository<Theatre, UUID> {
}
