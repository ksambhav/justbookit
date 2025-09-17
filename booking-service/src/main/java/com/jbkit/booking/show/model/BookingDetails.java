package com.jbkit.booking.show.model;

import com.jbkit.booking.show.dto.BookingAmountComputations;
import com.jbkit.booking.show.dto.BookingRequest;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "booking_details")
@Data
@EqualsAndHashCode(of = {"uuid"})
public class BookingDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID uuid;
    @Version
    private Integer version;
    @CreatedDate
    private Instant createdOn;
    @LastModifiedDate
    private Instant updatedOn;
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "booking_request", columnDefinition = "jsonb")
    private BookingRequest bookingRequest;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "amount_computations", columnDefinition = "jsonb")
    private BookingAmountComputations bookingAmountComputations;

    private String paymentReference;

    private String userEmail;
    private String userMobile;
    private String fullName;

    public enum BookingStatus {
        PENDING_PAYMENT,
        CONFIRMED,
        CANCELLED
    }
}
