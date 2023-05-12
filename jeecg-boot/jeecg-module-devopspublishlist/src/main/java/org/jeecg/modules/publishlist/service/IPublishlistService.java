package org.jeecg.modules.publishlist.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.jeecg.modules.publishlist.entity.DependentComponent;
import org.jeecg.modules.publishlist.entity.PackageUrl;
import org.jeecg.modules.publishlist.entity.PublishlistProject;
import org.jeecg.modules.publishlist.entity.Publishlist;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.publishlist.vo.PublishlistQueryResult;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 发布单
 * @Author: jeecg-boot
 * @Date:   2023-04-17
 * @Version: V1.0
 */
public interface IPublishlistService extends IService<Publishlist> {

	/**
	 * 添加一对多
	 *
	 * @param publishlist
	 * @param publishlistProjectList
	 * @param dependentComponentList
	 * @param packageUrlList
	 */
	public void saveMain(Publishlist publishlist, List<PublishlistProject> publishlistProjectList, List<DependentComponent> dependentComponentList, List<PackageUrl> packageUrlList) ;
	
	/**
	 * 修改一对多
	 *
   * @param publishlist
   * @param publishlistProjectList
   * @param dependentComponentList
   * @param packageUrlList
	 */
	public void updateMain(Publishlist publishlist,List<PublishlistProject> publishlistProjectList,List<DependentComponent> dependentComponentList,List<PackageUrl> packageUrlList);
	
	/**
	 * 删除一对多
	 *
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);

	public PublishlistQueryResult queryByMainId(String id);


	public List<PublishlistQueryResult> listByMainMap(Map<String, Object> columnMap);

	public List<PublishlistQueryResult> ListByMainWrapper(Wrapper queryWrapper);
	
}
