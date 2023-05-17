package org.jeecg.modules.publishlist.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.jeecg.modules.publishlist.entity.DependentComponent;
import org.jeecg.modules.publishlist.entity.PackageUrl;
import org.jeecg.modules.publishlist.entity.Publishlist;
import org.jeecg.modules.publishlist.entity.PublishlistProject;
import org.jeecg.modules.publishlist.exception.BussinessException;
import org.jeecg.modules.publishlist.mapper.DependentComponentMapper;
import org.jeecg.modules.publishlist.mapper.PackageUrlMapper;
import org.jeecg.modules.publishlist.mapper.PublishlistProjectMapper;
import org.jeecg.modules.publishlist.mapper.PublishlistMapper;
import org.jeecg.modules.publishlist.service.IPublishlistService;
import org.jeecg.modules.publishlist.domainservice.impl.PublishlistStatusMachine;
import org.jeecg.modules.publishlist.vo.PublishlistQueryResult;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.*;


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
	private DependentComponentMapper dependentComponentMapper;
	@Autowired
	private PackageUrlMapper packageUrlMapper;
	@Autowired
	private PublishlistStatusMachine publishlistStatusMachine;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(Publishlist publishlist, List<PublishlistProject> publishlistProjectList,List<DependentComponent> dependentComponentList,List<PackageUrl> packageUrlList) {
		//如果产品线、产品、jira版本有相同的发布单，则返回
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("product_line_name",publishlist.getProductLineName());
		queryMap.put("product_name",publishlist.getProductName());
		queryMap.put("jira_version_name", publishlist.getJiraVersionName());
		List<Publishlist> publishlistQueryResult = publishlistMapper.selectByMap(queryMap);

		if(!publishlistQueryResult.isEmpty()){
            throw new BussinessException("已经存在相同产品线名、产品名和jira版本名的发布单，请先删除再新建！");
		}

		publishlist.setPublishlistStage(publishlistStatusMachine.init());

		publishlistMapper.insert(publishlist);
		if(publishlistProjectList!=null && publishlistProjectList.size()>0) {
			for(PublishlistProject entity:publishlistProjectList) {
				//外键设置
				entity.setPublishlistId(publishlist.getId());
				publishlistProjectMapper.insert(entity);
			}
		}
		if(dependentComponentList!=null && dependentComponentList.size()>0) {
			for(DependentComponent entity:dependentComponentList) {
				//外键设置
				entity.setPublishlistId(publishlist.getId());
				dependentComponentMapper.insert(entity);
			}
		}
		if(packageUrlList!=null && packageUrlList.size()>0) {
			for(PackageUrl entity:packageUrlList) {
				//外键设置
				entity.setPublishlistId(publishlist.getId());
				packageUrlMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(Publishlist publishlist, List<PublishlistProject> publishlistProjectList, List<DependentComponent> dependentComponentList, List<PackageUrl> packageUrlList) {
		publishlistMapper.updateById(publishlist);
		
		//1.先删除子表数据
		publishlistProjectMapper.deleteByMainId(publishlist.getId());
		dependentComponentMapper.deleteByMainId(publishlist.getId());
		packageUrlMapper.deleteByMainId(publishlist.getId());
		
		//2.子表数据重新插入
		if(publishlistProjectList!=null && publishlistProjectList.size()>0) {
			for(PublishlistProject entity:publishlistProjectList) {
				//外键设置
				entity.setPublishlistId(publishlist.getId());
				publishlistProjectMapper.insert(entity);
			}
		}
		if(dependentComponentList!=null && dependentComponentList.size()>0) {
			for(DependentComponent entity:dependentComponentList) {
				//外键设置
				entity.setPublishlistId(publishlist.getId());
				dependentComponentMapper.insert(entity);
			}
		}
		if(packageUrlList!=null && packageUrlList.size()>0) {
			for(PackageUrl entity:packageUrlList) {
				//外键设置
				entity.setPublishlistId(publishlist.getId());
				packageUrlMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public PublishlistQueryResult queryByMainId(String id){
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("publishlist_id",id);

		Publishlist publishlist = publishlistMapper.selectById(id);
		List<PackageUrl> packageUrlList = packageUrlMapper.selectByMap(queryMap);
		List<DependentComponent> dependentComponentList = dependentComponentMapper.selectByMap(queryMap);
		List<PublishlistProject> publishlistProjectList = publishlistProjectMapper.selectByMap(queryMap);

		PublishlistQueryResult result = new PublishlistQueryResult();
		result.setPublishlist(publishlist);
		result.setPackageUrlList(packageUrlList);
		result.setDependentComponentList(dependentComponentList);
		result.setPublishlistProjectList(publishlistProjectList);

        return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<PublishlistQueryResult> listByMainMap(Map<String, Object> columnMap){
        List<Publishlist> publishlistList = publishlistMapper.selectByMap(columnMap);

		List<PublishlistQueryResult> result = new ArrayList<>();
		for(Publishlist publishlist : publishlistList){
			PublishlistQueryResult publishlistQueryResult = queryByMainId(publishlist.getId());
			result.add(publishlistQueryResult);
		}
		return result;
	}

	@Override
	public List<PublishlistQueryResult> ListByMainWrapper(Wrapper queryWrapper){
		List<Publishlist> publishlistList = list(queryWrapper);

		List<PublishlistQueryResult> result = new ArrayList<>();
		for(Publishlist publishlist : publishlistList){
			PublishlistQueryResult publishlistQueryResult = queryByMainId(publishlist.getId());
			result.add(publishlistQueryResult);
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		publishlistProjectMapper.deleteByMainId(id);
		dependentComponentMapper.deleteByMainId(id);
		packageUrlMapper.deleteByMainId(id);
		publishlistMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			publishlistProjectMapper.deleteByMainId(id.toString());
			dependentComponentMapper.deleteByMainId(id.toString());
			packageUrlMapper.deleteByMainId(id.toString());
			publishlistMapper.deleteById(id);
		}
	}
	
}
