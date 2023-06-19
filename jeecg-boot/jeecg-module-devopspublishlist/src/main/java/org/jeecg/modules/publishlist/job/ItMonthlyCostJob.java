package org.jeecg.modules.publishlist.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.publishlist.controller.ItSoftwareMonthlyCostController;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ItMonthlyCostJob implements Job {

    @Autowired
    private ItSoftwareMonthlyCostController itSoftwareMonthlyCostController;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }

}
