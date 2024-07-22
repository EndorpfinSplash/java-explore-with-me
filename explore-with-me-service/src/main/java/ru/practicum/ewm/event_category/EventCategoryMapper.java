package ru.practicum.ewm.event_category;

import ru.practicum.ewm.event_category.dto.CategoryDto;
import ru.practicum.ewm.event_category.dto.CategoryOutDto;

public class EventCategoryMapper {
    public static EventCategory categoryDtoToEventCategory(CategoryDto categoryDto) {
        return EventCategory.builder()
                .name(categoryDto.getName())
                .build();
    }

    public static CategoryOutDto eventCategoryToEventCategoryOutDto(EventCategory eventCategory) {
        return CategoryOutDto.builder()
                .id(eventCategory.getId())
                .name(eventCategory.getName())
                .build();
    }
}
