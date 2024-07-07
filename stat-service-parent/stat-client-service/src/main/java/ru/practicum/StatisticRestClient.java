package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.commons.EventCreationDto;
import ru.practicum.commons.EventOutDto;
import ru.practicum.commons.EventStatisticOutDto;

import java.util.List;

@Slf4j
public class StatisticRestClient {

    private static final String RESOURCE_PATH_TO_SAVE_EVENT = "/hit";
    private static final String RESOURCE_PATH_TO_GET_STATISTIC = "/stats";

    @Value("${server.port}")
    private int port;

    @Value("${host}")
    private String host;

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<EventOutDto> sendData(EventCreationDto eventCreationDto) {
        String requestUri = host + ":" + port + RESOURCE_PATH_TO_SAVE_EVENT;
        return restTemplate.postForEntity(requestUri,
                eventCreationDto,
                EventOutDto.class);
    }

    public ResponseEntity<List<EventStatisticOutDto>> getData() {
        String requestUri = host + ":" + port + RESOURCE_PATH_TO_GET_STATISTIC;
        return null;
    }

}