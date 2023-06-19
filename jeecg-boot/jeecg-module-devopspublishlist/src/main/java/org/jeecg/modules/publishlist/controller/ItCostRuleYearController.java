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

import org.jeecg.modules.publishlist.entity.ItCostRuleYear;
import org.jeecg.modules.publishlist.service.IItCostRuleYearService;
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
 * @Description: 年费用类型
 * @Author: jeecg-boot
 * @Date:   2023-06-19
 * @Version: V1.0
 */
@Api(tags="年费用类型")
@RestController
@RequestMapping("/org.jeecg.modules.publishlist/itCostRuleYear")
@Slf4j
public class ItCostRuleYearController extends JeecgController<ItCostRuleYear, IItCostRuleYearService> {
	@Autowired
	private IItCostRuleYearService itCostRuleYearService;
	
	/**
	 * 分页列表查询
	 *
	 * @param itCostRuleYear
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "年费用类型-分页列表查询")
	@ApiOperation(value="年费用类型-分页列表查询", notes="年费用类型-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ItCostRuleYear>> queryPageList(ItCostRuleYear itCostRuleYear,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ItCostRuleYear> queryWrapper = QueryGenerator.initQueryWrapper(itCostRuleYear, req.getParameterMap());
		Page<ItCostRuleYear> page = new Page<ItCostRuleYear>(pageNo, pageSize);
		IPage<ItCostRuleYear> pageList = itCostRuleYearService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param itCostRuleYear
	 * @return
	 */
	@AutoLog(value = "年费用类型-添加")
	@ApiOperation(value="年费用类型-添加", notes="年费用类型-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:it_cost_rule_year:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ItCostRuleYear itCostRuleYear) {
		itCostRuleYearService.save(itCostRuleYear);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param itCostRuleYear
	 * @return
	 */
	@AutoLog(value = "年费用类型-编辑")
	@ApiOperation(value="年费用类型-编辑", notes="年费用类型-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:it_cost_rule_year:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ItCostRuleYear itCostRuleYear) {
		itCostRuleYearService.updateById(itCostRuleYear);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "年费用类型-通过id删除")
	@ApiOperation(value="年费用类型-通过id删除", notes="年费用类型-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:it_cost_rule_year:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		itCostRuleYearService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "年费用类型-批量删除")
	@ApiOperation(value="年费用类型-批量删除", notes="年费用类型-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:it_cost_rule_year:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.itCostRuleYearService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "年费用类型-通过id查询")
	@ApiOperation(value="年费用类型-通过id查询", notes="年费用类型-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ItCostRuleYear> queryById(@RequestParam(name="id",required=true) String id) {
		ItCostRuleYear itCostRuleYear = itCostRuleYearService.getById(id);
		if(itCostRuleYear==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(itCostRuleYear);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param itCostRuleYear
    */
    //@RequiresPermissions("org.jeecg.modules.demo:it_cost_rule_year:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ItCostRuleYear itCostRuleYear) {
        return super.exportXls(request, itCostRuleYear, ItCostRuleYear.class, "年费用类型");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("it_cost_rule_year:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ItCostRuleYear.class);
    }

}
