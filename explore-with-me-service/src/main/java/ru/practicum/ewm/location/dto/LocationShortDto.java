package ru.practicum.ewm.location.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationShortDto {

    private String name;
    private String description;
    private double locationLat;
    private double locationLon;
    private double radius;
}
