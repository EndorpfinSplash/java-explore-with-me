package ru.practicum.ewm.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestsCountByEvent {
    private Long eventId;
    private Long count;
}
