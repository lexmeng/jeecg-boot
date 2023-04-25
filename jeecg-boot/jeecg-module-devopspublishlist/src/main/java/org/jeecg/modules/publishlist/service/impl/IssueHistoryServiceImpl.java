package org.jeecg.modules.publishlist.service.impl;

import org.jeecg.modules.publishlist.entity.IssueHistory;
import org.jeecg.modules.publishlist.mapper.IssueHistoryMapper;
import org.jeecg.modules.publishlist.service.IIssueHistoryService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: issue历史表
 * @Author: jeecg-boot
 * @Date:   2023-04-17
 * @Version: V1.0
 */
@Service
public class IssueHistoryServiceImpl extends ServiceImpl<IssueHistoryMapper, IssueHistory> implements IIssueHistoryService {

}
