package ru.practicum.ewm.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.event.EventStatus;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("SELECT l FROM Location l WHERE l.name = :name")
    List<Location> findByName(@Param("name") String name);

    @Query(value =
            "SELECT t.id " +
            "FROM event t cross join (select * from location where id = :id) l " +
            "where l.radius - distance(l.location_lat, l.location_lon, t.location_lat, t.location_lon) > 0 "+
            "and t.status = :status "
            , nativeQuery = true)
    List<Long> getLocationEventsById(Long id, String status);
}