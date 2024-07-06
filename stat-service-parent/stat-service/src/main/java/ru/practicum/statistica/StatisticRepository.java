package ru.practicum.statistica;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.commons.EventStatisticByEventDto;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<Event, Long> {

    @Query("select new ru.practicum.commons.EventStatisticByEventDto( e.app, e.uri, count(*)) " +
            " from Event as e " +
            " where e.timestamp between ?1 and ?2 " +
            " and e.uri in (?3) " +
            " group by e.app, e.uri " +
            " order by count(*) desc")
    List<EventStatisticByEventDto> countEvents(LocalDateTime from, LocalDateTime to, List<String> uriList);

    @Query("select new ru.practicum.commons.EventStatisticByEventDto( e.app, e.uri, count(*)) " +
            " from Event as e " +
            " where e.timestamp between ?1 and ?2 " +
            " group by e.app, e.uri " +
            " order by count(*) desc")
    List<EventStatisticByEventDto> countEvents(LocalDateTime from, LocalDateTime to);

    @Query("select new ru.practicum.commons.EventStatisticByEventDto( e.app, e.uri, count(distinct e.ip)) " +
            " from Event as e " +
            " where e.timestamp between ?1 and ?2 " +
            " and e.uri in (?3) " +
            " group by e.app, e.uri " +
            " order by count(distinct e.ip) desc")
    List<EventStatisticByEventDto> countUniqIpForEvents(LocalDateTime from, LocalDateTime to, List<String> uriList);

    @Query("select new ru.practicum.commons.EventStatisticByEventDto( e.app, e.uri, count(distinct e.ip)) " +
            " from Event as e " +
            " where e.timestamp between ?1 and ?2 " +
            " group by e.app, e.uri " +
            " order by count(distinct e.ip) desc")
    List<EventStatisticByEventDto> countUniqIpForEvents(LocalDateTime from, LocalDateTime to);
}
