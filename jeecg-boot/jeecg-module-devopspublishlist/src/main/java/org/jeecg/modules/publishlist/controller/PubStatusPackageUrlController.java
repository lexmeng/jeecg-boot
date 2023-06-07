package org.jeecg.modules.publishlist.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.publishlist.entity.PubStatusPackageUrl;
import org.jeecg.modules.publishlist.service.IPubStatusPackageUrlService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 发布单不同状态的package url
 * @Author: jeecg-boot
 * @Date:   2023-06-07
 * @Version: V1.0
 */
@Api(tags="发布单不同状态的package url")
@RestController
@RequestMapping("/release/pubStatusPackageUrl")
@Slf4j
public class PubStatusPackageUrlController extends JeecgController<PubStatusPackageUrl, IPubStatusPackageUrlService> {
	@Autowired
	private IPubStatusPackageUrlService pubStatusPackageUrlService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pubStatusPackageUrl
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "发布单不同状态的package url-分页列表查询")
	@ApiOperation(value="发布单不同状态的package url-分页列表查询", notes="发布单不同状态的package url-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<PubStatusPackageUrl>> queryPageList(PubStatusPackageUrl pubStatusPackageUrl,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PubStatusPackageUrl> queryWrapper = QueryGenerator.initQueryWrapper(pubStatusPackageUrl, req.getParameterMap());
		Page<PubStatusPackageUrl> page = new Page<PubStatusPackageUrl>(pageNo, pageSize);
		IPage<PubStatusPackageUrl> pageList = pubStatusPackageUrlService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param pubStatusPackageUrl
	 * @return
	 */
	@AutoLog(value = "发布单不同状态的package url-添加")
	@ApiOperation(value="发布单不同状态的package url-添加", notes="发布单不同状态的package url-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:pub_status_package_url:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody PubStatusPackageUrl pubStatusPackageUrl) {
		pubStatusPackageUrlService.save(pubStatusPackageUrl);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pubStatusPackageUrl
	 * @return
	 */
	@AutoLog(value = "发布单不同状态的package url-编辑")
	@ApiOperation(value="发布单不同状态的package url-编辑", notes="发布单不同状态的package url-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:pub_status_package_url:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody PubStatusPackageUrl pubStatusPackageUrl) {
		pubStatusPackageUrlService.updateById(pubStatusPackageUrl);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "发布单不同状态的package url-通过id删除")
	@ApiOperation(value="发布单不同状态的package url-通过id删除", notes="发布单不同状态的package url-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:pub_status_package_url:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		pubStatusPackageUrlService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "发布单不同状态的package url-批量删除")
	@ApiOperation(value="发布单不同状态的package url-批量删除", notes="发布单不同状态的package url-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:pub_status_package_url:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.pubStatusPackageUrlService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "发布单不同状态的package url-通过id查询")
	@ApiOperation(value="发布单不同状态的package url-通过id查询", notes="发布单不同状态的package url-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<PubStatusPackageUrl> queryById(@RequestParam(name="id",required=true) String id) {
		PubStatusPackageUrl pubStatusPackageUrl = pubStatusPackageUrlService.getById(id);
		if(pubStatusPackageUrl==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(pubStatusPackageUrl);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pubStatusPackageUrl
    */
    //@RequiresPermissions("org.jeecg.modules.demo:pub_status_package_url:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PubStatusPackageUrl pubStatusPackageUrl) {
        return super.exportXls(request, pubStatusPackageUrl, PubStatusPackageUrl.class, "发布单不同状态的package url");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("pub_status_package_url:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, PubStatusPackageUrl.class);
    }

}
