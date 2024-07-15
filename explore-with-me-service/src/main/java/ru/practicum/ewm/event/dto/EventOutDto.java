package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event_category.EventCategory;
import ru.practicum.ewm.event_category.EventLocation;
import ru.practicum.ewm.user.User;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EventOutDto {

    private long id;

    private String annotation;

    private EventCategory category;

    private long confirmedRequests;

    private LocalDateTime createdOn;

    private String description;

    private LocalDateTime eventDate;

    private User initiator;

    private EventLocation location;

    private boolean paid;

    private int participantLimit;

    private LocalDateTime publishedOn;

    private boolean requestModeration;

    private String state; //TODO create STATE ENUM

    private String title;

    private long views; //TODO calculable
}
