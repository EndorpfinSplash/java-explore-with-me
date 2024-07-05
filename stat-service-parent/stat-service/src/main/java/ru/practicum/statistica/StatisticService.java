package ru.practicum.statistica;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.commons.EventCreationDto;
import ru.practicum.commons.EventOutDto;
import ru.practicum.commons.EventStatisticByEventDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final StatisticRepository statisticRepository;


    public EventOutDto save(EventCreationDto eventCreationDto) {
        Event eventForSave = EventMapper.eventCreationDtoToEvent(eventCreationDto);
        statisticRepository.save(eventForSave);
        return EventMapper.eventToEventOutDto(eventForSave);
    }



    public List<EventStatisticByEventDto> getEventStatistic(LocalDateTime start,
                                                            LocalDateTime end,
                                                            List<String> uris,
                                                            boolean unique) {
        return  statisticRepository.countEvents(start, end, uris);
    }
}
