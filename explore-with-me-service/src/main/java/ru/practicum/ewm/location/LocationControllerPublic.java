package ru.practicum.ewm.location;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.location.dto.LocationShortDto;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
@Slf4j
public class LocationControllerPublic {

    private final LocationService locationService;

    @GetMapping
    public List<LocationShortDto> getAllLocations() {
        log.info("GET request to fetch all locations received");
        List<LocationShortDto> publicAllLocations = locationService.getPublicAllLocations();
        log.info("{} locations were received.", publicAllLocations.size());
        return publicAllLocations;
    }


    @GetMapping("/{id}")
    public LocationShortDto getPublicLocationById(@PathVariable Long id) {
        log.info("GET request to fetch location received");
        LocationShortDto publicLocationById = locationService.findPublicLocationById(id);
        log.info("{} location were received.", publicLocationById);
        return publicLocationById;
    }

}

