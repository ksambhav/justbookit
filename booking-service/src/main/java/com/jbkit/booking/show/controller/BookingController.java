package com.jbkit.booking.show.controller;

import com.jbkit.booking.show.api.BookingException;
import com.jbkit.booking.show.api.BookingManager;
import com.jbkit.booking.show.dto.BookingRequest;
import com.jbkit.booking.show.dto.UserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shows")
class BookingController {

    private final BookingManager bookingManager;

    /**
     *
     * Exception handler for BookingException.
     * Logs the error and returns a ProblemDetail response with appropriate status code and message.
     * <p>
     * NOTE: Other places also we need to handle exceptions and return ProblemDetail
     */
    @ExceptionHandler(BookingException.class)
    public ProblemDetail handleBookingException(BookingException ex) {
        log.error("Booking exception occurred: {}", ex.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatus(ex.statusCode);
        problemDetail.setTitle("Booking Error");
        problemDetail.setDetail(ex.getMessage());
        return problemDetail;
    }

    @PostMapping("/book")
    public Map<String, String> bookShow(@RequestBody @Valid BookingRequest request) throws BookingException {
        log.debug("Received booking request: {}", request);
        //FIXME User details to be fetched from auth context
        UserDetails userDetails = new UserDetails("123");
        return Map.of("bookingId", bookingManager.initiateBooking(request, userDetails));
    }
}
