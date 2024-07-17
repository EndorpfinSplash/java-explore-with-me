package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.dto.EventCreationDto;
import ru.practicum.ewm.event.dto.EventOutDto;
import ru.practicum.ewm.event_category.EventCategory;
import ru.practicum.ewm.event_category.EventCategoryRepository;
import ru.practicum.ewm.exception.EventCategoryNotFoundException;
import ru.practicum.ewm.exception.EventNotValidArgumentException;
import ru.practicum.ewm.exception.UserNotFoundException;
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

    public EventOutDto createEvent(Integer userId, EventCreationDto eventCreationDto) {
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


    public Collection<EventOutDto> getUserEvents(Integer userId, Integer from, Integer size) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(MessageFormat.format("User with userId={0} not found", userId))
        );
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);
        return eventRepository.findAllByUserIdOrderById(userId,page)
                .stream()
                .map(event -> EventMapper.eventToOutDto(event, 0L, 0L))
                .collect(Collectors.toList());
    }
}
