package com.jbkit.booking.show.api.internal;

import com.jbkit.booking.show.api.BookingDetailsRepo;
import com.jbkit.booking.show.api.BookingException;
import com.jbkit.booking.show.api.BookingManager;
import com.jbkit.booking.show.api.PricingService;
import com.jbkit.booking.show.dto.BookingAmountComputations;
import com.jbkit.booking.show.dto.BookingRequest;
import com.jbkit.booking.show.dto.ShowDetails;
import com.jbkit.booking.show.dto.UserDetails;
import com.jbkit.booking.show.model.BookingDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static com.jbkit.booking.show.model.BookingDetails.BookingStatus.PENDING_PAYMENT;

@Slf4j
@Service
@RequiredArgsConstructor
class BookingManagerImpl implements BookingManager {

    private final BookingDetailsRepo bookingDetailsRepo;
    private final PricingService pricingService;
    private final SeatCordonService seatCordonService;

    static Set<String> bookingKeys(BookingRequest bookingRequest) {
        Set<String> keys = new HashSet<>(bookingRequest.requestedSeats().size());
        final ShowDetails showDetails = bookingRequest.showDetails();
        for (String seat : bookingRequest.requestedSeats()) {
            keys.add("%s-%s-%s-%s".formatted(showDetails.theatreId(), showDetails.showDate(), showDetails.showTime(), seat));
        }
        return keys;

    }

    @Override
    @Transactional
    public String initiateBooking(BookingRequest request, UserDetails userDetails) throws BookingException {
        Set<String> keys = bookingKeys(request);
        validate(request, keys);
        boolean locksAcquired = seatCordonService.acquireLocks(keys, "booking-service");
        if (!locksAcquired) {
            throw new BookingException("Some or all seats are already held/booked, cannot proceed with booking", HttpStatus.CONFLICT);
        }
        BookingAmountComputations bookingAmountComputations = computeAmount(request);
        BookingDetails bookingDetails = persistBooking(request, bookingAmountComputations);
        return bookingDetails.getUuid().toString();
    }

    BookingDetails persistBooking(BookingRequest request, BookingAmountComputations bookingAmountComputations) {
        BookingDetails details = new BookingDetails();
        details.setVersion(0);
        details.setBookingRequest(request);
        details.setBookingStatus(PENDING_PAYMENT);
        details.setBookingAmountComputations(bookingAmountComputations);
        return bookingDetailsRepo.saveAndFlush(details);
    }

    BookingAmountComputations computeAmount(BookingRequest request) {
        return pricingService.compute(request);
    }

    void validate(BookingRequest request, Set<String> keys) {
        //TODO validate dates, cannot be in past
        //TODO validate show timing, online booking should be done at least 30 mins before show time
        //TODO validate max seats allowed
        //TODO validate ScheduledShowSeat are valid and available from local bounded context data of catalogue service
        // TODO validated seats are available, there should be no entries for bookingKeys with status BOOKED or HOLD
    }

    @EventListener
    public void handleRedisKeyExpiredEvent(RedisKeyExpiredEvent event) {
        String expiredKey = new String(event.getId()); // Get the expired key
        String keyspace = event.getKeyspace(); // Get the keyspace
        Object expiredValue = event.getValue(); // Get the expired value (if available)
        log.info("Redis key expired: {} in keyspace: {}", expiredKey, keyspace);
        // Perform actions based on the expired key, e.g., clean up related data, log, etc.
    }
}
