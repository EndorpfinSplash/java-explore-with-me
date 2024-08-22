package ru.practicum.ewm.event;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event_category.EventCategory;
import ru.practicum.ewm.event_category.Location;
import ru.practicum.ewm.event_category.dto.CategoryDto;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.dto.UserShortDto;

public class EventMapper {

    public static Event creationDtoToEvent(NewEventDto newEventDto,
                                           User user,
                                           EventCategory eventCategory
    ) {
        return Event.builder()
                .user(user)
                .annotation(newEventDto.getAnnotation())
                .category(eventCategory)
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .locationLon(newEventDto.getLocation().getLon())
                .locationLat(newEventDto.getLocation().getLat())
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.isRequestModeration())
                .title(newEventDto.getTitle())
                .eventStatus(EventStatus.PENDING)
                .build();
    }


    public static EventFullDto eventToFullDto(Event savedEvent, long views, long confirmedRequests) {
        return EventFullDto.builder()
                .id(savedEvent.getId())
                .annotation(savedEvent.getAnnotation())
                .category(CategoryDto.builder()
                        .id(savedEvent.getCategory().getId())
                        .name(savedEvent.getCategory().getName())
                        .build())
                .confirmedRequests(confirmedRequests)
                .createdOn(savedEvent.getCreatedOn())
                .description(savedEvent.getDescription())
                .eventDate(savedEvent.getEventDate())
                .initiator(UserShortDto.builder()
                        .id(savedEvent.getUser().getId())
                        .email(savedEvent.getUser().getEmail())
                        .build())
                .location(Location.builder()
                        .lat(savedEvent.getLocationLat())
                        .lon(savedEvent.getLocationLon())
                        .build())
                .paid(savedEvent.isPaid())
                .participantLimit(savedEvent.getParticipantLimit())
                .publishedOn(savedEvent.getPublishedOn())
                .requestModeration(savedEvent.isRequestModeration())
                .state(savedEvent.getEventStatus().name())
                .title(savedEvent.getTitle())
                .views(views)
                .build();
    }

    public static EventShortDto eventToShortDto(Event event, Long views, Long confirmedRequests) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryDto.builder()
                        .id(event.getCategory().getId())
                        .name(event.getCategory().getName())
                        .build())
                .confirmedRequests(confirmedRequests)
                .eventDate(event.getEventDate())
                .initiator(UserShortDto.builder()
                        .id(event.getUser().getId())
                        .email(event.getUser().getEmail())
                        .build())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .title(event.getTitle())
                .views(views)
                .build();
    }
}
