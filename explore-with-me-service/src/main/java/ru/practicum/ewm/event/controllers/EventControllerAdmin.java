package ru.practicum.ewm.event.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Slf4j
public class EventControllerAdmin {

    private final EventService eventService;

    @GetMapping(produces = "application/json")
    public Collection<EventFullDto> getAdminEvents(
            @RequestParam(value = "users", required = false) final List<Integer> users,
            @RequestParam(value = "states", required = false) final List<String> states,
            @RequestParam(value = "categories", required = false) final List<Integer> categories,
            @RequestParam(value = "rangeStart", required = false) final String rangeStart,
            @RequestParam(value = "rangeEnd", required = false) final String rangeEnd,
            @RequestParam(value = "from", defaultValue = "0") final Integer from,
            @RequestParam(value = "size", defaultValue = "10") final Integer size) {
        log.info("GET request to fetch list of events received.");
        Collection<EventFullDto> adminEvents = eventService.getAdminEvents(
                Optional.empty(),
                Optional.ofNullable(users),
                Optional.ofNullable(states),
                Optional.ofNullable(categories),
                Optional.ofNullable(rangeStart),
                Optional.ofNullable(rangeEnd),
                from,
                size
        );
        log.info("{} events were fetched.", adminEvents.size());
        return adminEvents;
    }

    @GetMapping("/in-location/{locationId}")
    public Collection<EventFullDto> getAdminEventsInLocation(
            @PathVariable(name = "locationId") final Long locationId,
            @RequestParam(value = "users", required = false) final List<Integer> users,
            @RequestParam(value = "states", required = false) final List<String> states,
            @RequestParam(value = "categories", required = false) final List<Integer> categories,
            @RequestParam(value = "rangeStart", required = false) final String rangeStart,
            @RequestParam(value = "rangeEnd", required = false) final String rangeEnd,
            @RequestParam(value = "from", defaultValue = "0") final Integer from,
            @RequestParam(value = "size", defaultValue = "10") final Integer size) {
        log.info("GET request to fetch list of events in location_id = {} received.",locationId);
        Collection<EventFullDto> adminEvents = eventService.getAdminEvents(
                Optional.ofNullable(locationId),
                Optional.ofNullable(users),
                Optional.ofNullable(states),
                Optional.ofNullable(categories),
                Optional.ofNullable(rangeStart),
                Optional.ofNullable(rangeEnd),
                from,
                size
        );
        log.info("{} events in location were fetched.", adminEvents.size());
        return adminEvents;
    }


    @PatchMapping("/{eventId}")
    public EventFullDto patchEventById(
            @PathVariable("eventId") final Long eventId,
            @RequestBody @Valid final UpdateEventAdminRequest updateEventAdminRequest
    ) {
        log.info("PATCH request to update eventId={} received.", eventId);
        EventFullDto eventFullDto = eventService.patchAdminEventById(eventId, updateEventAdminRequest);
        log.info("event = {} was updated.", eventFullDto);
        return eventFullDto;
    }

}
