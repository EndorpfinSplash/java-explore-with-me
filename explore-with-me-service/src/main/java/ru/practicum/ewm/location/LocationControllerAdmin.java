package ru.practicum.ewm.location;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.location.dto.LocationFullDto;
import ru.practicum.ewm.location.dto.NewLocationDto;
import ru.practicum.ewm.location.dto.UpdateLocationDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.ewm.event.EventService.getViewsStatisticMap;

@RestController
@RequestMapping("/admin/locations")
@RequiredArgsConstructor
@Slf4j
public class LocationControllerAdmin {

    private final LocationService locationService;
    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;

    @GetMapping
    public List<LocationFullDto> getAdminAllLocations() {
        log.info("GET request to fetch all locations received");
        List<LocationFullDto> adminAllLocations = locationService.getAdminAllLocations();
        log.info("{} locations were received.", adminAllLocations.size());
        return adminAllLocations;
    }

    @PostMapping
    public LocationFullDto createLocation(@RequestBody @Valid NewLocationDto newLocationDto) {
        log.info("POST request received to create new location {}", newLocationDto);
        LocationFullDto locationSaved = locationService.createLocation(newLocationDto);
        log.info("Received location saved {}", locationSaved);
        return locationSaved;
    }

    @GetMapping("/{id}")
    public LocationFullDto getLocationById(@PathVariable Long id) {
        log.info("GET request received to fetch location_id {}", id);
        LocationFullDto locationFullDto = locationService.findAdminLocationById(id);
        log.info("Location {} received", locationFullDto);
        return locationFullDto;
    }

    @PatchMapping("/{id}")
    public LocationFullDto update(@PathVariable Long id, @RequestBody UpdateLocationDto updateLocationDto) {
        log.info("PUT request received to update location_id {} by {}", id, updateLocationDto);
        LocationFullDto updatedLocation = locationService.updateLocation(id, updateLocationDto);
        log.info("Location {} updated", updatedLocation);
        return updatedLocation;

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("DELETE request to remove Location_id {} received", id);
        locationService.delete(id);
    }


    @GetMapping("/{id}/events")
    public List<EventFullDto> getAdminLocationEventsById(@PathVariable Long id) {
        locationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Location not found")
        );
        List<Event> allLocationEventsById = eventRepository.getAllLocationEventsById(id);
        Map<Long, Long> viewsByEvent = getViewsStatisticMap();
        return allLocationEventsById.stream()
                .map(
                        event -> EventMapper.eventToFullDto(event, viewsByEvent.getOrDefault(event.getId(), 0L),
                                0L)
                )
                .collect(Collectors.toList());
    }

}

