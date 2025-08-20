package com.boreebeko.calio.service;

import com.boreebeko.calio.dto.EventDTO;
import com.boreebeko.calio.model.ReminderMethod;

import java.util.List;

public interface EventService {
    EventDTO createNewEvent(EventDTO eventDTO, boolean scheduleReminder, ReminderMethod reminderMethod);
    List<EventDTO> getAllEvents();
    void deleteEvent(Long eventId);
}
