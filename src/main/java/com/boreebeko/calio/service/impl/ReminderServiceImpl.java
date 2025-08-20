package com.boreebeko.calio.service.impl;

import com.boreebeko.calio.dto.ReminderDTO;
import com.boreebeko.calio.exception.NoSuchCalendarEntityException;
import com.boreebeko.calio.exception.NoSuchEventEntityException;
import com.boreebeko.calio.exception.NoSuchReminderEntityException;
import com.boreebeko.calio.model.Event;
import com.boreebeko.calio.model.Reminder;
import com.boreebeko.calio.model.projection.CalendarIdOwnerIdProjection;
import com.boreebeko.calio.repository.CalendarRepository;
import com.boreebeko.calio.repository.EventRepository;
import com.boreebeko.calio.repository.ReminderRepository;
import com.boreebeko.calio.service.ReminderService;
import com.boreebeko.calio.service.UserService;
import com.boreebeko.calio.service.mapper.ReminderMapper;
import com.boreebeko.calio.service.scheduler.SchedulerFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReminderServiceImpl implements ReminderService {

    private final ReminderMapper reminderMapper;
    private final ReminderRepository reminderRepository;
    private final EventRepository eventRepository;
    private final CalendarRepository calendarRepository;
    private final UserService userService;

    private final SchedulerFacade scheduler;

    @Autowired
    public ReminderServiceImpl(ReminderMapper reminderMapper,
                               ReminderRepository reminderRepository,
                               EventRepository eventRepository,
                               CalendarRepository calendarRepository,
                               UserService userService,
                               SchedulerFacade scheduler) {

        this.reminderMapper = reminderMapper;
        this.reminderRepository = reminderRepository;
        this.eventRepository = eventRepository;
        this.calendarRepository = calendarRepository;
        this.userService = userService;
        this.scheduler = scheduler;
    }

    @Override
    @Transactional
    public ReminderDTO createReminder(Long calendarId, Long eventId, ReminderDTO reminderDTO) {

        CalendarIdOwnerIdProjection calendarProjection =
                calendarRepository
                        .findCalendarByOwnerId(userService.getCurrentUserUUID())
                        .orElseThrow(() -> new NoSuchCalendarEntityException("Calendar for user with such UUID doesn't exist"));

        if (!calendarProjection.getId().equals(calendarId)) {
            throw new AccessDeniedException("User don't have authority to create reminder");
        }

        Event existingEvent = eventRepository.findEventByEventId(eventId)
                .orElseThrow(() -> new NoSuchEventEntityException("There is no event with such ID"));

        Reminder newReminder = reminderMapper.toEntity(reminderDTO);
        newReminder.setEvent(existingEvent);

        Reminder persistedReminder = reminderRepository.save(newReminder);
        scheduler.scheduleJob(persistedReminder);

        return reminderMapper.toDTO(persistedReminder);
    }

    @Override
    @Transactional
    public void deleteReminderByEventId(Long eventId) {
        Reminder scheduledReminder = reminderRepository
                .findReminderByEventId(eventId)
                .orElseThrow(() -> new NoSuchReminderEntityException("There is no reminder entity associated with event with ID " + eventId));

        scheduler.unscheduleJob(scheduledReminder);
        reminderRepository.delete(scheduledReminder);
    }
}
