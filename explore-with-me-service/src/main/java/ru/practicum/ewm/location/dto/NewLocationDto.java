package ru.practicum.ewm.location.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewLocationDto {

    @NotBlank
    @Length(min = 1, max = 1000)
    private String name;

    @Length(max = 2000)
    private String description;

    private Double locationLat;

    private Double locationLon;

    @Positive
    private Double radius;

}
