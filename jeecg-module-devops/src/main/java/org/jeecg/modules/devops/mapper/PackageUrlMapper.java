package org.jeecg.modules.devops.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.devops.entity.PackageUrl;

/**
 * @Description: 产品包的下载地址
 * @Author: jeecg-boot
 * @Date:   2023-05-06
 * @Version: V1.0
 */
public interface PackageUrlMapper extends BaseMapper<PackageUrl> {

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
   * @return List<PackageUrl>
   */
	public List<PackageUrl> selectByMainId(@Param("mainId") String mainId);
}
