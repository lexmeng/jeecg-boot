package org.jeecg.modules.devops.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags="github")
@RestController
@RequestMapping("/github")
@Slf4j
public class GithubController {
    @AutoLog(value = "github pr回调接口")
    @ApiOperation(value="github pr回调接口", notes="github pr回调接口")
    @PostMapping(value = "/handleWebhook")
    public void handleWebhook(@RequestBody String payload) {
        log.info("github webhook payload:"+payload);
    }
}
