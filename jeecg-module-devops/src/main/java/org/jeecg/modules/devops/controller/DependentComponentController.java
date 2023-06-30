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

import org.jeecg.modules.devops.entity.DependentComponent;
import org.jeecg.modules.devops.service.IDependentComponentService;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 依赖组件
 * @Author: jeecg-boot
 * @Date:   2023-05-06
 * @Version: V1.0
 */
@Api(tags="依赖组件")
@RestController
@RequestMapping("/release/dependentComponent")
@Slf4j
public class DependentComponentController extends JeecgController<DependentComponent, IDependentComponentService> {
	@Autowired
	private IDependentComponentService dependentComponentService;

	/**
	 * 分页列表查询
	 *
	 * @param dependentComponent
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "依赖组件-分页列表查询")
	@ApiOperation(value="依赖组件-分页列表查询", notes="依赖组件-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<DependentComponent>> queryPageList(DependentComponent dependentComponent,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DependentComponent> queryWrapper = QueryGenerator.initQueryWrapper(dependentComponent, req.getParameterMap());
		Page<DependentComponent> page = new Page<DependentComponent>(pageNo, pageSize);
		IPage<DependentComponent> pageList = dependentComponentService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param dependentComponent
	 * @return
	 */
	@AutoLog(value = "依赖组件-添加")
	@ApiOperation(value="依赖组件-添加", notes="依赖组件-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:pub_dependent_component:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody DependentComponent dependentComponent) {
		dependentComponentService.save(dependentComponent);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param dependentComponent
	 * @return
	 */
	@AutoLog(value = "依赖组件-编辑")
	@ApiOperation(value="依赖组件-编辑", notes="依赖组件-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:dependent_component:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody DependentComponent dependentComponent) {
		dependentComponentService.updateById(dependentComponent);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "依赖组件-通过id删除")
	@ApiOperation(value="依赖组件-通过id删除", notes="依赖组件-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:pub_dependent_component:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		dependentComponentService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "依赖组件-批量删除")
	@ApiOperation(value="依赖组件-批量删除", notes="依赖组件-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:pub_dependent_component:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.dependentComponentService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "依赖组件-通过id查询")
	@ApiOperation(value="依赖组件-通过id查询", notes="依赖组件-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<DependentComponent> queryById(@RequestParam(name="id",required=true) String id) {
		DependentComponent dependentComponent = dependentComponentService.getById(id);
		if(dependentComponent==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(dependentComponent);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param dependentComponent
    */
    //@RequiresPermissions("org.jeecg.modules.demo:pub_dependent_component:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DependentComponent dependentComponent) {
        return super.exportXls(request, dependentComponent, DependentComponent.class, "依赖组件");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("pub_dependent_component:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, DependentComponent.class);
    }

}
