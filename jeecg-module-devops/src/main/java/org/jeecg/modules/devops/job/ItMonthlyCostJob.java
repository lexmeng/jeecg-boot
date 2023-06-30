package org.jeecg.modules.devops.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.devops.controller.ItSoftwareMonthlyCostController;
import org.jeecg.modules.devops.tools.FeishuMessageUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Slf4j
@Component
public class ItMonthlyCostJob implements Job {

    @Autowired
    private ItSoftwareMonthlyCostController itSoftwareMonthlyCostController;

    @Autowired
    private FeishuMessageUtils feishuMessageUtils;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info(String.format(" 普通定时任务 ItMonthlyCostJob 开始!  时间:" + DateUtils.getTimestamp()));
        feishuMessageUtils.sendFeiShuMsg("定时任务ItMonthlyCostJob开始！");

        Integer year = DateUtils.getCalendar().get(Calendar.YEAR);
        Integer month = DateUtils.getCalendar().get(Calendar.MONTH);
        try{
            itSoftwareMonthlyCostController.generateMonthlyCost(year.toString(), month.toString());
        }catch (Exception e){
            log.info(String.format(" Jeecg-Boot 普通定时任务 ItMonthlyCostJob 报错结束!  时间:" + DateUtils.getTimestamp()));
            feishuMessageUtils.sendFeiShuMsg("定时任务ItMonthlyCostJob报错结束！错误："+e.getMessage());
        }


        log.info(String.format(" Jeecg-Boot 普通定时任务 ItMonthlyCostJob 结束!  时间:" + DateUtils.getTimestamp()));
        feishuMessageUtils.sendFeiShuMsg("定时任务ItMonthlyCostJob结束！");
    }

}
