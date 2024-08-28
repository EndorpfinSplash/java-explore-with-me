package ru.practicum.ewm.event;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.StatisticRestClient;
import ru.practicum.commons.EndpointHit;
import ru.practicum.commons.ViewStats;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event_category.EventCategory;
import ru.practicum.ewm.event_category.EventCategoryService;
import ru.practicum.ewm.exception.*;
import ru.practicum.ewm.location.LocationRepository;
import ru.practicum.ewm.request.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserService;

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
    private final UserService userService;
    private final EventCategoryService eventCategoryService;
    private final RequestRepository requestRepository;
    private final EntityManager entityManager;
    private final LocationRepository locationRepository;

    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        User user = userService.findUser(userId);
        Long eventCategoryId = Long.valueOf(newEventDto.getCategory());
        EventCategory eventCategory = eventCategoryService.findEventCategory(eventCategoryId);
        if (newEventDto.getEventDate()
                .minusHours(2L).isBefore(LocalDateTime.now())) {
            throw new EventNotValidArgumentException("Event should be announced 2 hours earlier then event");
        }
        Event eventForSave = EventMapper.creationDtoToEvent(newEventDto, user, eventCategory);
        Event savedEvent = eventRepository.save(eventForSave);
        return EventMapper.eventToFullDto(savedEvent, 0L, 0L);
    }


    public Collection<EventFullDto> getUserEvents(Long userId, Integer from, Integer size) {
        userService.findUser(userId);
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);
        Map<Long, Long> viewsByEvent = getViewsStatisticMap();
        Map<Long, Long> confirmedRequestsByEvent = getConfirmedRequestsMap();
        return eventRepository.findAllByUserIdOrderById(userId, page).stream()
                .map(
                        event -> EventMapper.eventToFullDto(event, viewsByEvent.getOrDefault(event.getId(), 0L),
                                confirmedRequestsByEvent.getOrDefault(event.getId(), 0L))
                )
                .collect(Collectors.toList());
    }

    public EventFullDto getEventById(HttpServletRequest httpServletRequest, Long eventId) {
        EndpointHit endpointHit = EndpointHit.builder()
                .app(EWM_MAIN_SERVICE_NAME)
                .ip(httpServletRequest.getRemoteAddr())
                .uri(httpServletRequest.getRequestURI())
                .build();
        StatisticRestClient.sendData(endpointHit);

        Event eventByUserAndId = eventRepository.findEventByIdAndEventStatus(eventId, EventStatus.PUBLISHED)
                .orElseThrow(
                        () -> new EntityNotFoundException(MessageFormat.format("Event with id={0} was not found", eventId))
                );
        Map<Long, Long> viewsByEvent = getViewsStatisticMap();
        Map<Long, Long> confirmedRequestsByEvent = getConfirmedRequestsMap();

        return EventMapper.eventToFullDto(eventByUserAndId,
                viewsByEvent.getOrDefault(eventId, 0L),
                confirmedRequestsByEvent.getOrDefault(eventId, 0L)
        );
    }

    public EventFullDto getUserEventById(Long userId, Long eventId) {
        User user = userService.findUser(userId);
        Event eventByUserAndId = findEventByUserAndId(eventId, user);

        Map<Long, Long> viewsByEvent = getViewsStatisticMap();
        Map<Long, Long> confirmedRequestsByEvent = getConfirmedRequestsMap();
        return EventMapper.eventToFullDto(eventByUserAndId,
                viewsByEvent.getOrDefault(eventId, 0L),
                confirmedRequestsByEvent.getOrDefault(eventId, 0L)
        );
    }

    public EventFullDto patchUserEventById(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {

        User user = userService.findUser(userId);
        Event eventForUpdate = findEventByUserAndId(eventId, user);
        if (!(eventForUpdate.getEventStatus() == EventStatus.PENDING ||
                eventForUpdate.getEventStatus() == EventStatus.CANCELED)) {
            throw new NotApplicableEvent("Only pending or canceled events can be changed");
        }

        if (eventForUpdate.getEventStatus() == EventStatus.PENDING &&
                updateEventUserRequest.getStateAction() == UpdateEventUserRequest.StateAction.CANCEL_REVIEW) {
            eventForUpdate.setEventStatus(EventStatus.CANCELED);
        }

        if (updateEventUserRequest.getEventDate() != null &&
                updateEventUserRequest.getEventDate().minusHours(2L).isBefore(LocalDateTime.now())
        ) {
            throw new EventNotValidArgumentException("Event should be announced 2 hours earlier then event");
        }

        if (eventForUpdate.getEventStatus() == EventStatus.CANCELED &&
                updateEventUserRequest.getStateAction() == UpdateEventUserRequest.StateAction.SEND_TO_REVIEW) {
            eventForUpdate.setEventStatus(EventStatus.PENDING);
        }

        Long eventCategoryId = updateEventUserRequest.getCategory();
        if (eventCategoryId != null) {
            EventCategory eventCategory = eventCategoryService.findEventCategory(eventCategoryId);
            eventForUpdate.setCategory(eventCategory);
        }

        if (updateEventUserRequest.getAnnotation() != null) {
            eventForUpdate.setAnnotation(updateEventUserRequest.getAnnotation());
        }
        if (updateEventUserRequest.getTitle() != null) {
            eventForUpdate.setTitle(updateEventUserRequest.getTitle());
        }
        if (updateEventUserRequest.getDescription() != null) {
            eventForUpdate.setDescription(updateEventUserRequest.getDescription());
        }
        if (updateEventUserRequest.getEventDate() != null) {
            eventForUpdate.setEventDate(updateEventUserRequest.getEventDate());
        }
        if (updateEventUserRequest.getLocation() != null) {
            eventForUpdate.setLocationLat(updateEventUserRequest.getLocation().getLat());
            eventForUpdate.setLocationLon(updateEventUserRequest.getLocation().getLon());
        }

        Event savedEvent = eventRepository.save(eventForUpdate);

        Map<Long, Long> viewsByEvent = getViewsStatisticMap();
        Map<Long, Long> confirmedRequestsByEvent = getConfirmedRequestsMap();
        return EventMapper.eventToFullDto(savedEvent,
                viewsByEvent.getOrDefault(eventId, 0L),
                confirmedRequestsByEvent.getOrDefault(eventId, 0L)
        );
    }

    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        User user = userService.findUser(userId);
        findEventByUserAndId(eventId, user);

        return requestRepository.findRequestByEventId(eventId).stream()
                .map(RequestMapper::requestToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    public EventRequestStatusUpdateResult patchRequests(final Long userId,
                                                        final Long eventId,
                                                        EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        RequestStatus newStatus;
        try {
            newStatus = RequestStatus.valueOf(eventRequestStatusUpdateRequest.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IncorrectStatusException("Incorrect request status");
        }

        User user = userService.findUser(userId);
        Event event = findEventByUserAndId(eventId, user);

        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            throw new ParticipantsLimitationException("This event's requests shouldn't be confirmed");
        }
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();

        final List<ParticipationRequestDto> participationRequestDtos;
        if (newStatus == RequestStatus.REJECTED) {
            participationRequestDtos = setNewStatusesForRequests(eventRequestStatusUpdateRequest, newStatus);
            eventRequestStatusUpdateResult.setRejectedRequests(participationRequestDtos);
            return eventRequestStatusUpdateResult;
        }

        if (newStatus == RequestStatus.CONFIRMED) {
            int confirmedRequestsCnt = requestRepository.countRequestByEventAndStatus(event, RequestStatus.CONFIRMED);
            int requestedConfirmsCnt = eventRequestStatusUpdateRequest.getRequestIds().size();
            if (confirmedRequestsCnt + requestedConfirmsCnt > event.getParticipantLimit()) {
                throw new ParticipantsLimitationException("The participant limit has been reached");
            }
            participationRequestDtos = setNewStatusesForRequests(eventRequestStatusUpdateRequest, newStatus);
            eventRequestStatusUpdateResult.setConfirmedRequests(participationRequestDtos);
            return eventRequestStatusUpdateResult;
        }

        return eventRequestStatusUpdateResult;
    }

    private List<ParticipationRequestDto> setNewStatusesForRequests(EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                                    RequestStatus newStatus) {
        List<ParticipationRequestDto> result = new ArrayList<>();
        eventRequestStatusUpdateRequest.getRequestIds().forEach(requestId -> {
            Request request = requestRepository.findById(requestId).orElseThrow(
                    () -> new EntityNotFoundException(
                            MessageFormat.format("Request with id={0} was not found", requestId)
                    )
            );
            if (request.getStatus() != RequestStatus.PENDING) {
                throw new IncorrectStatusException(
                        MessageFormat.format("Request with id={0} must have status PENDING", requestId)
                );
            }
            request.setStatus(newStatus);
            requestRepository.save(request);
            result.add(RequestMapper.requestToParticipationRequestDto(request));
        });
        return result;
    }

    public List<EventShortDto> getPublicEvents(HttpServletRequest httpServletRequest,
                                               Optional<Long> locationId,
                                               Optional<String> text,
                                               Optional<List<Integer>> categories,
                                               Optional<Boolean> paid,
                                               Optional<String> rangeStart,
                                               Optional<String> rangeEnd,
                                               boolean onlyAvailable,
                                               Optional<String> sort,
                                               Integer from,
                                               Integer size) {
        EndpointHit endpointHit = EndpointHit.builder()
                .app(EWM_MAIN_SERVICE_NAME)
                .ip(httpServletRequest.getRemoteAddr())
                .uri(httpServletRequest.getRequestURI())
                .build();
        StatisticRestClient.sendData(endpointHit);

        List<Event> events = getEvents(text, categories, paid, rangeStart, rangeEnd);

        if (locationId.isPresent()) {
            List<Event> eventsInLocation = eventRepository.getEventsInLocationByLocationId(locationId.get());
            events = events.stream()
                    .filter(eventsInLocation::contains)
                    .toList();
        }

        Map<Long, Long> viewsByEvent = getViewsStatisticMap();
        Map<Long, Long> confirmedRequestsByEvent = getConfirmedRequestsMap();

        List<EventShortDto> eventShortDtoList = events.stream()
                .filter(currentEvent -> {
                    if (onlyAvailable && currentEvent.getParticipantLimit() -
                            confirmedRequestsByEvent.getOrDefault(currentEvent.getId(), 0L) > 0) {
                        return true;
                    }
                    return true;
                })
                .map(eventEntity -> EventMapper.eventToShortDto(eventEntity,
                                viewsByEvent.getOrDefault(eventEntity.getId(), 0L),
                                confirmedRequestsByEvent.getOrDefault(eventEntity.getId(), 0L)
                        )
                )
                .collect(Collectors.toList());

        if (sort.isPresent()) {
            try {
                EventShortDtoSortBy eventShortDtoSortBy = EventShortDtoSortBy.valueOf(sort.get());
                eventShortDtoList = eventShortDtoList.stream().sorted(eventShortDtoSortBy.getComparator()).collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                throw new EventSortOrderNotValidException("Not valid event sorting order");
            }
        }

        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);

        return eventShortDtoList.subList((int) page.getOffset(),
                Math.min(page.getPageSize(), eventShortDtoList.size())
        );
    }

    private List<Event> getEvents(Optional<String> text,
                                  Optional<List<Integer>> categories,
                                  Optional<Boolean> paid,
                                  Optional<String> rangeStart,
                                  Optional<String> rangeEnd) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> event = query.from(Event.class);

        List<Predicate> predicates = new ArrayList<>();
        Predicate statusPublished = cb.equal(event.get("eventStatus"), EventStatus.PUBLISHED);
        predicates.add(statusPublished);

        categories.ifPresent(categoriesList -> {
            Predicate inCategoryPredicate = (event.get("category").get("id")).in(categoriesList);
            predicates.add(inCategoryPredicate);
        });

        paid.ifPresent(isPaid -> {
            Predicate isPaidPredicate = cb.equal(event.get("paid"), isPaid);
            predicates.add(isPaidPredicate);
        });

        text.ifPresent(txt -> {
            String pattern = "%" + txt.toLowerCase() + "%";
            Predicate descriptionLike = cb.like(cb.lower(event.get("description")), pattern);
            Predicate annotationLike = cb.like(cb.lower(event.get("annotation")), pattern);
            predicates.add(cb.or(descriptionLike, annotationLike));
        });

        LocalDateTime startDate = rangeStart.map(
                        s -> LocalDateTime.parse(s, DATE_TIME_FORMATTER)
                )
                .orElseGet(LocalDateTime::now);

        Predicate startFromPredicate = cb.greaterThan(event.get("eventDate"), startDate);
        predicates.add(startFromPredicate);

        rangeEnd.ifPresent(endTime -> {
            LocalDateTime endDate = LocalDateTime.parse(rangeEnd.get(), DATE_TIME_FORMATTER);
            if (!startDate.isBefore(endDate)) {
                throw new IncorrectDateException("End date should be after start date");
            }
            predicates.add(cb.lessThan(event.get("eventDate"), endDate));
        });

        query.where(predicates.toArray(new Predicate[0]));

        List<Event> eventsList = entityManager.createQuery(query).getResultList();
        return eventsList;
    }

    public Collection<EventFullDto> getAdminEvents(Optional<Long> locationId,
                                                   Optional<List<Integer>> users,
                                                   Optional<List<String>> states,
                                                   Optional<List<Integer>> categories,
                                                   Optional<String> rangeStart,
                                                   Optional<String> rangeEnd,
                                                   Integer from,
                                                   Integer size) {

        List<Event> events = getEventList(users, states, categories, rangeStart, rangeEnd);

        if (locationId.isPresent()) {
            List<Event> eventsInLocation = eventRepository.getEventsInLocationByLocationId(locationId.get());
            events = events.stream()
                    .filter(eventsInLocation::contains)
                    .toList();
        }

        Map<Long, Long> confirmedRequestsByEvent = getConfirmedRequestsMap();
        Map<Long, Long> viewsByEvent = getViewsStatisticMap();

        List<EventFullDto> eventFullDtoList = events.stream()
                .map(
                        eventEntity -> EventMapper.eventToFullDto(
                                eventEntity,
                                viewsByEvent.getOrDefault(eventEntity.getId(), 0L),
                                confirmedRequestsByEvent.getOrDefault(eventEntity.getId(), 0L)
                        )
                )
                .collect(Collectors.toList());

        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);
        return eventFullDtoList.subList((int) page.getOffset(),
                Math.min(page.getPageSize(), eventFullDtoList.size())
        );
    }

    private List<Event> getEventList(Optional<List<Integer>> users, Optional<List<String>> states, Optional<List<Integer>> categories, Optional<String> rangeStart, Optional<String> rangeEnd) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> event = query.from(Event.class);

        List<Predicate> predicates = new ArrayList<>();

        users.ifPresent(usersIds -> {
            Predicate inCategoryPredicate = (event.get("user").get("id")).in(usersIds);
            predicates.add(inCategoryPredicate);
        });

        states.ifPresent(statesIds -> {
                    List<EventStatus> eventStatuses = statesIds.stream()
                            .map(EventStatus::valueOf)
                            .collect(Collectors.toList());
                    Predicate statusesPredicate = event.get("eventStatus").in(eventStatuses);
                    predicates.add(statusesPredicate);
                }
        );

        categories.ifPresent(categoriesList -> {
            Predicate inCategoryPredicate = (event.get("category").get("id")).in(categoriesList);
            predicates.add(inCategoryPredicate);
        });

        Predicate startFromPredicate = cb.greaterThan(event.get("eventDate"),
                rangeStart.isEmpty() ? LocalDateTime.now() : LocalDateTime.parse(rangeStart.get(), DATE_TIME_FORMATTER));
        predicates.add(startFromPredicate);

        rangeEnd.ifPresent(endTime -> {
            predicates.add(cb.lessThan(event.get("eventDate"), LocalDateTime.parse(rangeEnd.get(), DATE_TIME_FORMATTER)));
        });

        query.where(predicates.toArray(new Predicate[0]));

        List<Event> eventsList = entityManager.createQuery(query).getResultList();
        return eventsList;
    }

    public EventFullDto patchAdminEventById(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event eventForUpdate = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(MessageFormat.format("Event with id={0} was not found", eventId))
        );

        if (updateEventAdminRequest.getEventDate() != null &&
                updateEventAdminRequest.getEventDate().minusHours(1L).isBefore(LocalDateTime.now())
        ) {
            throw new EventNotValidArgumentException("Event should be announced 1 hours earlier then event");
        }

        if (eventForUpdate.getEventStatus() == EventStatus.PUBLISHED &&
                updateEventAdminRequest.getStateAction() == UpdateEventAdminRequest.StateAction.PUBLISH_EVENT
        ) {
            throw new NotValidRequestException("Event has already published");
        }

        if (eventForUpdate.getEventStatus() == EventStatus.CANCELED &&
                updateEventAdminRequest.getStateAction() == UpdateEventAdminRequest.StateAction.PUBLISH_EVENT
        ) {
            throw new NotValidRequestException("Event has already been canceled");
        }

        if (eventForUpdate.getEventStatus() == EventStatus.PUBLISHED &&
                updateEventAdminRequest.getStateAction() == UpdateEventAdminRequest.StateAction.REJECT_EVENT
        ) {
            throw new NotValidRequestException("Event has already published");
        }

        if (eventForUpdate.getEventStatus() == EventStatus.PENDING &&
                updateEventAdminRequest.getStateAction() == UpdateEventAdminRequest.StateAction.PUBLISH_EVENT) {
            eventForUpdate.setEventStatus(EventStatus.PUBLISHED);
        }

        if (eventForUpdate.getEventStatus() != EventStatus.PUBLISHED &&
                updateEventAdminRequest.getStateAction() == UpdateEventAdminRequest.StateAction.REJECT_EVENT) {
            eventForUpdate.setEventStatus(EventStatus.CANCELED);
        }

        Long eventCategoryId = updateEventAdminRequest.getCategory();
        if (eventCategoryId != null) {
            EventCategory eventCategory = eventCategoryService.findEventCategory(eventCategoryId);
            eventForUpdate.setCategory(eventCategory);
        }
        if (updateEventAdminRequest.getAnnotation() != null) {
            eventForUpdate.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getTitle() != null) {
            eventForUpdate.setTitle(updateEventAdminRequest.getTitle());
        }
        if (updateEventAdminRequest.getDescription() != null) {
            eventForUpdate.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            eventForUpdate.setEventDate(updateEventAdminRequest.getEventDate());
        }
        if (updateEventAdminRequest.getLocation() != null) {
            eventForUpdate.setLocationLat(updateEventAdminRequest.getLocation().getLat());
            eventForUpdate.setLocationLon(updateEventAdminRequest.getLocation().getLon());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            eventForUpdate.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            eventForUpdate.setPaid(updateEventAdminRequest.getPaid());
        }

        Event savedEvent = eventRepository.save(eventForUpdate);
        return EventMapper.eventToFullDto(savedEvent, 0L, 0L);
    }

    private Map<Long, Long> getConfirmedRequestsMap() {
        Map<Long, Long> confirmedRequestsByEvent = requestRepository.countRequestByEventId(RequestStatus.CONFIRMED)
                .stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(RequestsCountByEvent::getEventId, RequestsCountByEvent::getCount),
                        resultMap -> resultMap.isEmpty() ? Map.of(0L, 0L) : resultMap)
                );
        return confirmedRequestsByEvent;
    }

    private static Map<Long, Long> getViewsStatisticMap() {
        List<ViewStats> viewStats = StatisticRestClient.getData(
                LocalDateTime.of(0, 1, 1, 0, 0).format(DATE_TIME_FORMATTER),
                LocalDateTime.of(5000, 1, 1, 0, 0).format(DATE_TIME_FORMATTER),
                null,
                String.valueOf(true));

        return viewStats.stream()
                .filter(viewStatsLine -> viewStatsLine.getApp().equals(EWM_MAIN_SERVICE_NAME))
                .filter(viewStats1 -> viewStats1.getUri().startsWith("/events/"))
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(viewStatsLine -> {
                            int lastSlashIndex = viewStatsLine.getUri().lastIndexOf('/');
                            return Long.valueOf(viewStatsLine.getUri().substring(lastSlashIndex + 1));
                        }, ViewStats::getHits),
                        resultMap -> resultMap.isEmpty() ? Map.of(0L, 0L) : resultMap)
                );
    }

    private Event findEventByUserAndId(Long eventId, User user) {
        return eventRepository.findEventByUserAndId(user, eventId).orElseThrow(
                () -> new EntityNotFoundException(MessageFormat.format("Event with id={0} was not found", eventId))
        );
    }

}
