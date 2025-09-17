package com.jbkit.catalogue.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode(of = {"id"})
public class Movie {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    @Column(unique = true)
    private String title;
    private LocalDate releaseDate;
    private int durationInMins;
    private String posterUrl;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "info", columnDefinition = "jsonb")
    private MovieInfo movieInfo;

    public record MovieInfo(List<String> actors, List<String> director, String genre) {

    }

}
