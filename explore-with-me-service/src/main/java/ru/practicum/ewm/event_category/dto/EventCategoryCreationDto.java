package ru.practicum.ewm.event_category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventCategoryCreationDto {

    @NotBlank(message = "Field: name. Error: must not be blank. Value: null")
    private String name;
}
