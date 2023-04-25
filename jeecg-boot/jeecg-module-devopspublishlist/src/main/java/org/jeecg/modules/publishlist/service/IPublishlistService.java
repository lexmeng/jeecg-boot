package org.jeecg.modules.publishlist.service;

import org.jeecg.modules.publishlist.entity.PublishlistProject;
import org.jeecg.modules.publishlist.entity.Publishlist;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

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
	 */
	public void saveMain(Publishlist publishlist,List<PublishlistProject> publishlistProjectList) ;
	
	/**
	 * 修改一对多
	 *
   * @param publishlist
   * @param publishlistProjectList
	 */
	public void updateMain(Publishlist publishlist,List<PublishlistProject> publishlistProjectList);
	
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
	
}
