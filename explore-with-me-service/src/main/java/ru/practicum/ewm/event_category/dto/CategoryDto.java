package ru.practicum.ewm.event_category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    @NotBlank(message = "Field: name. Error: must not be blank. Value: null")
    @Min(1L)
    @Max(50L)
    private String name;
}
