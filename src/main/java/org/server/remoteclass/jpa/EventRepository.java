package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByEventId(Long eventId);
}
