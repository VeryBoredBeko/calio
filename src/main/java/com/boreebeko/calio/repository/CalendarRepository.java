package com.boreebeko.calio.repository;

import com.boreebeko.calio.model.Calendar;
import com.boreebeko.calio.model.projection.CalendarIdProjection;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CalendarRepository extends ListCrudRepository<Calendar, Long> {

    Calendar findCalendarsByOwnerId(UUID ownerId);
    CalendarIdProjection findCalendarIdByOwnerId(UUID ownerId);
}
