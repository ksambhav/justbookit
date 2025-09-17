package com.jbkit.catalogue.model;

import com.jbkit.catalogue.dto.SeatCoordinates;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "theatre")
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class Theatre {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "venue_id")
    private Venue venue;
    private String name;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "seats", columnDefinition = "jsonb")
    private Set<SeatCoordinates> seats;

    public Theatre(Venue venue, String name, Set<SeatCoordinates> seats) {
        this.venue = venue;
        this.name = name;
        this.seats = seats;
    }
}
