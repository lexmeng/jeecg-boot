package org.jeecg.modules.publishlist.service.impl;


import org.jeecg.modules.publishlist.entity.PackageUrl;
import org.jeecg.modules.publishlist.mapper.PackageUrlMapper;
import org.jeecg.modules.publishlist.service.IPackageUrlService;
import org.springframework.stereotype.Service;
import java.util.List;
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
		return packageUrlMapper.selectByMainId(mainId);
	}
}
