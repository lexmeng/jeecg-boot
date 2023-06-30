package org.jeecg.modules.devops.service.impl;


import org.jeecg.modules.devops.entity.DependentComponent;
import org.jeecg.modules.devops.mapper.DependentComponentMapper;
import org.jeecg.modules.devops.service.IDependentComponentService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 依赖组件
 * @Author: jeecg-boot
 * @Date:   2023-05-06
 * @Version: V1.0
 */
@Service
public class DependentComponentServiceImpl extends ServiceImpl<DependentComponentMapper, DependentComponent> implements IDependentComponentService {

	@Autowired
	private DependentComponentMapper dependentComponentMapper;

	@Override
	public List<DependentComponent> selectByMainId(String mainId) {
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("publishlist_id",mainId);
		return dependentComponentMapper.selectByMap(queryMap);
		//return dependentComponentMapper.selectByMainId(mainId);
	}
}
