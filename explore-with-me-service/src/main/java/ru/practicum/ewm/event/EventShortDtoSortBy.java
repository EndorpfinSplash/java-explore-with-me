package ru.practicum.ewm.event;


import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.Comparator;

@Getter
@AllArgsConstructor
public enum EventShortDtoSortBy {
    EVENT_DATE(Comparator.comparing(EventShortDto::getEventDate)),
    VIEWS(Comparator.comparingLong(EventShortDto::getViews));

    private final Comparator<EventShortDto> comparator;

}
