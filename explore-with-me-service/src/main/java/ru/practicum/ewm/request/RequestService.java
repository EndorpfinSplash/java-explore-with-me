package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.EventStatus;
import ru.practicum.ewm.exception.EventNotFoundException;
import ru.practicum.ewm.exception.NotValidRequestException;
import ru.practicum.ewm.exception.UserNotFoundException;
import ru.practicum.ewm.request.dto.RequestCreationDto;
import ru.practicum.ewm.request.dto.RequestOutDto;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestService {
    RequestRepository requestRepository;
    UserRepository userRepository;
    EventRepository eventRepository;

    public RequestOutDto createRequest(Long userId, Long eventId, RequestCreationDto requestCreationDto) {
        User requester = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(MessageFormat.format("Requester with userId={0} not found", userId))
        );
        Event event = eventRepository.findEventByUserAndId(requester, eventId)
                .orElseThrow(
                        () -> new EventNotFoundException(
                                MessageFormat.format("Event with id={0} was not found", eventId)
                        )
                );

        if (event.getEventStatus() != EventStatus.PUBLISHED) {
            throw new NotValidRequestException(
                    MessageFormat.format("Event_id={0} hasn't published yes", eventId)
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

        int participantsCnt = requestRepository.countRequestByEventAndStatus(event, RequestStatus.ACCEPTED);
        if (participantsCnt > event.getParticipantLimit()) {
            throw new NotValidRequestException(
                    MessageFormat.format("Event_id={0} has achieved participants limit={1}", eventId, participantsCnt)
            );
        }

        return null;
    }
}
