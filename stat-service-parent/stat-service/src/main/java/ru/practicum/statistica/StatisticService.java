package ru.practicum.statistica;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.commons.EndpointHit;
import ru.practicum.commons.EventOutDto;
import ru.practicum.commons.ViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public List<ViewStats> getEventStatistic(String  startStr,
                                             String  endStr,
                                             List<String> uris,
                                             boolean unique) {
        LocalDateTime start = LocalDateTime.parse(startStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime end = LocalDateTime.parse(endStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

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
