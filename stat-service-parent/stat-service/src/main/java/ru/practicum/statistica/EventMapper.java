package ru.practicum.statistica;

import org.springframework.stereotype.Component;
import ru.practicum.commons.EventCreationDto;
import ru.practicum.commons.EventOutDto;

@Component
public class EventMapper {
    public static Event eventCreationDtoToEvent(EventCreationDto eventCreationDto) {
        return Event.builder()
                .app(eventCreationDto.getApp())
                .uri(eventCreationDto.getUri())
                .ip(eventCreationDto.getIp())
                .timestamp(eventCreationDto.getTimestamp())
                .build();
    }

    public static EventOutDto eventToEventOutDto(Event event) {
        return EventOutDto.builder()
                .app(event.getApp())
                .uri(event.getUri())
                .ip(event.getIp())
                .timestamp(event.getTimestamp())
                .build();
    }
}
