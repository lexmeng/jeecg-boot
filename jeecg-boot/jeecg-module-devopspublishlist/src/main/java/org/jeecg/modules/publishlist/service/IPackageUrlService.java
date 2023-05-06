package org.jeecg.modules.publishlist.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.publishlist.entity.PackageUrl;

import java.util.List;

/**
 * @Description: 产品包的下载地址
 * @Author: jeecg-boot
 * @Date:   2023-05-06
 * @Version: V1.0
 */
public interface IPackageUrlService extends IService<PackageUrl> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<PackageUrl>
	 */
	public List<PackageUrl> selectByMainId(String mainId);
}
