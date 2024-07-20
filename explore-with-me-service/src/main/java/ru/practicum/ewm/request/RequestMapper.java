package ru.practicum.ewm.request;

import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.request.dto.RequestCreationDto;
import ru.practicum.ewm.request.dto.RequestOutDto;
import ru.practicum.ewm.user.User;

public class RequestMapper {
    public static Request RequesetCreationDtoToRequest(RequestCreationDto requestCreationDto, User user, Event event) {
        return Request.builder()
                .requester(user)
                .event(event)
                .build();
    }

    public static RequestOutDto RequestToOutDto(Request request) {
        return RequestOutDto.builder()
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .build();

    }
}
