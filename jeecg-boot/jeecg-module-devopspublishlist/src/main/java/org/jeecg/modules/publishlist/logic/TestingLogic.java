package org.jeecg.modules.publishlist.logic;

import org.jeecg.modules.publishlist.entity.TestQuardResult;
import org.jeecg.modules.publishlist.entity.TestQuardTask;
import org.jeecg.modules.publishlist.tools.IdTool;
import org.jeecg.modules.publishlist.vo.AllureResult;
import org.jeecg.modules.publishlist.vo.QuardTestProcess;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestingLogic {

    public static TestQuardTask generateTestQuardTaskFromQuardTestProcess(QuardTestProcess quardTestProcess){
        TestQuardTask testQuardTask = new TestQuardTask();
        testQuardTask.setId(IdTool.generalId());
        testQuardTask.setJenkinsJobNum(quardTestProcess.getJenkinsJobId());
        testQuardTask.setPlatformName(quardTestProcess.getPlatform());

        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
            Date startTime = dateFormat.parse(quardTestProcess.getTestTime());
            testQuardTask.setStartTime(startTime);
        }catch (ParseException parseException){
            parseException.printStackTrace();
        }

        testQuardTask.setVersion(quardTestProcess.getKEVersion());
        testQuardTask.setQuardStage(quardTestProcess.getQuardStage());
        testQuardTask.setTestType(quardTestProcess.getTestType());
        testQuardTask.setUpgradeFrom(quardTestProcess.getUpgradeFrom());
        testQuardTask.setUpgradeTo(quardTestProcess.getUpgradeTo());

        return testQuardTask;
    }

    public static TestQuardResult generateTestQuardResultFromAllureResult(AllureResult allureResult, String requestId, String jenkinsJobNo){
        TestQuardResult testQuardResult = new TestQuardResult();
        testQuardResult.setId(IdTool.generalId());

        testQuardResult.setQuardRequestId(requestId);
        testQuardResult.setJenkinsJobNum(jenkinsJobNo);
        testQuardResult.setTotalCaseNum(allureResult.getTotal());
        testQuardResult.setFailedCaseNum(allureResult.getFailed());
        testQuardResult.setBrokenCaseNum(allureResult.getBroken());
        testQuardResult.setSkippedCaseNum(allureResult.getSkipped());
        testQuardResult.setPassedCaseNum(allureResult.getPassed());
        testQuardResult.setUnknownCaseNum(allureResult.getUnknown());

        return testQuardResult;
    }
}
