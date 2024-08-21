package ru.practicum.ewm.event.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Comparator;

@Getter
@AllArgsConstructor
public enum EventShortDtoSortBy {
    EVENT_DATE(Comparator.comparing(EventShortDto::getEventDate)),
    VIEWS(Comparator.comparingLong(EventShortDto::getViews));

    private final Comparator<EventShortDto> comparator;

}
