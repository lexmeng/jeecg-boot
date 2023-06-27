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

import org.jeecg.modules.publishlist.entity.TestQuardJob;
import org.jeecg.modules.publishlist.service.ITestQuardJobService;
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
 * @Description: quard 发起任务
 * @Author: jeecg-boot
 * @Date:   2023-06-24
 * @Version: V1.0
 */
@Api(tags="quard 发起任务")
@RestController
@RequestMapping("/test/testQuardJob")
@Slf4j
public class TestQuardJobController extends JeecgController<TestQuardJob, ITestQuardJobService> {
	@Autowired
	private ITestQuardJobService testQuardJobService;
	
	/**
	 * 分页列表查询
	 *
	 * @param testQuardJob
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "quard 发起任务-分页列表查询")
	@ApiOperation(value="quard 发起任务-分页列表查询", notes="quard 发起任务-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TestQuardJob>> queryPageList(TestQuardJob testQuardJob,
                                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                     HttpServletRequest req) {
		QueryWrapper<TestQuardJob> queryWrapper = QueryGenerator.initQueryWrapper(testQuardJob, req.getParameterMap());
		Page<TestQuardJob> page = new Page<TestQuardJob>(pageNo, pageSize);
		IPage<TestQuardJob> pageList = testQuardJobService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param testQuardJob
	 * @return
	 */
	@AutoLog(value = "quard 发起任务-添加")
	@ApiOperation(value="quard 发起任务-添加", notes="quard 发起任务-添加")
	//@RequiresPermissions(":test_quard_job:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody TestQuardJob testQuardJob) {
		testQuardJobService.save(testQuardJob);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param testQuardJob
	 * @return
	 */
	@AutoLog(value = "quard 发起任务-编辑")
	@ApiOperation(value="quard 发起任务-编辑", notes="quard 发起任务-编辑")
	//@RequiresPermissions(":test_quard_job:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody TestQuardJob testQuardJob) {
		testQuardJobService.updateById(testQuardJob);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "quard 发起任务-通过id删除")
	@ApiOperation(value="quard 发起任务-通过id删除", notes="quard 发起任务-通过id删除")
	//@RequiresPermissions(":test_quard_job:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		testQuardJobService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "quard 发起任务-批量删除")
	@ApiOperation(value="quard 发起任务-批量删除", notes="quard 发起任务-批量删除")
	//@RequiresPermissions(":test_quard_job:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.testQuardJobService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "quard 发起任务-通过id查询")
	@ApiOperation(value="quard 发起任务-通过id查询", notes="quard 发起任务-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<TestQuardJob> queryById(@RequestParam(name="id",required=true) String id) {
		TestQuardJob testQuardJob = testQuardJobService.getById(id);
		if(testQuardJob==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(testQuardJob);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param testQuardJob
    */
    //@RequiresPermissions(":test_quard_job:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TestQuardJob testQuardJob) {
        return super.exportXls(request, testQuardJob, TestQuardJob.class, "quard 发起任务");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("test_quard_job:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TestQuardJob.class);
    }

}
