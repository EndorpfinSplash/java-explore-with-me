package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event_category.EventLocation;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EventCreationDto {

    private String annotation;

    private long categoryId;

    private String description;

    private LocalDateTime localDateTime;

    private EventLocation location;

    private boolean paid;

    private int participantLimit;

    private boolean requestModeration;

    private String title;
}
