package com.jbkit.catalogue.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.DigestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleShowSeatRef {
    UUID movieId;
    UUID theatreId;
    LocalDate showDate;
    LocalTime startTime;
    String seatId;

    /**
     * @return Unique Scheduled Show Seat Identifier
     */
    public String ussi() {
        return DigestUtils.md5DigestAsHex("%s_%s_%s_%s_%s".formatted(movieId, theatreId, showDate, startTime, seatId).getBytes());
    }
}
