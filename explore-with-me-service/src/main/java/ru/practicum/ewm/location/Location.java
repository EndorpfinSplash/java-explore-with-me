package ru.practicum.ewm.location;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LOCATION")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "LOCATION_LAT")
    private double locationLat;

    @Column(name = "LOCATION_LON")
    private double locationLon;

    @Column(name = "RADIUS")
    private double radius;


    @Builder.Default
    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn = LocalDateTime.now();
}
