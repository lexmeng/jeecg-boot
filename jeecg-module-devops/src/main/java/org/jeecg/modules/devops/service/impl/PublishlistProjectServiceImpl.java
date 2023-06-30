package org.jeecg.modules.devops.service.impl;

import org.jeecg.modules.devops.entity.PublishlistProject;
import org.jeecg.modules.devops.mapper.PublishlistProjectMapper;
import org.jeecg.modules.devops.service.IPublishlistProjectService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 发布单项目表
 * @Author: jeecg-boot
 * @Date:   2023-04-17
 * @Version: V1.0
 */
@Service
public class PublishlistProjectServiceImpl extends ServiceImpl<PublishlistProjectMapper, PublishlistProject> implements IPublishlistProjectService {

	@Autowired
	private PublishlistProjectMapper publishlistProjectMapper;

	@Override
	public List<PublishlistProject> selectByMainId(String mainId) {
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("publishlist_id",mainId);
		return publishlistProjectMapper.selectByMap(queryMap);
		//return publishlistProjectMapper.selectByMainId(mainId);
	}
}
