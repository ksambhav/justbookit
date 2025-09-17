package com.jbkit.booking.show.api;

import com.jbkit.booking.show.model.SeatInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatInventoryRepo extends JpaRepository<SeatInventory, String> {
}
