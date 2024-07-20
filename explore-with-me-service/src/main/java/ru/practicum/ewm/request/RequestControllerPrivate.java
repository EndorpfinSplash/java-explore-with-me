package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventOutDto;
import ru.practicum.ewm.event.dto.EventUpdateDto;
import ru.practicum.ewm.request.dto.RequestCreationDto;
import ru.practicum.ewm.request.dto.RequestOutDto;

import java.util.Collection;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestControllerPrivate {

    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestOutDto createRequest(@RequestBody final RequestCreationDto requestCreationDto,
                                       @PathVariable("userId") final Long userId,
                                       @RequestParam("eventId")final Long eventId) {
        log.info("Create request from user {} for event_id={}", userId, requestCreationDto);
        return requestService.createRequest(userId, eventId, requestCreationDto);
    }

//    @GetMapping
//    public Collection<EventOutDto> getEvents(@PathVariable("userId") final Integer userId,
//                                             @RequestParam(value = "from", defaultValue = "0") final Integer from,
//                                             @RequestParam(value = "size", defaultValue = "10") final Integer size) {
//        log.info("GET request from userId={} to fetch collection of events from = {} with size = {} received.", userId, from, size);
//        return requestService.getUserEvents(userId, from, size);
//    }
//
//    @GetMapping("/{eventId}")
//    public EventOutDto getEventById(@PathVariable("userId") final Integer userId,
//                                    @PathVariable("eventId") final Integer eventId
//    ) {
//        log.info("GET request from userId={} to fetch  eventId={} received.", userId, eventId);
//        return requestService.getUserEventById(userId, eventId);
//    }
//
//    @PatchMapping("/{eventId}")
//    public EventOutDto patchEventById(@PathVariable("userId") final Integer userId,
//                                      @PathVariable("eventId") final Integer eventId,
//                                      @RequestBody final EventUpdateDto eventUpdateDto
//    ) {
//        log.info("GET request from userId={} to fetch  eventId={} received.", userId, eventId);
//        return requestService.patchUserEventById(userId, eventId, eventUpdateDto);
//    }

}
