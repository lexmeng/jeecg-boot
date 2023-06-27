package org.jeecg.modules.publishlist.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.publishlist.controller.ItFaController;
import org.jeecg.modules.publishlist.tools.FeishuMessageUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Slf4j
@Component
public class ItFixAssertsDepreciationJob implements Job {

    @Autowired
    private FeishuMessageUtils feishuMessageUtils;

    @Autowired
    private ItFaController itFaController;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info(String.format(" 普通定时任务 ItFixAssertsDepreciationJob 开始!  时间:" + DateUtils.getTimestamp()));
        feishuMessageUtils.sendFeiShuMsg("定时任务ItFixAssertsDepreciationJob开始！");

        Integer year = DateUtils.getCalendar().get(Calendar.YEAR);
        Integer month = DateUtils.getCalendar().get(Calendar.MONTH);

        try{
            itFaController.depreciationAll();
        }catch (Exception e){
            log.info(String.format(" Jeecg-Boot 普通定时任务 ItFixAssertsDepreciationJob 报错结束!  时间:" + DateUtils.getTimestamp()));
            feishuMessageUtils.sendFeiShuMsg("定时任务ItFixAssertsDepreciationJob报错结束！错误："+e.getMessage());
        }


        log.info(String.format(" Jeecg-Boot 普通定时任务 ItFixAssertsDepreciationJob 结束!  时间:" + DateUtils.getTimestamp()));
        feishuMessageUtils.sendFeiShuMsg("定时任务ItFixAssertsDepreciationJob结束！");
    }
}
