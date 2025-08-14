package com.boreebeko.calio.service.impl;

import com.boreebeko.calio.dto.EventDTO;
import com.boreebeko.calio.exception.NoSuchEventEntityException;
import com.boreebeko.calio.model.Calendar;
import com.boreebeko.calio.model.Event;
import com.boreebeko.calio.model.projection.CalendarIdProjection;
import com.boreebeko.calio.model.projection.EventWithSimpleCalendarProjection;
import com.boreebeko.calio.repository.CalendarRepository;
import com.boreebeko.calio.repository.EventRepository;
import com.boreebeko.calio.service.EventService;
import com.boreebeko.calio.service.UserService;
import com.boreebeko.calio.service.mapper.EventMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional(readOnly = true)
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    private final UserService userService;
    private final CalendarRepository calendarRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper, UserService userService, CalendarRepository calendarRepository) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.userService = userService;
        this.calendarRepository = calendarRepository;
    }

    @Override
    @Transactional
    public EventDTO createNewEvent(EventDTO eventDTO) {
        Event newEvent = eventMapper.toEntity(eventDTO);

        Calendar userCalendar =
                calendarRepository.findCalendarsByOwnerId(userService.getCurrentUserUUID());

        newEvent.setCalendar(userCalendar);

        Event persistedEvent = eventRepository.save(newEvent);
        return eventMapper.toDTO(persistedEvent);
    }

    @Override
    public List<EventDTO> getAllEvents() {

        UUID userId = userService.getCurrentUserUUID();
        CalendarIdProjection calendarId = calendarRepository.findCalendarIdByOwnerId(userId);

        List<Event> persistedEvents = eventRepository.findEventsByCalendarId(calendarId.getId());
        return eventMapper.toDTOList(persistedEvents);
    }

    @Override
    @Transactional
    public void deleteEvent(Long eventId) {

        UUID userId = userService.getCurrentUserUUID();

        EventWithSimpleCalendarProjection persistedEvent = eventRepository
                .findEventProjectionById(eventId)
                .orElseThrow(() -> new NoSuchEventEntityException("There is no event entity with such id"));

        if (!persistedEvent.getCalendar().getOwnerId().equals(userId))
            throw new AccessDeniedException("User hasn't enough authority to delete event");

        eventRepository.deleteById(eventId);
    }
}
