package com.jbkit.booking.show.api;

import com.jbkit.booking.show.model.BookingDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookingDetailsRepo extends JpaRepository<BookingDetails, UUID> {
}
