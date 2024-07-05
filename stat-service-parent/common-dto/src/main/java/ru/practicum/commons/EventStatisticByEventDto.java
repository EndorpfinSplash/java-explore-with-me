package ru.practicum.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventStatisticByEventDto {
    String app;
    String uri;
    Long hits;
}
