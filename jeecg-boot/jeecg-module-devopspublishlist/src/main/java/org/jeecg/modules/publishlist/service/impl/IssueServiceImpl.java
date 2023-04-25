package org.jeecg.modules.publishlist.service.impl;

import org.jeecg.modules.publishlist.entity.Issue;
import org.jeecg.modules.publishlist.mapper.IssueMapper;
import org.jeecg.modules.publishlist.service.IIssueService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: issue本地记录
 * @Author: jeecg-boot
 * @Date:   2023-04-17
 * @Version: V1.0
 */
@Service
public class IssueServiceImpl extends ServiceImpl<IssueMapper, Issue> implements IIssueService {

}
