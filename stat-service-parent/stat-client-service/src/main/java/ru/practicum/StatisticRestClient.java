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
    private static int port;

    @Value("${host}")
    private static String host;

    private static final RestTemplate restTemplate = new RestTemplate();

    public static EventOutDto sendData(EndpointHit endpointHit) {
        String requestUri = host + ":" + port + RESOURCE_PATH_TO_SAVE_EVENT;
        return restTemplate.postForEntity(requestUri,
                        endpointHit,
                        EventOutDto.class)
                .getBody();
    }

    public static List<ViewStats> getData(String start, String end, List<String> uris, String unique) {
        String requestUri = host + ":" + port + RESOURCE_PATH_TO_GET_STATISTIC;

        Map<String, Object> urlParameters = new HashMap<>();
        urlParameters.put("start", start);
        urlParameters.put("end", end);
        if(uris != null && !uris.isEmpty()) {
            urlParameters.put("uris", uris);
        }
        if(unique != null && !unique.isEmpty()) {
            urlParameters.put("unique", unique);
        }

        ResponseEntity<ViewStats[]> events = restTemplate.getForEntity(
                requestUri,
                ViewStats[].class,
                urlParameters);
        return events.getBody() != null ? Arrays.asList(events.getBody()) :
                Collections.emptyList();
    }

}