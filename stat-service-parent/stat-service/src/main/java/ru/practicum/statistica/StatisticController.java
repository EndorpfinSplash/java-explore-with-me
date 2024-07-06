package ru.practicum.statistica;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.commons.EventCreationDto;
import ru.practicum.commons.EventOutDto;
import ru.practicum.commons.EventStatisticByEventDto;

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
        log.info("Creating event {}", eventCreationDto);
        return statisticService.save(eventCreationDto);
    }

    @GetMapping("/stats")
    public List<EventStatisticByEventDto> getStats(

            @RequestParam(name = "start")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime start,

            @RequestParam(name = "end")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime end,
            @RequestParam(name = "uris", required = false) List<String> uris,
            @RequestParam(name = "unique", defaultValue = "false") boolean unique
    ) {
        log.info("Get events from {} to end {} for list of {} with unique flag {}",
                start, end, Collections.singletonList(uris), unique);
        return statisticService.getEventStatistic(start, end, uris, unique);
    }

}
