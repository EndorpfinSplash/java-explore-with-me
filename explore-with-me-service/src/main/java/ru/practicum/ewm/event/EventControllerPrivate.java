package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventCreationDto;
import ru.practicum.ewm.event.dto.EventOutDto;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class EventControllerPrivate {

    private final EventService eventService;

    @PostMapping
    public EventOutDto createEvent(@RequestBody EventCreationDto eventCreationDto,
                                   @PathVariable("userId") String userId) {
        log.info("Create event for user {} with event {}", userId, eventCreationDto);
        return eventService.createEvent(userId, eventCreationDto);
    }
}
