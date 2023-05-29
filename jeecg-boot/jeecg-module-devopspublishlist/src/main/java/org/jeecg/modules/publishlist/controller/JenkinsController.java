package org.jeecg.modules.publishlist.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.publishlist.bpservice.JenkinsBPService;
import org.jeecg.modules.publishlist.vo.JenkinsJobReq;
import org.jeecg.modules.publishlist.entity.Publishlist;
import org.jeecg.modules.publishlist.vo.PublishlistPage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags="JenkinsController")
@RestController
@RequestMapping("/jenkins")
@Slf4j
public class JenkinsController {
    @Autowired
    private JenkinsBPService jenkinsBPService;

    @AutoLog(value = "执行参数任务")
    @ApiOperation(value="执行参数任务", notes="执行参数任务")
    @PostMapping(value = "/exeJob")
    public Result<String> add(@RequestBody JenkinsJobReq jenkinsJobReq) {
        jenkinsBPService.jenkinsBuildWithParameters(jenkinsJobReq.getTypeName(), jenkinsJobReq.getFolderName(), jenkinsJobReq.getJobName(), jenkinsJobReq.getParamMap());

        return Result.OK("执行成功！");
    }
}
