package com.boreebeko.calio.service;

import com.boreebeko.calio.dto.EventDTO;

import java.util.List;

public interface EventService {
    EventDTO createNewEvent(EventDTO eventDTO);
    List<EventDTO> getAllEvents();
    void deleteEvent(Long eventId);
}
