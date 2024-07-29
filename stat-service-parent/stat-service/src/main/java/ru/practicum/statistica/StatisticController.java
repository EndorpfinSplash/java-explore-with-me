package ru.practicum.statistica;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.commons.EndpointHit;
import ru.practicum.commons.EventOutDto;
import ru.practicum.commons.ViewStats;

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
    @ResponseStatus(HttpStatus.CREATED)
    public EventOutDto hit(
            @RequestBody @Valid EndpointHit endpointHit) {
        log.info("Collect data about event {}", endpointHit);
        EventOutDto savedEvent = statisticService.save(endpointHit);
        log.info("Data about request to end-point {} was saved", savedEvent);
        return savedEvent;
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(

            @RequestParam(value = "start")
//            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            String  start,

            @RequestParam(value = "end")
//            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            String end,
            @RequestParam(value = "uris", required = false) List<String> uris,
            @RequestParam(value = "unique", required = false, defaultValue = "false") boolean unique
    ) {
        log.info("Get events from {} to end {} for list of URIs[{}] with unique flag {}",
                start, end, Collections.singletonList(uris), unique);
        List<ViewStats> eventStatistic = statisticService.getEventStatistic(start, end, uris, unique);
        log.info("Event statistic calculated: {} ", eventStatistic);
        return eventStatistic;
    }

}
