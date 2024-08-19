package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.commons.EndpointHit;
import ru.practicum.commons.EventOutDto;
import ru.practicum.commons.ViewStats;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class StatisticRestClient {

    private static final String RESOURCE_PATH_TO_SAVE_EVENT = "/hit";
    private static final String RESOURCE_PATH_TO_GET_STATISTIC = "/stats";
    private static String host;
    private static int port;

    @Value("${server.port}")
    public void setPort(int port) {
        StatisticRestClient.port = port;
    }

    @Value("${host}")
    public void setHost(String host) {
        StatisticRestClient.host = host;
    }

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

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> entity = new HttpEntity<>(headers);


        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(requestUri)
                .queryParam("start", start)
                .queryParam("end", end);

        if (uris != null && !uris.isEmpty()) {
            uriComponentsBuilder.queryParam("uris", String.join(",", uris));
        }
        if (unique != null && !unique.isEmpty()) {
            uriComponentsBuilder.queryParam("unique", unique);
        }

        String urlTemplate = uriComponentsBuilder.encode().toUriString();

        HttpEntity<ViewStats[]> response = restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                entity,
                ViewStats[].class
        );

        return response.getBody() != null ? List.of(response.getBody()) :
                Collections.emptyList();
    }

}