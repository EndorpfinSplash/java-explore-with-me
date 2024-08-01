package ru.practicum.ewm.event_category;

import ru.practicum.ewm.event_category.dto.CategoryDto;
import ru.practicum.ewm.event_category.dto.NewCategoryDto;

public class EventCategoryMapper {
    public static EventCategory categoryDtoToEventCategory(NewCategoryDto newCategoryDto) {
        return EventCategory.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public static CategoryDto eventCategoryToEventCategoryOutDto(EventCategory eventCategory) {
        return CategoryDto.builder()
                .id(eventCategory.getId())
                .name(eventCategory.getName())
                .build();
    }
}
