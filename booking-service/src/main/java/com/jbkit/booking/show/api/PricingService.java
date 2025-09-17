package com.jbkit.booking.show.api;

import com.jbkit.booking.show.dto.BookingAmountComputations;
import com.jbkit.booking.show.dto.BookingRequest;

public interface PricingService {
    BookingAmountComputations compute(BookingRequest request);
}
