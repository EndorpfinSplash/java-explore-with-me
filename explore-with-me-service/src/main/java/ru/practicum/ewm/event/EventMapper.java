package ru.practicum.ewm.event;

import ru.practicum.ewm.event.dto.EventCreationDto;
import ru.practicum.ewm.event.dto.EventOutDto;
import ru.practicum.ewm.event.dto.EventUpdateDto;
import ru.practicum.ewm.event_category.EventCategory;
import ru.practicum.ewm.event_category.EventLocation;
import ru.practicum.ewm.user.User;

public class EventMapper {

    public static Event creationDtoToEvent(EventCreationDto eventCreationDto,
                                           User user,
                                           EventCategory eventCategory
    ) {
        return Event.builder()
                .user(user)
                .annotation(eventCreationDto.getAnnotation())
                .category(eventCategory)
                .description(eventCreationDto.getDescription())
                .eventDate(eventCreationDto.getEventDate())
                .locationLon(eventCreationDto.getLocation().getLon())
                .locationLat(eventCreationDto.getLocation().getLat())
                .paid(eventCreationDto.isPaid())
                .participantLimit(eventCreationDto.getParticipantLimit())
                .requestModeration(eventCreationDto.isRequestModeration())
                .title(eventCreationDto.getTitle())
                .eventStatus(EventStatus.WAITING)
                .build();
    }


    public static EventOutDto eventToOutDto(Event savedEvent, long views, long confirmedRequests) {
        return EventOutDto.builder()
                .id(savedEvent.getId())
                .annotation(savedEvent.getAnnotation())
                .category(savedEvent.getCategory())
                .confirmedRequests(confirmedRequests)
                .createdOn(savedEvent.getCreatedOn())
                .description(savedEvent.getDescription())
                .eventDate(savedEvent.getEventDate())
                .initiator(savedEvent.getUser())
                .location(EventLocation.builder()
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

    public static EventOutDto eventToOutDto(EventUpdateDto eventUpdateDto) {
        EventOutDto eventOutDto = new EventOutDto();
        if (eventUpdateDto.getAnnotation() != null) {
            eventOutDto.setAnnotation(eventUpdateDto.getAnnotation());
        }
        if (eventUpdateDto.getTitle() != null) {
            eventOutDto.setTitle(eventUpdateDto.getTitle());
        }
        if (eventUpdateDto.getDescription() != null) {
            eventOutDto.setDescription(eventUpdateDto.getDescription());
        }
        if (eventUpdateDto.getEventDate() != null) {
            eventOutDto.setEventDate(eventUpdateDto.getEventDate());
        }
        if (eventUpdateDto.getLocation() != null) {
            eventOutDto.setLocation(eventUpdateDto.getLocation());
        }
        return eventOutDto;
    }
}
