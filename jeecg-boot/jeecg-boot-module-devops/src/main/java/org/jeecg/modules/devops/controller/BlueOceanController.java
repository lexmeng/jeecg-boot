package org.jeecg.modules.devops.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.devops.blueocean.vo.BlueOceanFolder;
import org.jeecg.modules.devops.blueocean.vo.BlueOceanJob;
import org.jeecg.modules.devops.blueocean.vo.BlueOceanRun;
import org.jeecg.modules.devops.service.BlueOceanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "BlueOcean API")
@RestController
@RequestMapping("/blueocean")
public class BlueOceanController {

    @Autowired
    private BlueOceanService blueOceanService;


    @ApiOperation(value = "目录列表")
    @GetMapping("/folders")
    public Result<List<BlueOceanFolder>> getFolders() {
        return Result.ok(blueOceanService.getFolders());
    }

    @ApiOperation(value = "任务列表")
    @GetMapping("/folders/{folder}/jobs")
    public Result<List<BlueOceanJob>> getJobs(@PathVariable("folder") String folder) {
        return Result.ok(blueOceanService.getJobs(folder));
    }


    @ApiOperation(value = "任务运行列表")
    @GetMapping("/folders/{folder}/jobs/{job}/runs")
    public Result<List<BlueOceanRun>> getJobs(@PathVariable("folder") String folder, @PathVariable("job") String job) {
        return Result.ok(blueOceanService.getRuns(folder, job));
    }

    @ApiOperation(value = "下载运行日志")
    @GetMapping("/folders/{folder}/jobs/{job}/runs/{number}")
    public Result<String> getJobs(@PathVariable("folder") String folder, @PathVariable("job") String job, @PathVariable("number") String number) {
        return Result.ok(blueOceanService.downloadJobRunLog(folder, job, number));
    }
}
