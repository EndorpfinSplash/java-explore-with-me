package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.commons.EndpointHit;
import ru.practicum.commons.EventOutDto;
import ru.practicum.commons.ViewStats;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class StatisticRestClient {

    private static final String RESOURCE_PATH_TO_SAVE_EVENT = "/hit";
    private static final String RESOURCE_PATH_TO_GET_STATISTIC = "/stats";


    private static int port = 9090;
//    @Value("${server.port}")
//    public void setPort(int port) {
//        StatisticRestClient.port = port;
//    }
//
//    public static int getPort() {
//        return port;
//    }

    private static String host = "localhost";
//    @Value("${host}")
//    public void setHost(String host) {
//        StatisticRestClient.host = host;
//    }
//
//    public static String getHost() {
//        return host;
//    }

    private static final RestTemplate restTemplate = new RestTemplate();

    public static EventOutDto sendData(EndpointHit endpointHit) {
        String requestUri = "http://" + host + ":" + port + RESOURCE_PATH_TO_SAVE_EVENT;
        return restTemplate.postForEntity(requestUri,
                        endpointHit,
                        EventOutDto.class)
                .getBody();
    }

    public static List<ViewStats> getData(String start, String end, List<String> uris, String unique) {
        String requestUri = "http://" + host + ":" + port + RESOURCE_PATH_TO_GET_STATISTIC;

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("start", start);
        uriVariables.put("end", end);
        if(uris != null && !uris.isEmpty()) {
            uriVariables.put("uris", String.join(",", uris));
        }
        if(unique != null && !unique.isEmpty()) {
            uriVariables.put("unique", unique);
        }

        ResponseEntity<ViewStats[]> events = restTemplate.getForEntity(
                requestUri,
                ViewStats[].class,
                uriVariables);

        return events.getBody() != null ? List.of(events.getBody()) :
                Collections.emptyList();
    }

}