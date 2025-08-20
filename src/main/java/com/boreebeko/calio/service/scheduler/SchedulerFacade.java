package com.boreebeko.calio.service.scheduler;

import com.boreebeko.calio.model.Reminder;

public interface SchedulerFacade {
    void scheduleJob(Reminder unscheduledReminder);
    void unscheduleJob(Reminder scheduledReminder);
}
