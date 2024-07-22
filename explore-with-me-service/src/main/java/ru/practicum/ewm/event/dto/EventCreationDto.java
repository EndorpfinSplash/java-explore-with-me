package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event_category.EventLocation;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EventCreationDto {

    @NotBlank
    @Min(20L)
    @Max(2000L)
    private String annotation;

    private Long category;

    @NotBlank
    @Min(20L)
    @Max(7000L)
    private String description;


    @FutureOrPresent
    @NotNull(message = "The date and time must not be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private EventLocation location;

    private boolean paid;

    @Positive
    @Builder.Default
    private int participantLimit = 0;

    private boolean requestModeration;

    @NotBlank
    @Min(3L)
    @Max(120L)
    private String title;
}
