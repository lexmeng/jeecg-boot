package org.jeecg.modules.devops.controller;


import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.devops.entity.TestStepResult;
import org.jeecg.modules.devops.service.ITestStepResultService;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 雷神测试结果数据
 * @Author: jeecg-boot
 * @Date:   2023-06-08
 * @Version: V1.0
 */
@Api(tags="雷神测试结果数据")
@RestController
@RequestMapping("/test/testStepResult")
@Slf4j
public class TestStepResultController extends JeecgController<TestStepResult, ITestStepResultService> {
	@Autowired
	private ITestStepResultService testStepResultService;

	/**
	 * 分页列表查询
	 *
	 * @param testStepResult
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "雷神测试结果数据-分页列表查询")
	@ApiOperation(value="雷神测试结果数据-分页列表查询", notes="雷神测试结果数据-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TestStepResult>> queryPageList(TestStepResult testStepResult,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TestStepResult> queryWrapper = QueryGenerator.initQueryWrapper(testStepResult, req.getParameterMap());
		Page<TestStepResult> page = new Page<TestStepResult>(pageNo, pageSize);
		IPage<TestStepResult> pageList = testStepResultService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param testStepResult
	 * @return
	 */
	@AutoLog(value = "雷神测试结果数据-添加")
	@ApiOperation(value="雷神测试结果数据-添加", notes="雷神测试结果数据-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:test_step_result:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody TestStepResult testStepResult) {
		testStepResultService.save(testStepResult);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param testStepResult
	 * @return
	 */
	@AutoLog(value = "雷神测试结果数据-编辑")
	@ApiOperation(value="雷神测试结果数据-编辑", notes="雷神测试结果数据-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:test_step_result:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody TestStepResult testStepResult) {
		testStepResultService.updateById(testStepResult);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "雷神测试结果数据-通过id删除")
	@ApiOperation(value="雷神测试结果数据-通过id删除", notes="雷神测试结果数据-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:test_step_result:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		testStepResultService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "雷神测试结果数据-批量删除")
	@ApiOperation(value="雷神测试结果数据-批量删除", notes="雷神测试结果数据-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:test_step_result:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.testStepResultService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "雷神测试结果数据-通过id查询")
	@ApiOperation(value="雷神测试结果数据-通过id查询", notes="雷神测试结果数据-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<TestStepResult> queryById(@RequestParam(name="id",required=true) String id) {
		TestStepResult testStepResult = testStepResultService.getById(id);
		if(testStepResult==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(testStepResult);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param testStepResult
    */
    //@RequiresPermissions("org.jeecg.modules.demo:test_step_result:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TestStepResult testStepResult) {
        return super.exportXls(request, testStepResult, TestStepResult.class, "雷神测试结果数据");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("test_step_result:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TestStepResult.class);
    }

}
