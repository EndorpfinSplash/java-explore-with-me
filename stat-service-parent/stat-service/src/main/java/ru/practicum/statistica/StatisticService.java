package ru.practicum.statistica;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.commons.EndpointHit;
import ru.practicum.commons.EventOutDto;
import ru.practicum.commons.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final StatisticRepository statisticRepository;


    public EventOutDto save(EndpointHit endpointHit) {
        Event eventForSave = EventMapper.eventCreationDtoToEvent(endpointHit);
        Event savedEvent = statisticRepository.save(eventForSave);
        return EventMapper.eventToEventOutDto(savedEvent);
    }

    public List<ViewStats> getEventStatistic(LocalDateTime start,
                                             LocalDateTime end,
                                             List<String> uris,
                                             boolean unique) {
        if ((uris == null || uris.isEmpty()) && !unique) {
            return statisticRepository.countEvents(start, end);
        }

        if (uris == null || uris.isEmpty()) {
            return statisticRepository.countUniqIpForEvents(start, end);
        }

        if (unique) {
            return statisticRepository.countUniqIpForEvents(start, end, uris);
        }

        return statisticRepository.countEvents(start, end, uris);
    }
}
