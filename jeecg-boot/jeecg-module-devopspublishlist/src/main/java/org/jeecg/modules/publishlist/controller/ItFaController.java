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

import org.jeecg.modules.publishlist.bpservice.ItFixAssertsBPService;
import org.jeecg.modules.publishlist.entity.ItFa;
import org.jeecg.modules.publishlist.entity.ItFixAssertsTransferRecord;
import org.jeecg.modules.publishlist.service.IItFaService;
import org.jeecg.modules.publishlist.service.IItFixAssertsTransferRecordService;
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
 * @Description: 固定资产表
 * @Author: jeecg-boot
 * @Date:   2023-06-27
 * @Version: V1.0
 */
@Api(tags="固定资产表")
@RestController
@RequestMapping("/it/itFa")
@Slf4j
public class ItFaController extends JeecgController<ItFa, IItFaService> {
	@Autowired
	private IItFaService itFaService;

	@Autowired
	private ItFixAssertsBPService itFixAssertsBPService;
	
	/**
	 * 分页列表查询
	 *
	 * @param itFa
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "固定资产表-分页列表查询")
	@ApiOperation(value="固定资产表-分页列表查询", notes="固定资产表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ItFa>> queryPageList(ItFa itFa,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ItFa> queryWrapper = QueryGenerator.initQueryWrapper(itFa, req.getParameterMap());
		Page<ItFa> page = new Page<ItFa>(pageNo, pageSize);
		IPage<ItFa> pageList = itFaService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 固定资产调拨
	  *
	  * @param fixAssertId
	  * @param toUserOwner
	  * @param toDepartment
	  * @param toUseStatus
	  * @param toLocation
	  * @return
	  */
	 //@AutoLog(value = "固定资产调拨")
	 @ApiOperation(value="固定资产调拨", notes="固定资产调拨")
	 @PostMapping(value = "/transfer")
	 public Result<String> transfer(@RequestBody String fixAssertId,
			                        @RequestBody String toUserOwner,
									@RequestBody String toDepartment,
									@RequestBody String toUseStatus,
									@RequestBody String toLocation) {

		 itFixAssertsBPService.transfer(fixAssertId, toUserOwner, toDepartment, toUseStatus, toLocation);
		 return Result.OK("调拨成功");
	 }

	 /**
	  * 固定资产折旧
	  *
	  * @param fixAssertId
	  * @return
	  */
	 //@AutoLog(value = "固定资产折旧")
	 @ApiOperation(value="固定资产折旧", notes="固定资产折旧")
	 @PostMapping(value = "/depreciation")
	 public Result<String> depreciation(@RequestBody String fixAssertId) {
		 itFixAssertsBPService.depreciation(fixAssertId);
		 return Result.OK("折旧成功");
	 }

	 /**
	  * 固定资产折旧-所有
	  *
	  * @param
	  * @return
	  */
	 //@AutoLog(value = "固定资产折旧所有")
	 @ApiOperation(value="固定资产折旧所有", notes="固定资产折旧所有")
	 @PostMapping(value = "/depreciationAll")
	 public Result<String> depreciationAll() {
		 itFixAssertsBPService.depreciationAll();
		 return Result.OK("折旧成功");
	 }
	
	/**
	 *   添加
	 *
	 * @param itFa
	 * @return
	 */
	@AutoLog(value = "固定资产表-添加")
	@ApiOperation(value="固定资产表-添加", notes="固定资产表-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:it_fa:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ItFa itFa) {
		itFaService.save(itFa);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param itFa
	 * @return
	 */
	@AutoLog(value = "固定资产表-编辑")
	@ApiOperation(value="固定资产表-编辑", notes="固定资产表-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:it_fa:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ItFa itFa) {
		itFaService.updateById(itFa);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "固定资产表-通过id删除")
	@ApiOperation(value="固定资产表-通过id删除", notes="固定资产表-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:it_fa:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		itFaService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "固定资产表-批量删除")
	@ApiOperation(value="固定资产表-批量删除", notes="固定资产表-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:it_fa:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.itFaService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "固定资产表-通过id查询")
	@ApiOperation(value="固定资产表-通过id查询", notes="固定资产表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ItFa> queryById(@RequestParam(name="id",required=true) String id) {
		ItFa itFa = itFaService.getById(id);
		if(itFa==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(itFa);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param itFa
    */
    //@RequiresPermissions("org.jeecg.modules.demo:it_fa:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ItFa itFa) {
        return super.exportXls(request, itFa, ItFa.class, "固定资产表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("it_fa:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ItFa.class);
    }

}
