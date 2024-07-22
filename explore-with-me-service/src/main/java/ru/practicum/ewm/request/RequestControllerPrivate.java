package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.Collection;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestControllerPrivate {

    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable("userId") final Long userId,
                                                 @RequestParam("eventId") final Long eventId) {
        log.info("Create request from user {} for event_id={}", userId, eventId);
        return requestService.createRequest(userId, eventId);
    }

    @GetMapping
    public Collection<ParticipationRequestDto> getAllUserRequests(@PathVariable("userId") final Long userId) {
        log.info("Get all request for user {} ", userId);
        return requestService.getAllRequest(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable("userId") final Long userId
            , @PathVariable("requestId") final Long requestId) {
        log.info("Cancel request_id={} for user {} ",requestId, userId);
        return requestService.cancelRequest(userId, requestId);
    }

}
