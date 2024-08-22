package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.EventStatus;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.exception.NotValidRequestException;
import ru.practicum.ewm.exception.ParticipantsLimitationException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserService;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final EventRepository eventRepository;

    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User requester = userService.findUser(userId);
        Event event = findEvent(eventId);

        if (event.getEventStatus() != EventStatus.PUBLISHED) {
            throw new NotValidRequestException(
                    MessageFormat.format("Event_id={0} hasn't published yet", eventId)
            );
        }

        if (requestRepository.findRequestByRequesterAndEvent(requester, event).isPresent()) {
            throw new NotValidRequestException(
                    MessageFormat.format("Request from requester_id={0} in event_id={1} already exist", requester, eventId)
            );
        }

        if (event.getUser().getId().equals(requester.getId())) {
            throw new NotValidRequestException(
                    MessageFormat.format("User with id={0} cant't take part in own event", eventId)
            );
        }

        int participantsCnt = requestRepository.countRequestByEventAndStatus(event, RequestStatus.CONFIRMED);
        if (participantsCnt >= event.getParticipantLimit() && event.getParticipantLimit() > 0) {
            throw new ParticipantsLimitationException("The participant limit has been reached");
        }

        Request request = Request.builder()
                .requester(requester)
                .event(event)
                .build();
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        }

        Request savedRequest = requestRepository.save(request);
        log.info("Request={} is created", savedRequest);
        return RequestMapper.requestToParticipationRequestDto(savedRequest);
    }

    public Collection<ParticipationRequestDto> getAllRequest(Long userId) {
        return requestRepository.getAllByRequesterIdOrderById(userId).stream()
                .map(RequestMapper::requestToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        User user = userService.findUser(userId);
        Request request = findRequest(requestId);
        if (!Objects.equals(user.getId(), request.getRequester().getId())) {
            throw new NotValidRequestException("You can cancel only your own requests");
        }
        request.setStatus(RequestStatus.CANCELED);
        Request savedRequest = requestRepository.save(request);
        return RequestMapper.requestToParticipationRequestDto(savedRequest);
    }

    private Request findRequest(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(
                () -> new EntityNotFoundException(
                        MessageFormat.format("Request with id={0} was not found", requestId)
                )
        );
    }

    private Event findEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                MessageFormat.format("Event with id={0} was not found", eventId)
                        )
                );
    }

}
