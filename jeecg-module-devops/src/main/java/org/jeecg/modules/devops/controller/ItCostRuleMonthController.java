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

import org.jeecg.modules.devops.entity.ItCostRuleMonth;
import org.jeecg.modules.devops.service.IItCostRuleMonthService;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 固定月单价类型
 * @Author: jeecg-boot
 * @Date:   2023-06-19
 * @Version: V1.0
 */
@Api(tags="固定月单价类型")
@RestController
@RequestMapping("/org.jeecg.modules.devops/itCostRuleMonth")
@Slf4j
public class ItCostRuleMonthController extends JeecgController<ItCostRuleMonth, IItCostRuleMonthService> {
	@Autowired
	private IItCostRuleMonthService itCostRuleMonthService;

	/**
	 * 分页列表查询
	 *
	 * @param itCostRuleMonth
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "固定月单价类型-分页列表查询")
	@ApiOperation(value="固定月单价类型-分页列表查询", notes="固定月单价类型-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ItCostRuleMonth>> queryPageList(ItCostRuleMonth itCostRuleMonth,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ItCostRuleMonth> queryWrapper = QueryGenerator.initQueryWrapper(itCostRuleMonth, req.getParameterMap());
		Page<ItCostRuleMonth> page = new Page<ItCostRuleMonth>(pageNo, pageSize);
		IPage<ItCostRuleMonth> pageList = itCostRuleMonthService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param itCostRuleMonth
	 * @return
	 */
	@AutoLog(value = "固定月单价类型-添加")
	@ApiOperation(value="固定月单价类型-添加", notes="固定月单价类型-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:it_cost_rule_month:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ItCostRuleMonth itCostRuleMonth) {
		itCostRuleMonthService.save(itCostRuleMonth);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param itCostRuleMonth
	 * @return
	 */
	@AutoLog(value = "固定月单价类型-编辑")
	@ApiOperation(value="固定月单价类型-编辑", notes="固定月单价类型-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:it_cost_rule_month:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ItCostRuleMonth itCostRuleMonth) {
		itCostRuleMonthService.updateById(itCostRuleMonth);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "固定月单价类型-通过id删除")
	@ApiOperation(value="固定月单价类型-通过id删除", notes="固定月单价类型-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:it_cost_rule_month:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		itCostRuleMonthService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "固定月单价类型-批量删除")
	@ApiOperation(value="固定月单价类型-批量删除", notes="固定月单价类型-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:it_cost_rule_month:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.itCostRuleMonthService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "固定月单价类型-通过id查询")
	@ApiOperation(value="固定月单价类型-通过id查询", notes="固定月单价类型-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ItCostRuleMonth> queryById(@RequestParam(name="id",required=true) String id) {
		ItCostRuleMonth itCostRuleMonth = itCostRuleMonthService.getById(id);
		if(itCostRuleMonth==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(itCostRuleMonth);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param itCostRuleMonth
    */
    //@RequiresPermissions("org.jeecg.modules.demo:it_cost_rule_month:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ItCostRuleMonth itCostRuleMonth) {
        return super.exportXls(request, itCostRuleMonth, ItCostRuleMonth.class, "固定月单价类型");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("it_cost_rule_month:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ItCostRuleMonth.class);
    }

}
