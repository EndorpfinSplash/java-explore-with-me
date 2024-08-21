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
        ParticipationRequestDto request = requestService.createRequest(userId, eventId);
        log.info("Request created {}", request);
        return request;
    }

    @GetMapping
    public Collection<ParticipationRequestDto> getAllUserRequests(@PathVariable("userId") final Long userId) {
        log.info("Get all request for user {} ", userId);
        Collection<ParticipationRequestDto> allRequest = requestService.getAllRequest(userId);
        log.info("All request for user id= {} is: {} ",userId, allRequest);
        return allRequest;
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable("userId") final Long userId,
                                                 @PathVariable("requestId") final Long requestId) {
        log.info("Cancel request_id={} for user {} ", requestId, userId);
        ParticipationRequestDto participationRequestDto = requestService.cancelRequest(userId, requestId);
        log.info("Request {} was canceled for user_id = {}", participationRequestDto, userId);
        return participationRequestDto;
    }

}
