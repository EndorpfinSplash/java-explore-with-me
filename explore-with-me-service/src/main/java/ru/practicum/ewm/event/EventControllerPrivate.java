package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventCreationDto;
import ru.practicum.ewm.event.dto.EventOutDto;

import java.util.Collection;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class EventControllerPrivate {

    private final EventService eventService;

    @PostMapping
    public EventOutDto createEvent(@RequestBody final EventCreationDto eventCreationDto,
                                   @PathVariable("userId") final Integer userId) {
        log.info("Create event for user {} with event {}", userId, eventCreationDto);
        return eventService.createEvent(userId, eventCreationDto);
    }

    @GetMapping
    public Collection<EventOutDto> getEvents(@PathVariable("userId") final Integer userId,
                                             @RequestParam(value = "from",defaultValue = "0") final Integer from,
                                             @RequestParam(value = "size",defaultValue = "10") final Integer size) {
        log.info("GET request from userId={} to fetch collection of events from = {} with size = {} received.",userId, from, size);
        return eventService.getUserEvents(userId,  from,  size);
    }


}
