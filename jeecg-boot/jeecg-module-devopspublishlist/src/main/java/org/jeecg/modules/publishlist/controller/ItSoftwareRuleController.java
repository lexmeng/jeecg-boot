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

import org.jeecg.modules.publishlist.entity.ItSoftwareRule;
import org.jeecg.modules.publishlist.service.IItSoftwareRuleService;
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
 * @Description: 软件分配表
 * @Author: jeecg-boot
 * @Date:   2023-06-19
 * @Version: V1.0
 */
@Api(tags="软件分配表")
@RestController
@RequestMapping("/it/itSoftwareRule")
@Slf4j
public class ItSoftwareRuleController extends JeecgController<ItSoftwareRule, IItSoftwareRuleService> {
	@Autowired
	private IItSoftwareRuleService itSoftwareRuleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param itSoftwareRule
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "软件分配表-分页列表查询")
	@ApiOperation(value="软件分配表-分页列表查询", notes="软件分配表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ItSoftwareRule>> queryPageList(ItSoftwareRule itSoftwareRule,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ItSoftwareRule> queryWrapper = QueryGenerator.initQueryWrapper(itSoftwareRule, req.getParameterMap());
		Page<ItSoftwareRule> page = new Page<ItSoftwareRule>(pageNo, pageSize);
		IPage<ItSoftwareRule> pageList = itSoftwareRuleService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param itSoftwareRule
	 * @return
	 */
	@AutoLog(value = "软件分配表-添加")
	@ApiOperation(value="软件分配表-添加", notes="软件分配表-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:it_software_rule:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ItSoftwareRule itSoftwareRule) {
		itSoftwareRuleService.save(itSoftwareRule);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param itSoftwareRule
	 * @return
	 */
	@AutoLog(value = "软件分配表-编辑")
	@ApiOperation(value="软件分配表-编辑", notes="软件分配表-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:it_software_rule:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ItSoftwareRule itSoftwareRule) {
		itSoftwareRuleService.updateById(itSoftwareRule);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "软件分配表-通过id删除")
	@ApiOperation(value="软件分配表-通过id删除", notes="软件分配表-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:it_software_rule:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		itSoftwareRuleService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "软件分配表-批量删除")
	@ApiOperation(value="软件分配表-批量删除", notes="软件分配表-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:it_software_rule:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.itSoftwareRuleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "软件分配表-通过id查询")
	@ApiOperation(value="软件分配表-通过id查询", notes="软件分配表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ItSoftwareRule> queryById(@RequestParam(name="id",required=true) String id) {
		ItSoftwareRule itSoftwareRule = itSoftwareRuleService.getById(id);
		if(itSoftwareRule==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(itSoftwareRule);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param itSoftwareRule
    */
    //@RequiresPermissions("org.jeecg.modules.demo:it_software_rule:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ItSoftwareRule itSoftwareRule) {
        return super.exportXls(request, itSoftwareRule, ItSoftwareRule.class, "软件分配表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("it_software_rule:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ItSoftwareRule.class);
    }

}
