package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;

import jakarta.servlet.http.HttpServletRequest;
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

//        log.info("client ip: {}", httpServletRequest.getRemoteAddr());
//        log.info("endpoint path: {}", httpServletRequest.getRequestURI());
//        log.info("GET httpServletRequest from collection of events.");
        return eventService.getPublicEvents(
                httpServletRequest,
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
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable(value = "id") final Long id,
                                     HttpServletRequest httpServletRequest
    ) {
        return eventService.getEventById(
                httpServletRequest,
                id
        );
    }

}
