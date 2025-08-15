package com.boreebeko.calio.job;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConsoleNotificationJob implements Job {

    private final static Logger logger = LoggerFactory.getLogger(ConsoleNotificationJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        logger.info("Reminder with ID - {}, method {}", jobDataMap.get("reminderId"), jobDataMap.get("method"));
    }
}
