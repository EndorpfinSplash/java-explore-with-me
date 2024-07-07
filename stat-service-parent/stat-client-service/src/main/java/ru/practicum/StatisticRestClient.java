package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.commons.EventCreationDto;
import ru.practicum.commons.EventOutDto;

@Slf4j
public class StatisticRestClient {

    private static final String RESOURCE_PATH = "/hit";

    @Value("${server.port}")
    private int port;

    @Value("${host}")
    private String host;

    private String REQUEST_URI;
    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<EventOutDto> sendData(EventCreationDto eventCreationDto) {
        this.REQUEST_URI = host + ":" + port + RESOURCE_PATH;
        return restTemplate.postForEntity(REQUEST_URI,
                eventCreationDto,
                EventOutDto.class);
    }

}