package com.jbkit.booking.show.api.internal;

import com.jbkit.booking.show.api.PricingService;
import com.jbkit.booking.show.dto.BookingAmountComputations;
import com.jbkit.booking.show.dto.BookingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
class PricingServiceImpl implements PricingService {
    @Override
    public BookingAmountComputations compute(BookingRequest request) {
        //TODO Pricing system integration
        log.info("Computing pricing for request: {}", request);
        return new BookingAmountComputations(new BigDecimal(100), BigDecimal.ZERO, BigDecimal.ZERO, new BigDecimal(100));
    }
}
