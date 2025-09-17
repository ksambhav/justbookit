package com.jbkit.booking.show.api;

import com.jbkit.booking.show.dto.BookingRequest;
import com.jbkit.booking.show.dto.UserDetails;

public interface BookingManager {

    String initiateBooking(BookingRequest request, UserDetails userDetails) throws BookingException;
}
