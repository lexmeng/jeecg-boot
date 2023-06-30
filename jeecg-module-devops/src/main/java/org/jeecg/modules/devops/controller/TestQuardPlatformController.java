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

import org.jeecg.modules.devops.entity.TestQuardPlatform;
import org.jeecg.modules.devops.service.ITestQuardPlatformService;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: Quard 测试平台信息
 * @Author: jeecg-boot
 * @Date:   2023-06-02
 * @Version: V1.0
 */
@Api(tags="Quard 测试平台信息")
@RestController
@RequestMapping("/test/testQuardPlatform")
@Slf4j
public class TestQuardPlatformController extends JeecgController<TestQuardPlatform, ITestQuardPlatformService> {
	@Autowired
	private ITestQuardPlatformService testQuardPlatformService;

	/**
	 * 分页列表查询
	 *
	 * @param testQuardPlatform
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "Quard 测试平台信息-分页列表查询")
	@ApiOperation(value="Quard 测试平台信息-分页列表查询", notes="Quard 测试平台信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TestQuardPlatform>> queryPageList(TestQuardPlatform testQuardPlatform,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TestQuardPlatform> queryWrapper = QueryGenerator.initQueryWrapper(testQuardPlatform, req.getParameterMap());
		Page<TestQuardPlatform> page = new Page<TestQuardPlatform>(pageNo, pageSize);
		IPage<TestQuardPlatform> pageList = testQuardPlatformService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param testQuardPlatform
	 * @return
	 */
	@AutoLog(value = "Quard 测试平台信息-添加")
	@ApiOperation(value="Quard 测试平台信息-添加", notes="Quard 测试平台信息-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:test_quard_platform:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody TestQuardPlatform testQuardPlatform) {
		testQuardPlatformService.save(testQuardPlatform);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param testQuardPlatform
	 * @return
	 */
	@AutoLog(value = "Quard 测试平台信息-编辑")
	@ApiOperation(value="Quard 测试平台信息-编辑", notes="Quard 测试平台信息-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:test_quard_platform:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody TestQuardPlatform testQuardPlatform) {
		testQuardPlatformService.updateById(testQuardPlatform);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "Quard 测试平台信息-通过id删除")
	@ApiOperation(value="Quard 测试平台信息-通过id删除", notes="Quard 测试平台信息-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:test_quard_platform:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		testQuardPlatformService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "Quard 测试平台信息-批量删除")
	@ApiOperation(value="Quard 测试平台信息-批量删除", notes="Quard 测试平台信息-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:test_quard_platform:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.testQuardPlatformService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "Quard 测试平台信息-通过id查询")
	@ApiOperation(value="Quard 测试平台信息-通过id查询", notes="Quard 测试平台信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<TestQuardPlatform> queryById(@RequestParam(name="id",required=true) String id) {
		TestQuardPlatform testQuardPlatform = testQuardPlatformService.getById(id);
		if(testQuardPlatform==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(testQuardPlatform);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param testQuardPlatform
    */
    //@RequiresPermissions("org.jeecg.modules.demo:test_quard_platform:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TestQuardPlatform testQuardPlatform) {
        return super.exportXls(request, testQuardPlatform, TestQuardPlatform.class, "Quard 测试平台信息");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("test_quard_platform:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TestQuardPlatform.class);
    }

}
