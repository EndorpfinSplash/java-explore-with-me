package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.event_category.Location;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    @NotBlank
    @Length(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @NotBlank
    @Length(min = 20, max = 7000)
    private String description;


    @FutureOrPresent
    @NotNull(message = "The date and time must not be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Location location;

    private boolean paid;

    @Positive
    @Builder.Default
    private int participantLimit = 0;

    private boolean requestModeration;

    @NotBlank
    @Length(min = 3, max = 120)
    private String title;
}
