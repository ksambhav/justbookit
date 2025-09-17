package com.jbkit.catalogue.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledShow {

    @Id
    String id;

    @Embedded
    private ScheduledShowId scheduledShowId;
    private LocalTime endTime;
    private String city;
    private String venue;
    private String movieTitle;
    private LocalDateTime showTime;

    private boolean soldOut = false;
    private boolean cancelled = false;
}
