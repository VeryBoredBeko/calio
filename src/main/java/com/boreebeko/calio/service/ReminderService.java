package com.boreebeko.calio.service;

import com.boreebeko.calio.dto.EventDTO;
import com.boreebeko.calio.dto.ReminderDTO;

public interface ReminderService {
    ReminderDTO createReminder(Long calendarId, Long eventId, ReminderDTO reminderDTO);
    void deleteReminderByEventId(Long eventId);
}
