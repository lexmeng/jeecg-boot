package org.jeecg.modules.devops.service;

import org.jeecg.modules.devops.entity.PublishlistProject;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 发布单项目表
 * @Author: jeecg-boot
 * @Date:   2023-04-17
 * @Version: V1.0
 */
public interface IPublishlistProjectService extends IService<PublishlistProject> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<PublishlistProject>
	 */
	public List<PublishlistProject> selectByMainId(String mainId);
}
