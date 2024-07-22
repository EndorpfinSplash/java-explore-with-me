package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event_category.EventCategory;
import ru.practicum.ewm.event_category.EventCategoryRepository;
import ru.practicum.ewm.exception.*;
import ru.practicum.ewm.request.Request;
import ru.practicum.ewm.request.RequestMapper;
import ru.practicum.ewm.request.RequestRepository;
import ru.practicum.ewm.request.RequestStatus;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final RequestRepository requestRepository;
    private final EntityManager entityManager;

    public EventOutDto createEvent(Long userId, EventCreationDto eventCreationDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(MessageFormat.format("User with userId={0} not found", userId))
        );

        Long eventCategoryId = eventCreationDto.getCategory();
        EventCategory eventCategory = eventCategoryRepository.findById(eventCategoryId).orElseThrow(
                () -> new EventCategoryNotFoundException(MessageFormat.format("Category with id={0} was not found",
                        eventCategoryId)
                )
        );
        if (eventCreationDto.getEventDate().minusHours(2L).isBefore(LocalDateTime.now())) {
            throw new EventNotValidArgumentException("Event should be announced 2 hours earlier then event");
        }
        Event eventForSave = EventMapper.creationDtoToEvent(eventCreationDto, user, eventCategory);
        Event savedEvent = eventRepository.save(eventForSave);
        return EventMapper.eventToOutDto(savedEvent, 0L, 0L);
    }


    public Collection<EventOutDto> getUserEvents(Long userId, Integer from, Integer size) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(MessageFormat.format("User with userId={0} not found", userId))
        );
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);
        return eventRepository.findAllByUserIdOrderById(userId, page)
                .stream()
                .map(
                        //TODO define views and confirms for each
                        event -> EventMapper.eventToOutDto(event, 0L, 0L)
                )
                .collect(Collectors.toList());
    }

    public EventOutDto getUserEventById(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(MessageFormat.format("User with userId={0} not found", userId))
        );
        //TODO define views and confirms
        Event eventByUserAndId = eventRepository.findEventByUserAndId(user, eventId)
                .orElseThrow(
                        () -> new EventNotFoundException(MessageFormat.format("Event with id={0} was not found", eventId))
                );
        return EventMapper.eventToOutDto(eventByUserAndId, 0L, 0L);
    }

    public EventOutDto patchUserEventById(Long userId, Long eventId, EventUpdateDto eventUpdateDto) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(MessageFormat.format("User with userId={0} not found", userId))
        );
        //TODO define views and confirms
        Event eventForUpdate = eventRepository.findEventByUserAndId(user, eventId)
                .orElseThrow(
                        () -> new EventNotFoundException(MessageFormat.format("Event with id={0} was not found", eventId))
                );
        if (!(eventForUpdate.getEventStatus() == EventStatus.WAITING ||
                eventForUpdate.getEventStatus() == EventStatus.CANCELED)
        ) {
            throw new NotApplicableEvent("Only pending or canceled events can be changed");
        }

        if (eventUpdateDto.getEventDate() != null && eventUpdateDto.getEventDate().minusHours(2L).isBefore(LocalDateTime.now())) {
            throw new EventNotValidArgumentException("Event should be announced 2 hours earlier then event");
        }

        Long eventCategoryId = eventUpdateDto.getCategory();
        if (eventCategoryId != null) {
            EventCategory eventCategory = eventCategoryRepository.findById(eventCategoryId).orElseThrow(
                    () -> new EventCategoryNotFoundException(MessageFormat.format("Category with id={0} was not found",
                            eventCategoryId)
                    )
            );
            eventForUpdate.setCategory(eventCategory);
        }
        return EventMapper.eventToOutDto(eventUpdateDto);
    }

    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(MessageFormat.format("User with userId={0} not found", userId))
        );
        Event event = eventRepository.findEventByUserAndId(user, eventId)
                .orElseThrow(
                        () -> new EventNotFoundException(MessageFormat.format("Event with id={0} was not found", eventId))
                );

        return requestRepository.findRequestByEventId(eventId)
                .stream()
                .map(RequestMapper::RequestToOutDto)
                .collect(Collectors.toList());
    }

    public EventRequestStatusUpdateResult patchRequests(final Long userId,
                                                        final Long eventId,
                                                        final EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {

        RequestStatus newStatus;
        try {
            newStatus = RequestStatus.valueOf(eventRequestStatusUpdateRequest.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IncorrectStatusException("Incorrect request status");
        }

        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(MessageFormat.format("User with userId={0} not found", userId))
        );

        Event event = eventRepository.findEventByUserAndId(user, eventId)
                .orElseThrow(
                        () -> new EventNotFoundException(MessageFormat.format("Event with id={0} was not found", eventId))
                );

        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            throw new ParticipantsLimitationException("This event's requests shouldn't be confirmed");
        }

        if (newStatus == RequestStatus.REJECTED) {
            setNewStatusesForRequests(eventRequestStatusUpdateRequest, newStatus);
        }

        if (newStatus == RequestStatus.CONFIRMED) {
            int confirmedRequestsCnt = requestRepository.countRequestByEventAndStatus(event, RequestStatus.CONFIRMED);
            int requestedConfirmsCnt = eventRequestStatusUpdateRequest.getRequestIds().size();
            if (confirmedRequestsCnt + requestedConfirmsCnt > event.getParticipantLimit()) {
                throw new ParticipantsLimitationException("The participant limit has been reached");
            }
            setNewStatusesForRequests(eventRequestStatusUpdateRequest, newStatus);
        }

        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        eventRequestStatusUpdateResult.setConfirmedRequests(requestRepository.getAllByStatusOrderById(RequestStatus.CONFIRMED));
        eventRequestStatusUpdateResult.setRejectedRequests(requestRepository.getAllByStatusOrderById(RequestStatus.REJECTED));
        return eventRequestStatusUpdateResult;
    }

    private void setNewStatusesForRequests(EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest, RequestStatus newStatus) {
        eventRequestStatusUpdateRequest.getRequestIds().forEach(
                requestId -> {
                    Request request = requestRepository.findById(requestId).orElseThrow(
                            () -> new RequestNotFoundException(MessageFormat.format("Request with id={0} was not found", requestId))
                    );
                    if (request.getStatus() != RequestStatus.PENDING) {
                        throw new IncorrectStatusException(
                                MessageFormat.format("Request with id={0} must have status PENDING", requestId)
                        );
                    }
                    request.setStatus(newStatus);
                    requestRepository.save(request);
                }
        );
    }

    public List<EventOutDto> getPublicEvents(Optional<String> text,
                                                   Optional<List<Integer>> categories,
                                                   Optional<Boolean> paid,
                                                   Optional<String> rangeStart,
                                                   Optional<String> rangeEnd,
                                                   boolean onlyAvailable,
                                                   Optional<String> sort,
                                                   Integer from,
                                                   Integer size
    ) {
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> event = query.from(Event.class);

        List<Predicate> predicates = new ArrayList<>();
        Predicate statusPublished = cb.equal(event.get("status"), EventStatus.PUBLISHED);
        predicates.add(statusPublished);

        text.ifPresent(
                txt ->
                {
                    String pattern = "%" + txt.toLowerCase() + "%";
                    Predicate descriptionLike = cb.like(cb.lower(event.get("description")), pattern);
                    Predicate annotationLike = cb.like(cb.lower(event.get("annotation")), pattern);
                    predicates.add(cb.or(descriptionLike, annotationLike));
                }

        );


        query.where(predicates.toArray(new Predicate[0]));

        List<Event> eventsList = entityManager.createQuery(query).getResultList();
        return eventsList.stream()
                .map(event1 -> EventMapper.eventToOutDto(event1, 0L , 0L))
                .collect(Collectors.toList());

    }
}
