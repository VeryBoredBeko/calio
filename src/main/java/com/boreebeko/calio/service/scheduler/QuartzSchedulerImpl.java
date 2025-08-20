package com.boreebeko.calio.service.scheduler;

import com.boreebeko.calio.exception.ReminderSchedulingException;
import com.boreebeko.calio.exception.ReminderUnschedulingException;
import com.boreebeko.calio.job.ConsoleNotificationJob;
import com.boreebeko.calio.job.EmailReminderJob;
import com.boreebeko.calio.model.Reminder;
import com.boreebeko.calio.model.ReminderMethod;
import com.boreebeko.calio.service.UserService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Date;

@Component
public class QuartzSchedulerImpl implements SchedulerFacade {

    private final static String JOB_NAME_PREFIX = "JOB_";
    private final static String JOB_GROUP_PREFIX = "JOB_GROUP_";

    private final static String TRIGGER_GROUP_PREFIX = "TRIGGER_GROUP_";

    private final Scheduler scheduler;
    private final UserService userService;

    @Autowired
    public QuartzSchedulerImpl(Scheduler scheduler, UserService userService) {
        this.scheduler = scheduler;
        this.userService = userService;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void scheduleJob(Reminder reminder) {

        JobDetail jobDetail = JobBuilder
                .newJob(identifyJobType(reminder.getReminderMethod()))
                .withIdentity(JOB_NAME_PREFIX + reminder.getId(), JOB_GROUP_PREFIX + reminder.getReminderMethod().toString())
                .usingJobData("reminderId", reminder.getId())
                .usingJobData("method", reminder.getReminderMethod().toString())
                .usingJobData("userId", userService.getCurrentUserUUID().toString())
                .usingJobData("email", userService.getCurrentUserEmail())
                .usingJobData("subject", "Reminding about upcoming meeting.")
                .usingJobData("text", "You have meeting in one hour.")
                .build();

        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("remindTrigger-" + reminder.getId(), TRIGGER_GROUP_PREFIX)
                .startAt(Date.from(reminder.getRemindAt().atZoneSameInstant(ZoneId.systemDefault()).toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException exception) {
            throw new ReminderSchedulingException("Couldn't schedule reminder with ID " + reminder.getId());
        }
    }

    @Override
    public void unscheduleJob(Reminder scheduledReminder) {
        if (scheduledReminder.getId() == null) throw new IllegalArgumentException();
        try {
            scheduler.deleteJob(new JobKey(JOB_NAME_PREFIX + scheduledReminder.getId(),
                    JOB_GROUP_PREFIX + scheduledReminder.getReminderMethod().toString()));
        } catch (SchedulerException e) {
            throw new ReminderUnschedulingException("Couldn't unschedule reminder with ID " + scheduledReminder.getId());
        }
    }

    private Class<? extends Job> identifyJobType(ReminderMethod method) {
        switch (method) {
            case NOTIFICATION -> {
                return ConsoleNotificationJob.class;
            }
            case EMAIL -> {
                return EmailReminderJob.class;
            }
            default -> throw new UnsupportedOperationException();
        }
    }
}
