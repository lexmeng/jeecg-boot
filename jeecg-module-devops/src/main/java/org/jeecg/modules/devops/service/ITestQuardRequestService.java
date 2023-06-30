package org.jeecg.modules.devops.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.devops.entity.TestQuardRequest;

/**
 * @Description: quard job请求表
 * @Author: jeecg-boot
 * @Date:   2023-06-02
 * @Version: V1.0
 */
public interface ITestQuardRequestService extends IService<TestQuardRequest> {

    public String getRequestIdByJenkinsJobNo(String jenkinsJobNo);

}
