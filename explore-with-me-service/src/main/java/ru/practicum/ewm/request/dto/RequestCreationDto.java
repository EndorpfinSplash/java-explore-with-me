package ru.practicum.ewm.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.request.RequestStatus;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor

public class RequestCreationDto {

    private Long userId;
    private Long eventId;

    @Builder.Default
    private RequestStatus status = RequestStatus.WAITING;

}
