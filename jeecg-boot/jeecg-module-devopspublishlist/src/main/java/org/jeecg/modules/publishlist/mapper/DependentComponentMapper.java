package org.jeecg.modules.publishlist.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.publishlist.entity.DependentComponent;

/**
 * @Description: 依赖组件
 * @Author: jeecg-boot
 * @Date:   2023-05-06
 * @Version: V1.0
 */
public interface DependentComponentMapper extends BaseMapper<DependentComponent> {

	/**
	 * 通过主表id删除子表数据
	 *
	 * @param mainId 主表id
	 * @return boolean
	 */
	public boolean deleteByMainId(@Param("mainId") String mainId);

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId 主表id
   * @return List<DependentComponent>
   */
	public List<DependentComponent> selectByMainId(@Param("mainId") String mainId);
}
