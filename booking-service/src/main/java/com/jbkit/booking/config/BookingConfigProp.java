package com.jbkit.booking.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Data
@Component
@ConfigurationProperties(prefix = "booking")
public class BookingConfigProp {

    private int maxSeatsPerBooking = 10;
    private Duration maxHoldMinutes = Duration.ofMinutes(1);
}
