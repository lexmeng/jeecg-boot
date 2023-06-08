package org.jeecg.modules.publishlist.controller;


import java.util.Arrays;
import java.util.HashMap;
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

import org.jeecg.modules.publishlist.entity.TestStepStageData;
import org.jeecg.modules.publishlist.service.ITestStepStageDataService;
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
 * @Description: 雷神测试过程数据
 * @Author: jeecg-boot
 * @Date:   2023-06-08
 * @Version: V1.0
 */
@Api(tags="雷神测试过程数据")
@RestController
@RequestMapping("/test/testStepStageData")
@Slf4j
public class TestStepStageDataController extends JeecgController<TestStepStageData, ITestStepStageDataService> {
	@Autowired
	private ITestStepStageDataService testStepStageDataService;
	
	/**
	 * 分页列表查询
	 *
	 * @param testStepStageData
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "雷神测试过程数据-分页列表查询")
	@ApiOperation(value="雷神测试过程数据-分页列表查询", notes="雷神测试过程数据-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TestStepStageData>> queryPageList(TestStepStageData testStepStageData,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TestStepStageData> queryWrapper = QueryGenerator.initQueryWrapper(testStepStageData, req.getParameterMap());
		Page<TestStepStageData> page = new Page<TestStepStageData>(pageNo, pageSize);
		IPage<TestStepStageData> pageList = testStepStageDataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param testStepStageData
	 * @return
	 */
	@AutoLog(value = "雷神测试过程数据-添加")
	@ApiOperation(value="雷神测试过程数据-添加", notes="雷神测试过程数据-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:test_step_stage_data:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody TestStepStageData testStepStageData) {
		testStepStageDataService.save(testStepStageData);
		return Result.OK("添加成功！");
	}

	 @AutoLog(value = "雷神测试过程数据-添加阶段结束时间")
	 @ApiOperation(value="雷神测试过程数据-添加阶段结束时间", notes="雷神测试过程数据-添加阶段结束时间")
	 //@RequiresPermissions("org.jeecg.modules.demo:test_step_stage_data:add")
	 @PostMapping(value = "/updateStageEndTime")
	 public Result<String> updateStageEndTime(@RequestBody TestStepStageData testStepStageDataReq) {
		 Map<String,Object> queryMap = new HashMap<>();
		 queryMap.put("jenkins_job_num",testStepStageDataReq.getJenkinsJobNum());
		 queryMap.put("test_type",testStepStageDataReq.getTestType());
		 queryMap.put("platform_name",testStepStageDataReq.getPlatformName());
		 queryMap.put("test_stage",testStepStageDataReq.getTestStage());
		 List<TestStepStageData> testStepStageDataList = testStepStageDataService.listByMap(queryMap);
		 if(testStepStageDataList==null){
			 return Result.error("未找到对应数据");
		 }

		 if(testStepStageDataList.size() > 1) {
			 return Result.error("数据有误，相同维度数据有多于1行");
		 }

		 TestStepStageData testStepStageData = testStepStageDataList.get(0);
		 testStepStageData.setEndTime(testStepStageDataReq.getEndTime());
		 testStepStageData.setUpdateTime(testStepStageDataReq.getUpdateTime());
		 testStepStageData.setUpdateBy(testStepStageDataReq.getUpdateBy());
		 testStepStageDataService.updateById(testStepStageData);

		 return Result.OK("更新成功！");
	 }

	
	/**
	 *  编辑
	 *
	 * @param testStepStageData
	 * @return
	 */
	@AutoLog(value = "雷神测试过程数据-编辑")
	@ApiOperation(value="雷神测试过程数据-编辑", notes="雷神测试过程数据-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:test_step_stage_data:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody TestStepStageData testStepStageData) {
		testStepStageDataService.updateById(testStepStageData);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "雷神测试过程数据-通过id删除")
	@ApiOperation(value="雷神测试过程数据-通过id删除", notes="雷神测试过程数据-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:test_step_stage_data:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		testStepStageDataService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "雷神测试过程数据-批量删除")
	@ApiOperation(value="雷神测试过程数据-批量删除", notes="雷神测试过程数据-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:test_step_stage_data:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.testStepStageDataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "雷神测试过程数据-通过id查询")
	@ApiOperation(value="雷神测试过程数据-通过id查询", notes="雷神测试过程数据-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<TestStepStageData> queryById(@RequestParam(name="id",required=true) String id) {
		TestStepStageData testStepStageData = testStepStageDataService.getById(id);
		if(testStepStageData==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(testStepStageData);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param testStepStageData
    */
    //@RequiresPermissions("org.jeecg.modules.demo:test_step_stage_data:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TestStepStageData testStepStageData) {
        return super.exportXls(request, testStepStageData, TestStepStageData.class, "雷神测试过程数据");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("test_step_stage_data:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TestStepStageData.class);
    }

}
