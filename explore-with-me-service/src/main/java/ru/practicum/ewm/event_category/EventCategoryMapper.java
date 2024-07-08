package ru.practicum.ewm.event_category;

public class EventCategoryMapper {
    public static EventCategory eventCategoryCreationDtoToEventCategory(EventCategoryCreationDto eventCategoryCreationDto) {
        return EventCategory.builder()
                .name(eventCategoryCreationDto.getName())
                .build();
    }

    public static EventCategoryOutDto eventCategoryToEventCategoryOutDto(EventCategory eventCategory) {
        return EventCategoryOutDto.builder()
                .id(eventCategory.getId())
                .name(eventCategory.getName())
                .build();
    }
}
