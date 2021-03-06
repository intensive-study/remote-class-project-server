package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e " +
            "join fetch e.coupon ")
    List<Event> findAll();

    @Query("select e from Event e " +
            "join fetch e.coupon " +
            "where e.eventId=:eventId")
    Optional<Event> findByEventId(@Param("eventId") Long eventId);
    void deleteByEventId(Long eventId);
}
