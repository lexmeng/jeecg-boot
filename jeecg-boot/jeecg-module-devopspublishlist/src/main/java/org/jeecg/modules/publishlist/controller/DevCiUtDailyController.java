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

import org.jeecg.modules.publishlist.entity.DevCiUtDaily;
import org.jeecg.modules.publishlist.service.IDevCiUtDailyService;
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
 * @Description: ci ut结果表
 * @Author: jeecg-boot
 * @Date:   2023-06-08
 * @Version: V1.0
 */
@Api(tags="ci ut结果表")
@RestController
@RequestMapping("/dev/devCiUtDaily")
@Slf4j
public class DevCiUtDailyController extends JeecgController<DevCiUtDaily, IDevCiUtDailyService> {
	@Autowired
	private IDevCiUtDailyService devCiUtDailyService;
	
	/**
	 * 分页列表查询
	 *
	 * @param devCiUtDaily
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "ci ut结果表-分页列表查询")
	@ApiOperation(value="ci ut结果表-分页列表查询", notes="ci ut结果表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<DevCiUtDaily>> queryPageList(DevCiUtDaily devCiUtDaily,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DevCiUtDaily> queryWrapper = QueryGenerator.initQueryWrapper(devCiUtDaily, req.getParameterMap());
		Page<DevCiUtDaily> page = new Page<DevCiUtDaily>(pageNo, pageSize);
		IPage<DevCiUtDaily> pageList = devCiUtDailyService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param devCiUtDaily
	 * @return
	 */
	@AutoLog(value = "ci ut结果表-添加")
	@ApiOperation(value="ci ut结果表-添加", notes="ci ut结果表-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:dev_ci_ut_daily:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody DevCiUtDaily devCiUtDaily) {
		devCiUtDailyService.save(devCiUtDaily);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param devCiUtDaily
	 * @return
	 */
	@AutoLog(value = "ci ut结果表-编辑")
	@ApiOperation(value="ci ut结果表-编辑", notes="ci ut结果表-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:dev_ci_ut_daily:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody DevCiUtDaily devCiUtDaily) {
		devCiUtDailyService.updateById(devCiUtDaily);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "ci ut结果表-通过id删除")
	@ApiOperation(value="ci ut结果表-通过id删除", notes="ci ut结果表-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:dev_ci_ut_daily:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		devCiUtDailyService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "ci ut结果表-批量删除")
	@ApiOperation(value="ci ut结果表-批量删除", notes="ci ut结果表-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:dev_ci_ut_daily:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.devCiUtDailyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "ci ut结果表-通过id查询")
	@ApiOperation(value="ci ut结果表-通过id查询", notes="ci ut结果表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<DevCiUtDaily> queryById(@RequestParam(name="id",required=true) String id) {
		DevCiUtDaily devCiUtDaily = devCiUtDailyService.getById(id);
		if(devCiUtDaily==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(devCiUtDaily);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param devCiUtDaily
    */
    //@RequiresPermissions("org.jeecg.modules.demo:dev_ci_ut_daily:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DevCiUtDaily devCiUtDaily) {
        return super.exportXls(request, devCiUtDaily, DevCiUtDaily.class, "ci ut结果表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("dev_ci_ut_daily:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, DevCiUtDaily.class);
    }

}
