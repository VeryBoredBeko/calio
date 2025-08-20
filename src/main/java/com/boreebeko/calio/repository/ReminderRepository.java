package com.boreebeko.calio.repository;

import com.boreebeko.calio.model.Reminder;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReminderRepository extends ListCrudRepository<Reminder, Long> {

    Optional<Reminder> findReminderByEventId(Long eventId);
}
