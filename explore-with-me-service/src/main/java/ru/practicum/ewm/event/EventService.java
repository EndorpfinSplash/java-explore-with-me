package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.dto.EventCreationDto;
import ru.practicum.ewm.event.dto.EventOutDto;
import ru.practicum.ewm.event.dto.EventUpdateDto;
import ru.practicum.ewm.event_category.EventCategory;
import ru.practicum.ewm.event_category.EventCategoryRepository;
import ru.practicum.ewm.exception.*;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventCategoryRepository eventCategoryRepository;

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
        Event eventByUserAndId = eventRepository.findEventByUserAndId(user, Long.valueOf(eventId))
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
}
