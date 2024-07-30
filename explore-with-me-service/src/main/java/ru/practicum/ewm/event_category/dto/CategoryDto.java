package ru.practicum.ewm.event_category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    @NotBlank(message = "Field: name. Error: must not be blank. Value: null")
    @Size(min = 1, max = 50)
    private String name;
}
