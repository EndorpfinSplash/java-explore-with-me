package ru.practicum.ewm.request;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;

public class RequestMapper {

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
