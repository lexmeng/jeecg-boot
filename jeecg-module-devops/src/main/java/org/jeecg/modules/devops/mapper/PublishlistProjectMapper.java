package org.jeecg.modules.devops.mapper;

import java.util.List;
import org.jeecg.modules.devops.entity.PublishlistProject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 发布单项目表
 * @Author: jeecg-boot
 * @Date:   2023-04-17
 * @Version: V1.0
 */
public interface PublishlistProjectMapper extends BaseMapper<PublishlistProject> {

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
   * @return List<PublishlistProject>
   */
	public List<PublishlistProject> selectByMainId(@Param("mainId") String mainId);
}
