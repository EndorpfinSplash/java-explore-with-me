package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.StatisticRestClient;
import ru.practicum.commons.EndpointHit;
import ru.practicum.commons.ViewStats;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event_category.EventCategory;
import ru.practicum.ewm.event_category.EventCategoryRepository;
import ru.practicum.ewm.exception.*;
import ru.practicum.ewm.request.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    public static final String EWM_MAIN_SERVICE_NAME = "ewm-main-service";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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

    public List<EventOutDto> getPublicEvents(HttpServletRequest httpServletRequest,
                                             Optional<String> text,
                                             Optional<List<Integer>> categories,
                                             Optional<Boolean> paid,
                                             Optional<String> rangeStart,
                                             Optional<String> rangeEnd,
                                             boolean onlyAvailable,
                                             Optional<String> sort,
                                             Integer from,
                                             Integer size
    ) {
        EndpointHit endpointHit = EndpointHit.builder()
                .app(EWM_MAIN_SERVICE_NAME)
                .ip(httpServletRequest.getRemoteAddr())
                .uri(httpServletRequest.getRequestURI())
                .build();
        StatisticRestClient.sendData(endpointHit);

        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> event = query.from(Event.class);

//        if (sort.isPresent()) {
//            query = query.orderBy(cb.asc(event.get(sort.get())));
//        }

        List<Predicate> predicates = new ArrayList<>();
        Predicate statusPublished = cb.equal(event.get("status"), EventStatus.PUBLISHED);
        predicates.add(statusPublished);

        categories.ifPresent(categoriesList ->
                {
                    Predicate inCategoryPredicate = event.get("category").in(categoriesList);
                    predicates.add(inCategoryPredicate);
                }
        );

        paid.ifPresent(isPaid ->
                {
                    Predicate isPaidPredicate = cb.equal(event.get("paid"), isPaid);
                    predicates.add(isPaidPredicate);
                }
        );

        Predicate isOnlyAvailablePredicate = cb.equal(event.get("onlyAvailable"), onlyAvailable);
        predicates.add(isOnlyAvailablePredicate);

        text.ifPresent(
                txt ->
                {
                    String pattern = "%" + txt.toLowerCase() + "%";
                    Predicate descriptionLike = cb.like(cb.lower(event.get("description")), pattern);
                    Predicate annotationLike = cb.like(cb.lower(event.get("annotation")), pattern);
                    predicates.add(cb.or(descriptionLike, annotationLike));
                }
        );

        Predicate startFromPredicate =
                cb.greaterThan(event.get("EVENT_DATE"),
                        rangeStart.isEmpty() ? LocalDateTime.now() :
                                LocalDateTime.parse(rangeStart.get())
                );
        predicates.add(startFromPredicate);

        rangeEnd.ifPresent(
                endTime -> {
                    predicates.add(
                            cb.lessThan(event.get("EVENT_DATE"),
                                    LocalDateTime.parse(rangeStart.get()))
                    );
                }
        );

        query.where(predicates.toArray(new Predicate[0]));

        Order order = cb.asc(event.get("name"));
        query.orderBy(order);

        List<Event> eventsList = entityManager.createQuery(query)
//                .setFirstResult((int) page.getOffset())
//                .setMaxResults(page.getPageSize())
                .getResultList();

        Map<Long, Long> requestsByEvent = requestRepository.countRequestByEventId(RequestStatus.CONFIRMED).stream()
                .collect(Collectors.toMap(
                        RequestsCountByEvent::getEventId,
                        RequestsCountByEvent::getCount)
                );

        String encodedStartDate = URLEncoder.encode(LocalDateTime.of(0, 1, 1, 0, 0)
                .format(DATE_TIME_FORMATTER), StandardCharsets.UTF_8);
//        String encodedStartDate = URLEncoder.encode(LocalDateTime.MIN.format(DATE_TIME_FORMATTER), StandardCharsets.UTF_8);
        String encodedEndDate = URLEncoder.encode(LocalDateTime.now().format(DATE_TIME_FORMATTER), StandardCharsets.UTF_8);
        List<ViewStats> viewStats = StatisticRestClient.getData(encodedStartDate, encodedEndDate, null, null);

        Map<Long, Long> viewsByEvent =
                viewStats.stream()
                        .filter(viewStatsLine -> viewStatsLine.getApp().equals(EWM_MAIN_SERVICE_NAME))
                        .collect(Collectors.toMap(
                                viewStatsLine -> {
                                    int lastSlashIndex = viewStatsLine.getUri().lastIndexOf('/');
                                    Long eventId = Long.valueOf(viewStatsLine.getUri().substring(lastSlashIndex + 1));
                                    return eventId;
                                },
                                ViewStats::getHits
                        ));

        List<EventOutDto> eventOutDtoList = eventsList.stream()
                .filter(currentEvent -> {
                    if (onlyAvailable && currentEvent.getParticipantLimit() - requestsByEvent.get(currentEvent.getId()) > 0) {
                        return true;
                    }
                    return true;
                })
                .map(eventEntity -> EventMapper.eventToOutDto(
                        eventEntity,
                        viewsByEvent.get(eventEntity.getId()),
                        requestsByEvent.get(eventEntity.getId()))
                )
                .collect(Collectors.toList());

        if (sort.isPresent()) {
            try {
                EventOutDtoSortBy eventOutDtoSortBy = EventOutDtoSortBy.valueOf(sort.get());
                eventOutDtoList = eventOutDtoList.stream().sorted(eventOutDtoSortBy.getComparator()).collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                //TODO add custom except
                throw new RuntimeException(e);
            }
        }
        return eventOutDtoList.subList((int) page.getOffset(), page.getPageSize());
    }
}
