package ru.practicum.ewm.location.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLocationDto {

    @Length(min = 1, max = 1000)
    private String name;

    @Length(max = 2000)
    private String description;

    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private Double locationLat;


    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private Double locationLon;

    @Positive
    private Double radius;

}
