package ru.practicum.ewm.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.user.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findRequestByRequesterAndEvent(User requester, Event event);

    int countRequestByEventAndStatus(Event event, RequestStatus requestStatus);

    Collection<ParticipationRequestDto> getAllByRequesterIdOrderById(Long userId);

    Collection<Request> findRequestByEventId(Long eventId);

    List<ParticipationRequestDto> getAllByStatusOrderById(RequestStatus status);

    @Query("select new ru.practicum.ewm.request.RequestsCountByEvent(r.event.id, count(r.requester))" +
            "from Request as r "+
            "where r.status = ?1"+
            "group by r.event.id "+
            "order by count(r.requester) ")
    List<RequestsCountByEvent> countRequestByEventId(RequestStatus requestStatus);
}
