package com.boreebeko.calio.repository;

import com.boreebeko.calio.model.Calendar;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CalendarRepository extends ListCrudRepository<Calendar, Long> {

    List<Calendar> findCalendarsByOwnerId(UUID ownerId);
}
