package ru.practicum.ewm.location;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.location.dto.LocationFullDto;
import ru.practicum.ewm.location.dto.LocationShortDto;
import ru.practicum.ewm.location.dto.NewLocationDto;
import ru.practicum.ewm.location.dto.UpdateLocationDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.ewm.event.EventService.getViewsStatisticMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationService {
    private final LocationRepository locationRepository;
    private final EventService eventService;

    public LocationFullDto createLocation(NewLocationDto newLocationDto) {
        Location locationForSave = LocationMapper.newLocationToEntity(newLocationDto);
        Location savedLocation = locationRepository.save(locationForSave);
        return LocationMapper.entityToLocationFullDto(savedLocation);
    }

    public LocationFullDto findAdminLocationById(Long locationId) {
        Optional<Location> optionalLocation = locationRepository.findById(locationId);
        return LocationMapper.entityToLocationFullDto(
                optionalLocation.orElseThrow(() -> new EntityNotFoundException("Location not found"))
        );
    }

    public LocationShortDto findPublicLocationById(Long locationId) {
        Optional<Location> locationOptional = locationRepository.findById(locationId);
        return LocationMapper.entityToShortLocationDto(
                locationOptional.orElseThrow(() -> new EntityNotFoundException("Location not found"))
        );
    }

    public LocationFullDto updateLocation(Long locationIdForUpdate, UpdateLocationDto updateLocationDto) {
        Location existingLocation = getLocationById(locationIdForUpdate);

        String locationDtoName = updateLocationDto.getName();
        if (locationDtoName != null) {
            existingLocation.setName(locationDtoName);
        }

        String locationDtoDescription = updateLocationDto.getDescription();
        if (locationDtoDescription != null) {
            existingLocation.setDescription(locationDtoDescription);
        }

        Double locationLat = updateLocationDto.getLocationLat();
        if (locationLat != null) {
            existingLocation.setLocationLat(locationLat);
        }

        Double locationLon = updateLocationDto.getLocationLon();
        if (locationLon != null) {
            existingLocation.setLocationLon(locationLon);
        }

        Double locationRadius = updateLocationDto.getRadius();
        if (locationRadius != null) {
            existingLocation.setRadius(locationRadius);
        }

        Location savedLocation = locationRepository.save(existingLocation);
        return LocationMapper.entityToLocationFullDto(savedLocation);
    }

    public void delete(Long id) {
        getLocationById(id);
        locationRepository.deleteById(id);
    }


    private Location getLocationById(Long id) {
        return locationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Location not found")
        );
    }

    public List<EventShortDto> findPublicLocationEventsById(Long id) {
        getLocationById(id);

        locationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Location not found")
        );
        List<Event> locationEventsById = eventRepository.getLocationEventsById(id);
        Map<Long, Long> viewsByEvent = getViewsStatisticMap();
//        Map<Long, Long> confirmedRequestsByEvent = getConfirmedRequestsMap();

        List<EventShortDto> eventShortDtoList = locationEventsById.stream()
                .map(eventEntity -> EventMapper.eventToShortDto(eventEntity,
                                viewsByEvent.getOrDefault(eventEntity.getId(), 0L),
//                                confirmedRequestsByEvent.getOrDefault(eventEntity.getId(), 0L)
                                0L
                        )
                )
                .collect(Collectors.toList());

//        eventService.getPublicEvents()
        return null;
    }



    public List<LocationFullDto> getAdminAllLocations() {
        return locationRepository.findAll().stream()
                .map(LocationMapper::entityToLocationFullDto)
                .collect(Collectors.toList());
    }

    public List<LocationShortDto> getPublicAllLocations() {
        return locationRepository.findAll().stream()
                .map(LocationMapper::entityToShortLocationDto)
                .collect(Collectors.toList());
    }
}
