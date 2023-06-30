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

import org.jeecg.modules.devops.bpservice.DevPrCommitBPService;
import org.jeecg.modules.devops.entity.DevCiUtPr;
import org.jeecg.modules.devops.entity.DevPrCommit;
import org.jeecg.modules.devops.service.IDevCiUtPrService;
import org.jeecg.modules.devops.service.IDevPrCommitService;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: pr的ci ut结果表
 * @Author: jeecg-boot
 * @Date:   2023-06-08
 * @Version: V1.0
 */
@Api(tags="pr的ci ut结果表")
@RestController
@RequestMapping("/dev/devCiUtPr")
@Slf4j
public class DevCiUtPrController extends JeecgController<DevCiUtPr, IDevCiUtPrService> {
	@Autowired
	private IDevCiUtPrService devCiUtPrService;

	@Autowired
	private DevPrCommitBPService devPrCommitBPService;

	@Autowired
	private IDevPrCommitService devPrCommitService;

	/**
	 * 分页列表查询
	 *
	 * @param devCiUtPr
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "pr的ci ut结果表-分页列表查询")
	@ApiOperation(value="pr的ci ut结果表-分页列表查询", notes="pr的ci ut结果表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<DevCiUtPr>> queryPageList(DevCiUtPr devCiUtPr,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DevCiUtPr> queryWrapper = QueryGenerator.initQueryWrapper(devCiUtPr, req.getParameterMap());
		Page<DevCiUtPr> page = new Page<DevCiUtPr>(pageNo, pageSize);
		IPage<DevCiUtPr> pageList = devCiUtPrService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 *   保存一条pr ut记录，则记录下当时的pr信息。
	 *   TODO: 用户merge代码的时候，也需要添加当时的PR信息
	 * @param devCiUtPr
	 * @return
	 */
	@AutoLog(value = "pr的ci ut结果表-添加")
	@ApiOperation(value="pr的ci ut结果表-添加", notes="pr的ci ut结果表-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:dev_ci_ut_pr:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody DevCiUtPr devCiUtPr) {
		devCiUtPrService.save(devCiUtPr);
		DevPrCommit devPrCommit = devPrCommitBPService.getCommitWhenPrUt(devCiUtPr);
		devPrCommitService.save(devPrCommit);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param devCiUtPr
	 * @return
	 */
	@AutoLog(value = "pr的ci ut结果表-编辑")
	@ApiOperation(value="pr的ci ut结果表-编辑", notes="pr的ci ut结果表-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:dev_ci_ut_pr:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody DevCiUtPr devCiUtPr) {
		devCiUtPrService.updateById(devCiUtPr);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "pr的ci ut结果表-通过id删除")
	@ApiOperation(value="pr的ci ut结果表-通过id删除", notes="pr的ci ut结果表-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:dev_ci_ut_pr:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		devCiUtPrService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "pr的ci ut结果表-批量删除")
	@ApiOperation(value="pr的ci ut结果表-批量删除", notes="pr的ci ut结果表-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:dev_ci_ut_pr:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.devCiUtPrService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "pr的ci ut结果表-通过id查询")
	@ApiOperation(value="pr的ci ut结果表-通过id查询", notes="pr的ci ut结果表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<DevCiUtPr> queryById(@RequestParam(name="id",required=true) String id) {
		DevCiUtPr devCiUtPr = devCiUtPrService.getById(id);
		if(devCiUtPr==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(devCiUtPr);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param devCiUtPr
    */
    //@RequiresPermissions("org.jeecg.modules.demo:dev_ci_ut_pr:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DevCiUtPr devCiUtPr) {
        return super.exportXls(request, devCiUtPr, DevCiUtPr.class, "pr的ci ut结果表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("dev_ci_ut_pr:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, DevCiUtPr.class);
    }

}
