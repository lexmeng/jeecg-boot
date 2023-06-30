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

import org.jeecg.modules.devops.bpservice.DevPrBPService;
import org.jeecg.modules.devops.entity.DevPr;
import org.jeecg.modules.devops.service.IDevPrService;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: pr表
 * @Author: jeecg-boot
 * @Date:   2023-06-12
 * @Version: V1.0
 */
@Api(tags="pr表")
@RestController
@RequestMapping("/dev/devPr")
@Slf4j
public class DevPrController extends JeecgController<DevPr, IDevPrService> {
	@Autowired
	private IDevPrService devPrService;

	@Autowired
	private DevPrBPService devPrBPService;

	/**
	 * 分页列表查询
	 *
	 * @param devPr
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "pr表-分页列表查询")
	@ApiOperation(value="pr表-分页列表查询", notes="pr表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<DevPr>> queryPageList(DevPr devPr,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DevPr> queryWrapper = QueryGenerator.initQueryWrapper(devPr, req.getParameterMap());
		Page<DevPr> page = new Page<DevPr>(pageNo, pageSize);
		IPage<DevPr> pageList = devPrService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param devPr
	 * @return
	 */
	@AutoLog(value = "pr表-添加")
	@ApiOperation(value="pr表-添加", notes="pr表-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:dev_pr:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody DevPr devPr) {
		devPrService.save(devPr);
		return Result.OK("添加成功！");
	}

	 @AutoLog(value = "根据projectlistId更新")
	 @ApiOperation(value="根据projectlistId更新", notes="根据projectlistId更新")
	 //@RequiresPermissions("org.jeecg.modules.demo:dev_pr:add")
	 @PostMapping(value = "/updateByProjectlistId")
	 public Result<String> updateByProjectlistId(@RequestParam(name="publishlistId",required=true) String publishlistId) {
		devPrBPService.updateByProjectlistId(publishlistId);
		return Result.OK("更新成功！");
	 }



	/**
	 *  编辑
	 *
	 * @param devPr
	 * @return
	 */
	@AutoLog(value = "pr表-编辑")
	@ApiOperation(value="pr表-编辑", notes="pr表-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:dev_pr:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody DevPr devPr) {
		devPrService.updateById(devPr);
		return Result.OK("编辑成功!");
	}

	 /**
	  *  从Jira更新PR
	  *  有3个事件会触发新增或更新PR：
	  *  1、新建issue的时候（新增PR）
	  *  1、PR UT完成后（新增PR或更新PR）
	  *  2、PR Merge进主分支（更新PR）
	  *  3、用户手动更新（更新PR）
	  *
	  * @param
	  * @return
	  */
	 @AutoLog(value = "从Jira更新PR")
	 @ApiOperation(value="从Jira更新PR", notes="从Jira更新PR")
	 //@RequiresPermissions("org.jeecg.modules.demo:dev_pr:edit")
	 @RequestMapping(value = "/updateFromJira", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> updateFromJira(@RequestParam(name="issueId",required=true) String issueId, @RequestParam(name="projectId",required=true) String projectId, @RequestParam(name="jiraVersionName",required=true) String jiraVersionName) {

		 devPrBPService.updateFromJira(issueId, projectId, jiraVersionName);
		 return Result.OK("更新成功!");
	 }

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "pr表-通过id删除")
	@ApiOperation(value="pr表-通过id删除", notes="pr表-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:dev_pr:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		devPrService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "pr表-批量删除")
	@ApiOperation(value="pr表-批量删除", notes="pr表-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:dev_pr:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.devPrService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "pr表-通过id查询")
	@ApiOperation(value="pr表-通过id查询", notes="pr表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<DevPr> queryById(@RequestParam(name="id",required=true) String id) {
		DevPr devPr = devPrService.getById(id);
		if(devPr==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(devPr);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param devPr
    */
    //@RequiresPermissions("org.jeecg.modules.demo:dev_pr:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DevPr devPr) {
        return super.exportXls(request, devPr, DevPr.class, "pr表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("dev_pr:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, DevPr.class);
    }

}
