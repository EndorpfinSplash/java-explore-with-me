package ru.practicum.ewm.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByUserIdOrderById(Long userId, Pageable page);

    Optional<Event> findEventByUserAndId(User user, Long eventId);

    Optional<Event> findEventByIdAndEventStatus(Long eventId, EventStatus status);

    @Query(value =
            "SELECT t.* " +
                    "FROM event t cross join (select * from location where id = :locationId) l " +
                    "where l.radius - distance(l.location_lat, l.location_lon, t.location_lat, t.location_lon) > 0 ",
            nativeQuery = true)
    List<Event> getEventsInLocationByLocationId(Long locationId);
}
