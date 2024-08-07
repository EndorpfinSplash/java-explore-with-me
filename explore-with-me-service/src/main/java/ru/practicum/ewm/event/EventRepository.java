package ru.practicum.ewm.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByUserIdOrderById(Long userId, Pageable page);

    Optional<Event> findEventByUserAndId(User user, Long eventId);
    Optional<Event> findEventByIdAndEventStatus(Long eventId, EventStatus status);
}
