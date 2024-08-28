package ru.practicum.ewm.location;

import ru.practicum.ewm.location.dto.LocationFullDto;
import ru.practicum.ewm.location.dto.LocationShortDto;
import ru.practicum.ewm.location.dto.NewLocationDto;

public class LocationMapper {
    public static Location newLocationToEntity(NewLocationDto newLocationDto) {
        return Location.builder()
                .name(newLocationDto.getName())
                .description(newLocationDto.getDescription())
                .locationLat(newLocationDto.getLocationLat())
                .locationLon(newLocationDto.getLocationLon())
                .radius(newLocationDto.getRadius())
                .build();
    }

    public static LocationFullDto entityToLocationFullDto(Location savedLocation) {
        return LocationFullDto.builder()
                .id(savedLocation.getId())
                .name(savedLocation.getName())
                .description(savedLocation.getDescription())
                .locationLat(savedLocation.getLocationLat())
                .locationLon(savedLocation.getLocationLon())
                .radius(savedLocation.getRadius())
                .createdOn(savedLocation.getCreatedOn())
                .build();
    }

    public static LocationShortDto entityToShortLocationDto(Location location) {
        return LocationShortDto.builder()
                .name(location.getName())
                .description(location.getDescription())
                .locationLat(location.getLocationLat())
                .locationLon(location.getLocationLon())
                .radius(location.getRadius())
                .build();
    }
}
