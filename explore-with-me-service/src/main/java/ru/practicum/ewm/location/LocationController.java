package ru.practicum.ewm.location;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.exception.EntityNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
@Slf4j
public class LocationController {

    private LocationRepository locationRepository;

    @GetMapping
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @PostMapping
    public void createLocation(@RequestBody Location location) {
        locationRepository.save(location);
    }

    @GetMapping("/{id}")
    public Location getLocationById(@PathVariable Long id) {
        return locationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Location not found")
        );
    }

    @PutMapping("/{id}")
    public void updateLocation(@PathVariable Long id, @RequestBody Location location) {
        Location existingLocation = locationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Location not found"));
//        BeanUtils.copyProperties(location, existingLocation);
        locationRepository.save(existingLocation);
    }

    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable Long id) {
        Location location = locationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Location not found"));
        locationRepository.delete(location);
    }
}

