package ru.practicum.ewm.location;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.location.dto.LocationFullDto;
import ru.practicum.ewm.location.dto.NewLocationDto;
import ru.practicum.ewm.location.dto.UpdateLocationDto;

import java.util.List;

@RestController
@RequestMapping("/admin/locations")
@RequiredArgsConstructor
@Slf4j
public class LocationControllerAdmin {

    private final LocationService locationService;

    @GetMapping
    public List<LocationFullDto> getAdminAllLocations() {
        log.info("GET request to fetch all locations received");
        List<LocationFullDto> adminAllLocations = locationService.getAdminAllLocations();
        log.info("{} locations were received.", adminAllLocations.size());
        return adminAllLocations;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationFullDto createLocation(@RequestBody @Valid final NewLocationDto newLocationDto) {
        log.info("POST request received to create new location {}", newLocationDto);
        LocationFullDto locationSaved = locationService.createLocation(newLocationDto);
        log.info("Received location saved {}", locationSaved);
        return locationSaved;
    }

    @GetMapping("/{id}")
    public LocationFullDto getLocationById(@PathVariable final Long id) {
        log.info("GET request received to fetch location_id {}", id);
        LocationFullDto locationFullDto = locationService.findAdminLocationById(id);
        log.info("Location {} received", locationFullDto);
        return locationFullDto;
    }

    @PatchMapping("/{id}")
    public LocationFullDto update(@PathVariable final Long id,
                                  @RequestBody @Valid final UpdateLocationDto updateLocationDto) {
        log.info("PUT request received to update location_id {} by {}", id, updateLocationDto);
        LocationFullDto updatedLocation = locationService.updateLocation(id, updateLocationDto);
        log.info("Location {} updated", updatedLocation);
        return updatedLocation;

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final Long id) {
        log.info("DELETE request to remove Location_id {} received", id);
        locationService.delete(id);
    }

}

