package org.jeecg.modules.publishlist.service.impl;


import org.jeecg.modules.publishlist.entity.TestQuardRequest;
import org.jeecg.modules.publishlist.exception.BussinessException;
import org.jeecg.modules.publishlist.mapper.TestQuardRequestMapper;
import org.jeecg.modules.publishlist.service.ITestQuardRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: quard job请求表
 * @Author: jeecg-boot
 * @Date:   2023-06-02
 * @Version: V1.0
 */
@Service
public class TestQuardRequestServiceImpl extends ServiceImpl<TestQuardRequestMapper, TestQuardRequest> implements ITestQuardRequestService {
    @Autowired
    private TestQuardRequestMapper testQuardRequestMapper;

    @Override
    public String getRequestIdByJenkinsJobNo(String jenkinsJobNo){
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("jenkins_job_num",jenkinsJobNo);
        List<TestQuardRequest> requestList = testQuardRequestMapper.selectByMap(queryMap);
        if(requestList==null || requestList.size()==0){
            throw new BussinessException("没查询到请求！");
        }

        if(requestList.size() >1){
            throw new BussinessException("请求数量大于1！");
        }

        return requestList.get(0).getId();
    }

}
