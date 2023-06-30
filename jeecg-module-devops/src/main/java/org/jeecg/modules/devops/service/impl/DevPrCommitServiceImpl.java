package org.jeecg.modules.devops.service.impl;

import org.jeecg.modules.devops.entity.DevPrCommit;
import org.jeecg.modules.devops.mapper.DevPrCommitMapper;
import org.jeecg.modules.devops.service.IDevPrCommitService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: pr commit表-跟pr ut一一对应
 * @Author: jeecg-boot
 * @Date:   2023-06-12
 * @Version: V1.0
 */
@Service
public class DevPrCommitServiceImpl extends ServiceImpl<DevPrCommitMapper, DevPrCommit> implements IDevPrCommitService {

}
