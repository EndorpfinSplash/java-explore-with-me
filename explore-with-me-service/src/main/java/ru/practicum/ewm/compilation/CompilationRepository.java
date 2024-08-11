package ru.practicum.ewm.compilation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventStatus;
import ru.practicum.ewm.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

}
