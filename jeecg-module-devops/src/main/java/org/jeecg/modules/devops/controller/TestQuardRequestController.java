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

import org.jeecg.modules.devops.entity.TestQuardRequest;
import org.jeecg.modules.devops.service.ITestQuardRequestService;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: quard job请求表
 * @Author: jeecg-boot
 * @Date:   2023-06-02
 * @Version: V1.0
 */
@Api(tags="quard job请求表")
@RestController
@RequestMapping("/test/testQuardRequest")
@Slf4j
public class TestQuardRequestController extends JeecgController<TestQuardRequest, ITestQuardRequestService> {
	@Autowired
	private ITestQuardRequestService testQuardRequestService;

	/**
	 * 分页列表查询
	 *
	 * @param testQuardRequest
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "quard job请求表-分页列表查询")
	@ApiOperation(value="quard job请求表-分页列表查询", notes="quard job请求表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TestQuardRequest>> queryPageList(TestQuardRequest testQuardRequest,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TestQuardRequest> queryWrapper = QueryGenerator.initQueryWrapper(testQuardRequest, req.getParameterMap());
		Page<TestQuardRequest> page = new Page<TestQuardRequest>(pageNo, pageSize);
		IPage<TestQuardRequest> pageList = testQuardRequestService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param testQuardRequest
	 * @return
	 */
	@AutoLog(value = "quard job请求表-添加")
	@ApiOperation(value="quard job请求表-添加", notes="quard job请求表-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:test_quard_request:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody TestQuardRequest testQuardRequest) {
		testQuardRequestService.save(testQuardRequest);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param testQuardRequest
	 * @return
	 */
	@AutoLog(value = "quard job请求表-编辑")
	@ApiOperation(value="quard job请求表-编辑", notes="quard job请求表-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:test_quard_request:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody TestQuardRequest testQuardRequest) {
		testQuardRequestService.updateById(testQuardRequest);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "quard job请求表-通过id删除")
	@ApiOperation(value="quard job请求表-通过id删除", notes="quard job请求表-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:test_quard_request:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		testQuardRequestService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "quard job请求表-批量删除")
	@ApiOperation(value="quard job请求表-批量删除", notes="quard job请求表-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:test_quard_request:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.testQuardRequestService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "quard job请求表-通过id查询")
	@ApiOperation(value="quard job请求表-通过id查询", notes="quard job请求表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<TestQuardRequest> queryById(@RequestParam(name="id",required=true) String id) {
		TestQuardRequest testQuardRequest = testQuardRequestService.getById(id);
		if(testQuardRequest==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(testQuardRequest);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param testQuardRequest
    */
    //@RequiresPermissions("org.jeecg.modules.demo:test_quard_request:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TestQuardRequest testQuardRequest) {
        return super.exportXls(request, testQuardRequest, TestQuardRequest.class, "quard job请求表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("test_quard_request:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TestQuardRequest.class);
    }

}
