package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class EventControllerPrivate {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventOutDto createEvent(@RequestBody @Valid final EventCreationDto eventCreationDto,
                                   @PathVariable("userId") final Long userId) {
        log.info("Create event for user {} with event {}", userId, eventCreationDto);
        return eventService.createEvent(userId, eventCreationDto);
    }

    @GetMapping
    public Collection<EventOutDto> getEvents(@PathVariable("userId") final Long userId,
                                             @RequestParam(value = "from", defaultValue = "0") final Integer from,
                                             @RequestParam(value = "size", defaultValue = "10") final Integer size) {
        log.info("GET request from userId={} to fetch collection of events from = {} with size = {} received.", userId, from, size);
        return eventService.getUserEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventOutDto getEventById(@PathVariable("userId") final Long userId,
                                    @PathVariable("eventId") final Long eventId
    ) {
        log.info("GET request from userId={} to fetch  eventId={} received.", userId, eventId);
        return eventService.getUserEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventOutDto patchEventById(@PathVariable("userId") final Long userId,
                                      @PathVariable("eventId") final Long eventId,
                                      @RequestBody @Valid final EventUpdateDto eventUpdateDto
    ) {
        log.info("GET request from userId={} to fetch  eventId={} received.", userId, eventId);
        return eventService.patchUserEventById(userId, eventId, eventUpdateDto);
    }

    @GetMapping("/{eventId}/requests")
    public Collection<ParticipationRequestDto> getAllRequestsForEvent(@PathVariable("userId") final Long userId,
                                                                      @PathVariable("eventId") final Long eventId
    ) {
        log.info("GET all requests for eventId={} created by userId={}.", eventId, userId);
        return eventService.getEventRequests(userId, eventId);
    }


    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult patchEventByIds(@PathVariable("userId") final Long userId,
                                                          @PathVariable("eventId") final Long eventId,
                                                          @RequestBody @Valid final EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest
    ) {
        log.info("Patch request from userId={} to update requests for eventId={} received.", userId, eventId);
        return eventService.patchRequests(userId, eventId, eventRequestStatusUpdateRequest);
    }

}
