package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event_category.EventLocation;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EventCreationDto {

    private String annotation;

    @NotBlank
    private long categoryId;

    private String description;


    @FutureOrPresent
    @NotNull
    private LocalDateTime eventDate;

    private EventLocation location;

    private boolean paid;

    private int participantLimit;

    private boolean requestModeration;

    private String title;
}
