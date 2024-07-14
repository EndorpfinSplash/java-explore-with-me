package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.dto.EventCreationDto;
import ru.practicum.ewm.event.dto.EventOutDto;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public EventOutDto createEvent(String userId, EventCreationDto eventCreationDto) {
        return new EventOutDto();
    }


}
