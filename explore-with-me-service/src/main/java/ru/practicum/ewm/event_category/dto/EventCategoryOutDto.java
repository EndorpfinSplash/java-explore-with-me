package ru.practicum.ewm.event_category.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventCategoryOutDto {
    private Long id;
    private String name;
}
