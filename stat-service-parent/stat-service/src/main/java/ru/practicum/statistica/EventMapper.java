package ru.practicum.statistica;

import org.springframework.stereotype.Component;
import ru.practicum.commons.EndpointHit;
import ru.practicum.commons.EventOutDto;

@Component
public class EventMapper {
    public static Event eventCreationDtoToEvent(EndpointHit endpointHit) {
        return Event.builder()
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp())
                .build();
    }

    public static EventOutDto eventToEventOutDto(Event event) {
        return EventOutDto.builder()
                .id(event.getId())
                .app(event.getApp())
                .uri(event.getUri())
                .ip(event.getIp())
                .timestamp(event.getTimestamp())
                .build();
    }
}
