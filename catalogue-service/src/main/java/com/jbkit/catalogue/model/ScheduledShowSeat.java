package com.jbkit.catalogue.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ScheduledShowSeat {

    @Id
    String id;
    @Embedded
    ScheduleShowSeatRef showSeatFk;
    @Enumerated(EnumType.STRING)
    Status status = Status.AVAILABLE;
    private String showId;

    public ScheduledShowSeat(String id, ScheduleShowSeatRef showSeatFk, String showId) {
        this.id = id;
        this.showSeatFk = showSeatFk;
        this.showId = showId;
    }

    public enum Status {
        BOOKED,
        HOLD,
        AVAILABLE
    }
}
