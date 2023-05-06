package org.jeecg.modules.publishlist.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.publishlist.entity.DependentComponent;

import java.util.List;

/**
 * @Description: 依赖组件
 * @Author: jeecg-boot
 * @Date:   2023-05-06
 * @Version: V1.0
 */
public interface IDependentComponentService extends IService<DependentComponent> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<DependentComponent>
	 */
	public List<DependentComponent> selectByMainId(String mainId);
}
