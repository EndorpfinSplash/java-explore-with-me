package ru.practicum.ewm.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.request.dto.RequestOutDto;
import ru.practicum.ewm.user.User;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findRequestByRequesterAndEvent(User requester, Event event);

    int countRequestByEventAndStatus(Event event, RequestStatus requestStatus);

    Collection<RequestOutDto> getAllByRequesterIdOrderById(Long userId);

    Collection<Request> findRequestByEventId(Long eventId);
}
