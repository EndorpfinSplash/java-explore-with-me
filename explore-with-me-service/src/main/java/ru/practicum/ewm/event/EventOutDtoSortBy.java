package ru.practicum.ewm.event;


import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.ewm.event.dto.EventOutDto;

import java.util.Comparator;

@Getter
@AllArgsConstructor
public enum EventOutDtoSortBy {
    EVENT_DATE(Comparator.comparing(EventOutDto::getEventDate)),
    VIEWS(Comparator.comparingLong(EventOutDto::getViews));

    private final Comparator<EventOutDto> comparator;

}
