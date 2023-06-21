package org.jeecg.modules.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.publishlist.tools.SpringContextProvider;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(tags="查询系统的配置项")
@RestController
@RequestMapping("/system/property")
@Slf4j
public class PropertyController {
    public static Environment environment;
    @ApiOperation(value="查询系统配置项", notes="查询系统配置项")
    @GetMapping(value = "/query")
    public Result<String> queryProperty(@RequestParam(name="propertyName", defaultValue="") String propertyName,
                                                HttpServletRequest req) {
        //String propertyValue = SpringContextProvider.getEnvProperty(propertyName);
        String propertyValue = environment.getProperty(propertyName);
        return Result.OK(propertyValue);
    }
}
