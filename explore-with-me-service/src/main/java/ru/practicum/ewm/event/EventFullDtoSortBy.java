package ru.practicum.ewm.event;


import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.ewm.event.dto.EventFullDto;

import java.util.Comparator;

@Getter
@AllArgsConstructor
public enum EventFullDtoSortBy {
    EVENT_DATE(Comparator.comparing(EventFullDto::getEventDate)),
    VIEWS(Comparator.comparingLong(EventFullDto::getViews));

    private final Comparator<EventFullDto> comparator;

}
