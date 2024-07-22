package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.event.dto.EventOutDto;

import javax.servlet.http.HttpServletRequest;
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
    public Collection<EventOutDto> getEvents(
            HttpServletRequest request,
            @RequestParam(value = "text", required = false) final String text,
            @RequestParam(value = "categories") final List<Integer> categories,
            @RequestParam(value = "paid") final boolean paid,
            @RequestParam(value = "rangeStart") final String rangeStart,
            @RequestParam(value = "rangeEnd") final String rangeEnd,
            @RequestParam(value = "onlyAvailable", defaultValue = "false") final boolean onlyAvailable,
            @RequestParam(value = "sort") final String sort,
            @RequestParam(value = "from", defaultValue = "0") final Integer from,
            @RequestParam(value = "size", defaultValue = "10") final Integer size) {

        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        log.info("GET request from collection of events.");
        return eventService.getPublicEvents(
                Optional.ofNullable(text),
                Optional.ofNullable(categories),
                Optional.of(paid),
                Optional.ofNullable(rangeStart),
                Optional.ofNullable(rangeEnd),
                onlyAvailable,
                Optional.ofNullable(sort),
                from,
                size
        );
    }

    public enum EventSortOrder {
        EVENT_DATE,
        VIEWS
    }

}
