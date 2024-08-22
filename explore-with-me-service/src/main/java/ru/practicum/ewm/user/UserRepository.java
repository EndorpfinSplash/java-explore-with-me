package ru.practicum.ewm.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(" select u from User u " +
            " where u.id in (?1) " +
            " order by u.id ")
    List<User> findAllByIdOrderById(List<Long> ids, Pageable page);

}
