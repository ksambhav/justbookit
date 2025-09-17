package com.jbkit.booking.show.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingAmountComputations {

    private BigDecimal baseAmount;
    private BigDecimal discountAmount;
    private BigDecimal taxes;
    private BigDecimal totalAmount;

}
