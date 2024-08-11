package ru.practicum.ewm.compilation;

import ru.practicum.StatisticRestClient;
import ru.practicum.commons.ViewStats;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.ewm.event.EventService.DATE_TIME_FORMATTER;
import static ru.practicum.ewm.event.EventService.EWM_MAIN_SERVICE_NAME;

public class CompilationMapper {
    public static Compilation newCompilationToCompilation(NewCompilationDto newCompilationDto, Set<Event> events) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned())
                .events(events)
                .build();
    }

    public static CompilationDto compilationToCompilationDto(Compilation compilation, Map<Long, Long> confirmedRequestsByEvent) {
        List<ViewStats> viewStats = StatisticRestClient.getData(
                LocalDateTime.of(0, 1, 1, 0, 0).format(DATE_TIME_FORMATTER),
                LocalDateTime.of(5000, 1, 1, 0, 0).format(DATE_TIME_FORMATTER),
                null,
                null);

        Map<Long, Long> viewsByEvent = viewStats.stream().filter(viewStatsLine -> viewStatsLine.getApp().equals(EWM_MAIN_SERVICE_NAME))
                .filter(viewStats1 -> viewStats1.getUri().startsWith("/events/"))
                .collect(Collectors.toMap(viewStatsLine -> {
                                    int lastSlashIndex = viewStatsLine.getUri().lastIndexOf('/');
                                    Long eventId = Long.valueOf(viewStatsLine.getUri().substring(lastSlashIndex + 1));
                                    return eventId;
                                },
                                ViewStats::getHits)
                );
        List<EventShortDto> events = compilation.getEvents().stream()
                .map(event -> EventMapper.eventToShortDto(
                        event,
                        viewsByEvent.getOrDefault(event.getId(), 0L),
                        confirmedRequestsByEvent.getOrDefault(event.getId(), 0L))
                )
                .collect(Collectors.toList());

        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned()!=null ? compilation.getPinned() : false)
                .events(events)
                .build();
    }
}
