package com.boreebeko.calio.job;

import com.boreebeko.calio.service.EmailService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class EmailReminderJob implements Job {

    private final EmailService emailService;

    public EmailReminderJob(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        emailService.sendMessage(
                jobDataMap.getString("email"),
                jobDataMap.getString("subject"),
                jobDataMap.getString("text")
        );
    }
}
