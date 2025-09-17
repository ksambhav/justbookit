package com.jbkit.booking.show.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class BookingException extends Exception {
    public final HttpStatusCode statusCode;

    public BookingException(String message, HttpStatus httpStatusCode) {
        super(message);
        this.statusCode = httpStatusCode;
    }
}
