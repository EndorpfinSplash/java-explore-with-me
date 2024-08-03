package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Slf4j
public class EventControllerAdmin {

    private final EventService eventService;

    @GetMapping
    public Collection<EventFullDto> getAdminEvents(
            @RequestParam(value = "users", required = false) final List<Integer> users,
            @RequestParam(value = "states", required = false) final List<String> states,
            @RequestParam(value = "categories", required = false) final List<Integer> categories,
            @RequestParam(value = "rangeStart", required = false) final String rangeStart,
            @RequestParam(value = "rangeEnd", required = false) final String rangeEnd,

            @RequestParam(value = "from", defaultValue = "0") final Integer from,
            @RequestParam(value = "size", defaultValue = "10") final Integer size) {

        return eventService.getAdminEvents(
                Optional.ofNullable(users),
                Optional.ofNullable(states),
                Optional.ofNullable(categories),
                Optional.ofNullable(rangeStart),
                Optional.ofNullable(rangeEnd),
                from,
                size
        );
    }


    @PatchMapping("/{eventId}")
    public EventFullDto patchEventById(
            @PathVariable("eventId") final Long eventId,
            @RequestBody @Valid final UpdateEventAdminRequest updateEventAdminRequest
    ) {
        log.info("PATCH request to update eventId={} received.", eventId);
        return eventService.patchAdminEventById(eventId, updateEventAdminRequest);
    }

}
