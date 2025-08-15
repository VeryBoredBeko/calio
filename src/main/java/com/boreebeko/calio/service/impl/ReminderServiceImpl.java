package com.boreebeko.calio.service.impl;

import com.boreebeko.calio.dto.ReminderDTO;
import com.boreebeko.calio.exception.NoSuchCalendarEntityException;
import com.boreebeko.calio.exception.NoSuchEventEntityException;
import com.boreebeko.calio.job.ConsoleNotificationJob;
import com.boreebeko.calio.model.Event;
import com.boreebeko.calio.model.Reminder;
import com.boreebeko.calio.model.projection.CalendarIdOwnerIdProjection;
import com.boreebeko.calio.repository.CalendarRepository;
import com.boreebeko.calio.repository.EventRepository;
import com.boreebeko.calio.repository.ReminderRepository;
import com.boreebeko.calio.service.EventService;
import com.boreebeko.calio.service.ReminderService;
import com.boreebeko.calio.service.UserService;
import com.boreebeko.calio.service.mapper.ReminderMapper;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Date;

@Service
@Transactional(readOnly = true)
public class ReminderServiceImpl implements ReminderService {

    private final ReminderMapper reminderMapper;
    private final ReminderRepository reminderRepository;
    private final EventRepository eventRepository;
    private final CalendarRepository calendarRepository;
    private final UserService userService;
    private final EventService eventService;

    private final Scheduler scheduler;

    @Autowired
    public ReminderServiceImpl(ReminderMapper reminderMapper,
                               ReminderRepository reminderRepository,
                               EventRepository eventRepository,
                               CalendarRepository calendarRepository,
                               UserService userService, EventService eventService, Scheduler scheduler) {

        this.reminderMapper = reminderMapper;
        this.reminderRepository = reminderRepository;
        this.eventRepository = eventRepository;
        this.calendarRepository = calendarRepository;
        this.userService = userService;
        this.eventService = eventService;
        this.scheduler = scheduler;
    }

    @Override
    @Transactional
    public ReminderDTO createReminder(Long calendarId, Long eventId, ReminderDTO reminderDTO) throws SchedulerException {

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

        JobDetail jobDetail = JobBuilder.newJob(ConsoleNotificationJob.class)
                .withIdentity("remindJob-" + persistedReminder.getId(), "test")
                .usingJobData("reminderId", persistedReminder.getId())
                .usingJobData("method", persistedReminder.getReminderMethod().toString())
                .usingJobData("userId", userService.getCurrentUserUUID().toString())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("remindTrigger-" + persistedReminder.getId(), "test")
                .startAt(Date.from(persistedReminder.getRemindAt().atZoneSameInstant(ZoneId.systemDefault()).toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                .build();

        scheduler.scheduleJob(jobDetail, trigger);

        return reminderMapper.toDTO(persistedReminder);
    }

    @Override
    @Transactional
    public void deleteReminder(Long eventId) {

    }
}
