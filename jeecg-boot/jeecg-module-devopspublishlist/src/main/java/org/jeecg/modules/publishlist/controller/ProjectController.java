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

import org.jeecg.modules.publishlist.entity.Project;
import org.jeecg.modules.publishlist.service.IProjectService;
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
 * @Description: 项目表
 * @Author: jeecg-boot
 * @Date:   2023-05-06
 * @Version: V1.0
 */
@Api(tags="项目表")
@RestController
@RequestMapping("/release/project")
@Slf4j
public class ProjectController extends JeecgController<Project, IProjectService> {
	@Autowired
	private IProjectService projectService;
	
	/**
	 * 分页列表查询
	 *
	 * @param project
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "项目表-分页列表查询")
	@ApiOperation(value="项目表-分页列表查询", notes="项目表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Project>> queryPageList(Project project,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Project> queryWrapper = QueryGenerator.initQueryWrapper(project, req.getParameterMap());
		Page<Project> page = new Page<Project>(pageNo, pageSize);
		IPage<Project> pageList = projectService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param project
	 * @return
	 */
	@AutoLog(value = "项目表-添加")
	@ApiOperation(value="项目表-添加", notes="项目表-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:project:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Project project) {
		projectService.save(project);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param project
	 * @return
	 */
	@AutoLog(value = "项目表-编辑")
	@ApiOperation(value="项目表-编辑", notes="项目表-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:project:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Project project) {
		projectService.updateById(project);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目表-通过id删除")
	@ApiOperation(value="项目表-通过id删除", notes="项目表-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:project:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		projectService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "项目表-批量删除")
	@ApiOperation(value="项目表-批量删除", notes="项目表-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:project:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.projectService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "项目表-通过id查询")
	@ApiOperation(value="项目表-通过id查询", notes="项目表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Project> queryById(@RequestParam(name="id",required=true) String id) {
		Project project = projectService.getById(id);
		if(project==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(project);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param project
    */
    //@RequiresPermissions("org.jeecg.modules.demo:project:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Project project) {
        return super.exportXls(request, project, Project.class, "项目表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("project:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Project.class);
    }

}
