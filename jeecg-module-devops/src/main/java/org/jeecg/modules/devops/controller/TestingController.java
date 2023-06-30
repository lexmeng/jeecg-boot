package org.jeecg.modules.devops.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.devops.bpservice.JenkinsBPService;
import org.jeecg.modules.devops.entity.TestQuardResult;
import org.jeecg.modules.devops.entity.TestQuardTask;
import org.jeecg.modules.devops.logic.TestingLogic;
import org.jeecg.modules.devops.service.ITestQuardResultService;
import org.jeecg.modules.devops.service.ITestQuardTaskService;
import org.jeecg.modules.devops.vo.AllureResult;
import org.jeecg.modules.devops.vo.QuardTestParam;
import org.jeecg.modules.devops.vo.QuardTestProcess;
import org.jeecg.modules.devops.vo.StepTestParam;
import org.jeecg.modules.devops.service.ITestQuardRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags="TestingController")
@RestController
@RequestMapping("/testing")
@Slf4j
public class TestingController {

    @Autowired
    private JenkinsBPService jenkinsBPService;

    @Autowired
    private ITestQuardTaskService testQuardTaskService;

    @Autowired
    private ITestQuardResultService testQuardResultService;

    @Autowired
    private ITestQuardRequestService testQuardRequestService;

    @AutoLog(value = "Quard测试jenkins任务")
    @ApiOperation(value="Quard测试jenkins任务", notes="Quard测试jenkins任务")
    @PostMapping(value = "/quardJenkins")
    public Result<String> quardJenkins(@RequestBody QuardTestParam quardTestParam) {
        jenkinsBPService.executeKE4QuardTestJob(quardTestParam);

        return Result.OK("执行成功！");
    }

    @AutoLog(value = "Step测试jenkins任务")
    @ApiOperation(value="Step测试jenkins任务", notes="Step测试jenkins任务")
    @PostMapping(value = "/stepJenkins")
    public Result<String> stepJenkins(@RequestBody StepTestParam stepTestParam) {
        jenkinsBPService.executeKE4StepTestJob(stepTestParam);
        return Result.OK("执行成功！");
    }

    @AutoLog(value = "quard测试过程中报告数据")
    @ApiOperation(value="quard测试过程中报告数据", notes="quard测试过程中报告数据")
    @PostMapping(value = "/quardReportProcessData")
    @Transactional
    public Result<String> quardReportTestProcess(@RequestBody QuardTestProcess quardTestProcess){
        if(quardTestProcess.getQuardStage()==null || quardTestProcess.getQuardStage().isEmpty()){
            log.info("quardReportTestProcess quard 状态为空！");
            return Result.error("quard 状态为空！");
        }

        TestQuardTask testQuardTask = TestingLogic.generateTestQuardTaskFromQuardTestProcess(quardTestProcess);
        testQuardTaskService.save(testQuardTask);

        if("finished".equals(quardTestProcess.getQuardStage().toLowerCase())){
            AllureResult allureResult = quardTestProcess.getAllureResult();
            String requestId = testQuardRequestService.getRequestIdByJenkinsJobNo(quardTestProcess.getJenkinsJobId());
            TestQuardResult testQuardResult = TestingLogic.generateTestQuardResultFromAllureResult(allureResult, requestId, quardTestProcess.getJenkinsJobId(), quardTestProcess.getReportLink());

            testQuardResultService.save(testQuardResult);
        }

        return Result.OK("执行成功！");
    }
}
