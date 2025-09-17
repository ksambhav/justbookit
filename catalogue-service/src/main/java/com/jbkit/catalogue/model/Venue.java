package com.jbkit.catalogue.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Table(name = "venue")
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class Venue {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String name;
    private String city;
    private String address;

    public Venue(String name, String city, String address) {
        this.name = name;
        this.city = city;
        this.address = address;
    }
}
