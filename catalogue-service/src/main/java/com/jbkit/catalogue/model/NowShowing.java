package com.jbkit.catalogue.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@EqualsAndHashCode(of = {"id"})
@Entity
public class NowShowing {

    @Id
    private String id;

    private String city;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "movie_title")
    private String movieTitle;

    @Column(name = "movie_id")
    private String movieId;

    @Column(name = "show_date")
    private LocalDate showDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "theatre_id")
    private String theatreId;

    private String venue;

    @Column(name = "show_time")
    private LocalDateTime showTime;

    private Boolean cancelled;

    @Column(name = "sold_out")
    private Boolean soldOut;
}