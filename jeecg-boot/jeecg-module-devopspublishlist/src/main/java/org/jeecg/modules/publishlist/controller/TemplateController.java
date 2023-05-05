package org.jeecg.modules.publishlist.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.publishlist.entity.Template;
import org.jeecg.modules.publishlist.service.ITemplateService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.publishlist.tools.IdTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 模板
 * @Author: jeecg-boot
 * @Date:   2023-04-18
 * @Version: V1.0
 */
@Api(tags="模板")
@RestController
@RequestMapping("/release/template")
@Slf4j
public class TemplateController extends JeecgController<Template, ITemplateService> {
	@Autowired
	private ITemplateService templateService;
	
	/**
	 * 分页列表查询
	 *
	 * @param template
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "模板-分页列表查询")
	@ApiOperation(value="模板-分页列表查询", notes="模板-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Template>> queryPageList(Template template,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Template> queryWrapper = QueryGenerator.initQueryWrapper(template, req.getParameterMap());
		Page<Template> page = new Page<Template>(pageNo, pageSize);
		IPage<Template> pageList = templateService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param template
	 * @return
	 */
	@AutoLog(value = "模板-添加")
	@ApiOperation(value="模板-添加", notes="模板-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:template:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Template template) {
		template.setId(IdTool.generalId());
		templateService.save(template);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param template
	 * @return
	 */
	@AutoLog(value = "模板-编辑")
	@ApiOperation(value="模板-编辑", notes="模板-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:template:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Template template) {
		templateService.updateById(template);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "模板-通过id删除")
	@ApiOperation(value="模板-通过id删除", notes="模板-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:template:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		templateService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "模板-批量删除")
	@ApiOperation(value="模板-批量删除", notes="模板-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:template:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.templateService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "模板-通过id查询")
	@ApiOperation(value="模板-通过id查询", notes="模板-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Template> queryById(@RequestParam(name="id",required=true) String id) {
		Template template = templateService.getById(id);
		if(template==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(template);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param template
    */
    //@RequiresPermissions("org.jeecg.modules.demo:template:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Template template) {
        return super.exportXls(request, template, Template.class, "模板");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("template:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Template.class);
    }

}
