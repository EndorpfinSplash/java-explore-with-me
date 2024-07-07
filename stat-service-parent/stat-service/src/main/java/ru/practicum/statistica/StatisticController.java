package ru.practicum.statistica;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.commons.EventCreationDto;
import ru.practicum.commons.EventOutDto;
import ru.practicum.commons.EventStatisticOutDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatisticController {

    private final StatisticService statisticService;


    @PostMapping("/hit")
    public EventOutDto hit(
            @RequestBody @Valid EventCreationDto eventCreationDto) {
        log.info("Collect data about event {}", eventCreationDto);
        EventOutDto savedEvent = statisticService.save(eventCreationDto);
        log.info("Event {} was saved", savedEvent);
        return savedEvent;
    }

    @GetMapping("/stats")
    public List<EventStatisticOutDto> getStats(

            @RequestParam(name = "start")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime start,

            @RequestParam(name = "end")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(name = "uris", required = false) List<String> uris,
            @RequestParam(name = "unique", defaultValue = "false") boolean unique
    ) {
        log.info("Get events from {} to end {} for list of URIs[{}] with unique flag {}",
                start, end, Collections.singletonList(uris), unique);
        List<EventStatisticOutDto> eventStatistic = statisticService.getEventStatistic(start, end, uris, unique);
        log.info("Event statistic calculated: {} ", eventStatistic);
        return eventStatistic;
    }

}
