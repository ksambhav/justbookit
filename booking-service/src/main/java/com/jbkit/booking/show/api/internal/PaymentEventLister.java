package com.jbkit.booking.show.api.internal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class PaymentEventLister {

    public void handlePaymentEvent() {
        log.info("Payment event received");
        // if payment , success, update booking status to CONFIRMED
        // if payment failure, release seat locks and update booking status to FAILED_PAYMENT
        // release locks in Redis
    }

}
