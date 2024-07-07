package ru.practicum.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventStatisticOutDto {
    private String app;
    private String uri;
    private Long hits;
}
