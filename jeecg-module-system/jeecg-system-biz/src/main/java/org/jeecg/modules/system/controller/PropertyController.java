package org.jeecg.modules.system.controller;

import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.context.ConfigurableApplicationContext;
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

    public static ConfigurableApplicationContext applicationContext;

    @ApiOperation(value="查询系统配置项", notes="查询系统配置项")
    @GetMapping(value = "/query")
    public Result<String> queryProperty(@RequestParam(name="propertyName", defaultValue="") String propertyName,
                                                HttpServletRequest req) {
        //String propertyValue = SpringContextProvider.getEnvProperty(propertyName);
        String propertyValue = environment.getProperty(propertyName);
        return Result.OK(propertyValue);
    }

    @ApiOperation(value="查询bean", notes="查询bean")
    @GetMapping(value = "/queryBean")
    public Result<String> queryBean(@RequestParam(name="beanName", defaultValue="") String beanName,@RequestParam(name="className", defaultValue="") String className,
                                        HttpServletRequest req) {
        //String propertyValue = SpringContextProvider.getEnvProperty(propertyName);
        Object object = applicationContext.getBean(beanName);

        Class clazz;
        try{
            clazz = Class.forName(className);
        }catch(ClassNotFoundException e){
            return Result.error("找不到类名！object.getClass():"+object.getClass());
        }

        return Result.OK(new Gson().toJson(object, clazz));
    }
}
