package org.jeecg.modules.publishlist.service.impl;

import org.jeecg.modules.publishlist.entity.Publishlist;
import org.jeecg.modules.publishlist.entity.PublishlistProject;
import org.jeecg.modules.publishlist.mapper.PublishlistProjectMapper;
import org.jeecg.modules.publishlist.mapper.PublishlistMapper;
import org.jeecg.modules.publishlist.service.IPublishlistService;
import org.jeecg.modules.publishlist.domainservice.impl.PublishlistStatusMachine;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;


/**
 * @Description: 发布单
 * @Author: jeecg-boot
 * @Date:   2023-04-17
 * @Version: V1.0
 */
@Service
public class PublishlistServiceImpl extends ServiceImpl<PublishlistMapper, Publishlist> implements IPublishlistService {

	@Autowired
	private PublishlistMapper publishlistMapper;
	@Autowired
	private PublishlistProjectMapper publishlistProjectMapper;

	@Autowired
	private PublishlistStatusMachine publishlistStatusMachine;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(Publishlist publishlist, List<PublishlistProject> publishlistProjectList) {
		publishlist.setPublishlistStage(publishlistStatusMachine.init());

		publishlistMapper.insert(publishlist);
		if(publishlistProjectList!=null && publishlistProjectList.size()>0) {
			for(PublishlistProject entity:publishlistProjectList) {
				//外键设置
				entity.setPublishlistId(publishlist.getId());
				publishlistProjectMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(Publishlist publishlist,List<PublishlistProject> publishlistProjectList) {
		publishlistMapper.updateById(publishlist);
		
		//1.先删除子表数据
		publishlistProjectMapper.deleteByMainId(publishlist.getId());
		
		//2.子表数据重新插入
		if(publishlistProjectList!=null && publishlistProjectList.size()>0) {
			for(PublishlistProject entity:publishlistProjectList) {
				//外键设置
				entity.setPublishlistId(publishlist.getId());
				publishlistProjectMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		publishlistProjectMapper.deleteByMainId(id);
		publishlistMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			publishlistProjectMapper.deleteByMainId(id.toString());
			publishlistMapper.deleteById(id);
		}
	}
	
}
