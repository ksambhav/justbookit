package com.jbkit.booking.show.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = {"ussi"})
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SeatInventory {

    @Id
    String ussi;
    UUID movieId;
    UUID theatreId;
    LocalDate showDate;
    LocalTime startTime;
    String seatLabel;
    String movieTitle;
    String theatreName;
    String venueName;
    @Enumerated(EnumType.STRING)
    SeatStatus status;

    public enum SeatStatus {
        AVAILABLE, BOOKED, LOCKED
    }
}
