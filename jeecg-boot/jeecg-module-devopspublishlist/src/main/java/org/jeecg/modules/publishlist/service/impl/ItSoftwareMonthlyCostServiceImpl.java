package org.jeecg.modules.publishlist.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.publishlist.entity.Issue;
import org.jeecg.modules.publishlist.entity.ItSoftwareMonthlyCost;
import org.jeecg.modules.publishlist.mapper.IssueMapper;
import org.jeecg.modules.publishlist.mapper.ItSoftwareMonthlyCostMapper;
import org.jeecg.modules.publishlist.service.IIssueService;
import org.jeecg.modules.publishlist.service.IItSoftwareMonthlyCostService;
import org.springframework.stereotype.Service;

@Service
public class ItSoftwareMonthlyCostServiceImpl extends ServiceImpl<ItSoftwareMonthlyCostMapper, ItSoftwareMonthlyCost> implements IItSoftwareMonthlyCostService {
}
