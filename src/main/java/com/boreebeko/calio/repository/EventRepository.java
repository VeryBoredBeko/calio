package com.boreebeko.calio.repository;

import com.boreebeko.calio.model.Event;
import com.boreebeko.calio.model.projection.EventWithSimpleCalendarProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends ListCrudRepository<Event, Long> {

    List<Event> findEventsByCalendarId(Long id);

    @Query("""
        SELECT e
        FROM Event e
        JOIN e.calendar c
        WHERE e.id = :eventId
        """)
    Optional<EventWithSimpleCalendarProjection> findEventProjectionById(Long eventId);
}
