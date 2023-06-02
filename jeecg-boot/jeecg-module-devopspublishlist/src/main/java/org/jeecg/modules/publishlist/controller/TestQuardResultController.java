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

import org.jeecg.modules.publishlist.entity.TestQuardResult;
import org.jeecg.modules.publishlist.service.ITestQuardResultService;
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
 * @Description: Quard测试结果记录
 * @Author: jeecg-boot
 * @Date:   2023-06-02
 * @Version: V1.0
 */
@Api(tags="Quard测试结果记录")
@RestController
@RequestMapping("/test/testQuardResult")
@Slf4j
public class TestQuardResultController extends JeecgController<TestQuardResult, ITestQuardResultService> {
	@Autowired
	private ITestQuardResultService testQuardResultService;
	
	/**
	 * 分页列表查询
	 *
	 * @param testQuardResult
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "Quard测试结果记录-分页列表查询")
	@ApiOperation(value="Quard测试结果记录-分页列表查询", notes="Quard测试结果记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TestQuardResult>> queryPageList(TestQuardResult testQuardResult,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TestQuardResult> queryWrapper = QueryGenerator.initQueryWrapper(testQuardResult, req.getParameterMap());
		Page<TestQuardResult> page = new Page<TestQuardResult>(pageNo, pageSize);
		IPage<TestQuardResult> pageList = testQuardResultService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param testQuardResult
	 * @return
	 */
	@AutoLog(value = "Quard测试结果记录-添加")
	@ApiOperation(value="Quard测试结果记录-添加", notes="Quard测试结果记录-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:test_quard_result:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody TestQuardResult testQuardResult) {
		testQuardResultService.save(testQuardResult);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param testQuardResult
	 * @return
	 */
	@AutoLog(value = "Quard测试结果记录-编辑")
	@ApiOperation(value="Quard测试结果记录-编辑", notes="Quard测试结果记录-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:test_quard_result:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody TestQuardResult testQuardResult) {
		testQuardResultService.updateById(testQuardResult);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "Quard测试结果记录-通过id删除")
	@ApiOperation(value="Quard测试结果记录-通过id删除", notes="Quard测试结果记录-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:test_quard_result:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		testQuardResultService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "Quard测试结果记录-批量删除")
	@ApiOperation(value="Quard测试结果记录-批量删除", notes="Quard测试结果记录-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:test_quard_result:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.testQuardResultService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "Quard测试结果记录-通过id查询")
	@ApiOperation(value="Quard测试结果记录-通过id查询", notes="Quard测试结果记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<TestQuardResult> queryById(@RequestParam(name="id",required=true) String id) {
		TestQuardResult testQuardResult = testQuardResultService.getById(id);
		if(testQuardResult==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(testQuardResult);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param testQuardResult
    */
    //@RequiresPermissions("org.jeecg.modules.demo:test_quard_result:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TestQuardResult testQuardResult) {
        return super.exportXls(request, testQuardResult, TestQuardResult.class, "Quard测试结果记录");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("test_quard_result:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TestQuardResult.class);
    }

}
