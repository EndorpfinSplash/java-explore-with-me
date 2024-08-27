package ru.practicum.ewm.event.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
public class EventControllerPublic {

    private final EventService eventService;

    @GetMapping
    public Collection<EventShortDto> getEvents(
            HttpServletRequest httpServletRequest,
            @RequestParam(value = "text", required = false) final String text,
            @RequestParam(value = "categories", required = false) final List<Integer> categories,
            @RequestParam(value = "paid", required = false) final Boolean paid,
            @RequestParam(value = "rangeStart", required = false) final String rangeStart,
            @RequestParam(value = "rangeEnd", required = false) final String rangeEnd,
            @RequestParam(value = "onlyAvailable", defaultValue = "false") final boolean onlyAvailable,
            @RequestParam(value = "sort", required = false) final String sort,
            @RequestParam(value = "from", defaultValue = "0") final Integer from,
            @RequestParam(value = "size", defaultValue = "10") final Integer size) {
        log.info("GET request to fetch events was received");
        List<EventShortDto> publicEvents = eventService.getPublicEvents(
                httpServletRequest,
                Optional.empty(),
                Optional.ofNullable(text),
                Optional.ofNullable(categories),
                Optional.ofNullable(paid),
                Optional.ofNullable(rangeStart),
                Optional.ofNullable(rangeEnd),
                onlyAvailable,
                Optional.ofNullable(sort),
                from,
                size
        );
        log.info("{} events was received", publicEvents.size());
        return publicEvents;
    }

    @GetMapping("/in-location/{locationId}")
    public Collection<EventShortDto> getEventsForLocation(
            HttpServletRequest httpServletRequest,
            @PathVariable(name = "locationId") final Long locationId,
            @RequestParam(value = "text", required = false) final String text,
            @RequestParam(value = "categories", required = false) final List<Integer> categories,
            @RequestParam(value = "paid", required = false) final Boolean paid,
            @RequestParam(value = "rangeStart", required = false) final String rangeStart,
            @RequestParam(value = "rangeEnd", required = false) final String rangeEnd,
            @RequestParam(value = "onlyAvailable", defaultValue = "false") final boolean onlyAvailable,
            @RequestParam(value = "sort", required = false) final String sort,
            @RequestParam(value = "from", defaultValue = "0") final Integer from,
            @RequestParam(value = "size", defaultValue = "10") final Integer size) {
        log.info("GET request to fetch events for location_id = {} was received", locationId);
        List<EventShortDto> locationEvents = eventService.getPublicEvents(
                httpServletRequest,
                Optional.ofNullable(locationId),
                Optional.ofNullable(text),
                Optional.ofNullable(categories),
                Optional.ofNullable(paid),
                Optional.ofNullable(rangeStart),
                Optional.ofNullable(rangeEnd),
                onlyAvailable,
                Optional.ofNullable(sort),
                from,
                size
        );
        log.info("{} events in location was received", locationEvents.size());
        return locationEvents;
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable(value = "id") final Long id,
                                     HttpServletRequest httpServletRequest
    ) {
        log.info("GET request to fetch event was received");
        EventFullDto eventById = eventService.getEventById(httpServletRequest, id);
        log.info("{} event was received", eventById);
        return eventById;
    }

}
