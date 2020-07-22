package com.manu.test.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class TestQuartzJob implements Job { // extends QuartzJobBean

    private static final Logger logger = LoggerFactory.getLogger(TestQuartzJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException { // executeInternal

        logger.info("Execution time : {}", ZonedDateTime.now());
        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String message = jobDataMap.getString("message");

        logger.info("Testing quartz job: {}", message);
    }
}
