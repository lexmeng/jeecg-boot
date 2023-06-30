package org.jeecg.modules.devops.service.impl;


import org.jeecg.modules.devops.entity.PackageUrl;
import org.jeecg.modules.devops.mapper.PackageUrlMapper;
import org.jeecg.modules.devops.service.IPackageUrlService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 产品包的下载地址
 * @Author: jeecg-boot
 * @Date:   2023-05-06
 * @Version: V1.0
 */
@Service
public class PackageUrlServiceImpl extends ServiceImpl<PackageUrlMapper, PackageUrl> implements IPackageUrlService {

	@Autowired
	private PackageUrlMapper packageUrlMapper;

	@Override
	public List<PackageUrl> selectByMainId(String mainId) {
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("publishlist_id",mainId);
		return packageUrlMapper.selectByMap(queryMap);
		//return packageUrlMapper.selectByMainId(mainId);
	}
}
