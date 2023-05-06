package org.jeecg.modules.publishlist.service.impl;


import org.jeecg.modules.publishlist.entity.Project;
import org.jeecg.modules.publishlist.mapper.ProjectMapper;
import org.jeecg.modules.publishlist.service.IProjectService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 项目表
 * @Author: jeecg-boot
 * @Date:   2023-05-06
 * @Version: V1.0
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {

}
