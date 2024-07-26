package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.commons.EndpointHit;
import ru.practicum.commons.EventOutDto;
import ru.practicum.commons.ViewStats;

import java.util.*;

@Slf4j
public class StatisticRestClient {

    private static final String RESOURCE_PATH_TO_SAVE_EVENT = "/hit";
    private static final String RESOURCE_PATH_TO_GET_STATISTIC = "/stats";

    @Value("${server.port}")
    private int port;

    @Value("${host}")
    private String host;

    private final RestTemplate restTemplate = new RestTemplate();

    public EventOutDto sendData(EndpointHit endpointHit) {
        String requestUri = host + ":" + port + RESOURCE_PATH_TO_SAVE_EVENT;
        return restTemplate.postForEntity(requestUri,
                        endpointHit,
                        EventOutDto.class)
                .getBody();
    }

    public List<ViewStats> getData(String start, String end, String uris, String unique) {
        String requestUri = host + ":" + port + RESOURCE_PATH_TO_GET_STATISTIC;

        Map<String, String> urlParameters = new HashMap<>();
        urlParameters.put("start", start);
        urlParameters.put("end", end);
        urlParameters.put("uris", uris);
        urlParameters.put("unique", unique);

        ResponseEntity<ViewStats[]> events = restTemplate.getForEntity(requestUri,
                ViewStats[].class,
                urlParameters);
        return events.getBody() != null ? Arrays.asList(events.getBody()) :
                Collections.emptyList();
    }

}