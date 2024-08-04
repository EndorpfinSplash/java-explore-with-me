package ru.practicum.ewm.request;

import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.request.dto.NewUserRequest;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.user.User;

public class RequestMapper {
    public static Request NewRequestDtoToRequest(NewUserRequest newUserRequest, User user, Event event) {
        return Request.builder()
                .requester(user)
                .event(event)
                .build();
    }

    public static ParticipationRequestDto requestToParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus().name())
                .build();
    }
}
