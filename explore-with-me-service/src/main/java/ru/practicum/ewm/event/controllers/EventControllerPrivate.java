package ru.practicum.ewm.event.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class EventControllerPrivate {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@RequestBody @Valid final NewEventDto newEventDto,
                                    @PathVariable("userId") final Long userId) {
        log.info("Create event for user {} with event {}", userId, newEventDto);
        EventFullDto event = eventService.createEvent(userId, newEventDto);
        log.info("Event {} created", event);
        return event;
    }

    @GetMapping
    public Collection<EventFullDto> getEvents(@PathVariable("userId") final Long userId,
                                              @RequestParam(value = "from", defaultValue = "0") final Integer from,
                                              @RequestParam(value = "size", defaultValue = "10") final Integer size) {
        log.info("GET request from userId={} to fetch collection of events from = {} with size = {} received.", userId, from, size);
        Collection<EventFullDto> userEvents = eventService.getUserEvents(userId, from, size);
        log.info("{} events was received.", userEvents.size());
        return userEvents;
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable("userId") final Long userId,
                                     @PathVariable("eventId") final Long eventId
    ) {
        log.info("GET request from userId={} to fetch  eventId={} received.", userId, eventId);
        EventFullDto eventById = eventService.getUserEventById(userId, eventId);
        log.info("{} event was received.", eventById);
        return eventById;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEventById(@PathVariable("userId") final Long userId,
                                       @PathVariable("eventId") final Long eventId,
                                       @RequestBody @Valid final UpdateEventUserRequest updateEventUserRequest
    ) {
        log.info("Patch request from userId={} to fetch  eventId={} received.", userId, eventId);
        EventFullDto eventFullDto = eventService.patchUserEventById(userId, eventId, updateEventUserRequest);
        log.info("{} event was updated.", eventFullDto);
        return eventFullDto;
    }

    @GetMapping("/{eventId}/requests")
    public Collection<ParticipationRequestDto> getAllRequestsForEvent(@PathVariable("userId") final Long userId,
                                                                      @PathVariable("eventId") final Long eventId
    ) {
        log.info("GET all requests for eventId={} created by userId={}.", eventId, userId);
        List<ParticipationRequestDto> eventRequests = eventService.getEventRequests(userId, eventId);
        log.info("{} requests were received.", eventRequests.size());
        return eventRequests;
    }


    @PatchMapping({"/{eventId}/requests", "/{eventId}/requests/"})
    public EventRequestStatusUpdateResult patchEventByIds(@PathVariable("userId") final Long userId,
                                                          @PathVariable("eventId") final Long eventId,
                                                          @RequestBody @Valid final EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest
    ) {
        log.info("Patch request from userId={} to update requests for eventId={} received.", userId, eventId);
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = eventService.patchRequests(userId, eventId, eventRequestStatusUpdateRequest);
        log.info("{} request was updated.", eventRequestStatusUpdateResult);
        return eventRequestStatusUpdateResult;
    }
}
