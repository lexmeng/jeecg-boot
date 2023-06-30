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

import org.jeecg.modules.devops.entity.ItFixAssertsTransferRecord;
import org.jeecg.modules.devops.service.IItFixAssertsTransferRecordService;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 固定资产移交记录
 * @Author: jeecg-boot
 * @Date:   2023-06-27
 * @Version: V1.0
 */
@Api(tags="固定资产移交记录")
@RestController
@RequestMapping("/it/itFixAssertsTransferRecord")
@Slf4j
public class ItFixAssertsTransferRecordController extends JeecgController<ItFixAssertsTransferRecord, IItFixAssertsTransferRecordService> {
	@Autowired
	private IItFixAssertsTransferRecordService itFixAssertsTransferRecordService;

	/**
	 * 分页列表查询
	 *
	 * @param itFixAssertsTransferRecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "固定资产移交记录-分页列表查询")
	@ApiOperation(value="固定资产移交记录-分页列表查询", notes="固定资产移交记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ItFixAssertsTransferRecord>> queryPageList(ItFixAssertsTransferRecord itFixAssertsTransferRecord,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ItFixAssertsTransferRecord> queryWrapper = QueryGenerator.initQueryWrapper(itFixAssertsTransferRecord, req.getParameterMap());
		Page<ItFixAssertsTransferRecord> page = new Page<ItFixAssertsTransferRecord>(pageNo, pageSize);
		IPage<ItFixAssertsTransferRecord> pageList = itFixAssertsTransferRecordService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param itFixAssertsTransferRecord
	 * @return
	 */
	@AutoLog(value = "固定资产移交记录-添加")
	@ApiOperation(value="固定资产移交记录-添加", notes="固定资产移交记录-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:it_fix_asserts_transfer_record:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ItFixAssertsTransferRecord itFixAssertsTransferRecord) {
		itFixAssertsTransferRecordService.save(itFixAssertsTransferRecord);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param itFixAssertsTransferRecord
	 * @return
	 */
	@AutoLog(value = "固定资产移交记录-编辑")
	@ApiOperation(value="固定资产移交记录-编辑", notes="固定资产移交记录-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:it_fix_asserts_transfer_record:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ItFixAssertsTransferRecord itFixAssertsTransferRecord) {
		itFixAssertsTransferRecordService.updateById(itFixAssertsTransferRecord);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "固定资产移交记录-通过id删除")
	@ApiOperation(value="固定资产移交记录-通过id删除", notes="固定资产移交记录-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:it_fix_asserts_transfer_record:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		itFixAssertsTransferRecordService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "固定资产移交记录-批量删除")
	@ApiOperation(value="固定资产移交记录-批量删除", notes="固定资产移交记录-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:it_fix_asserts_transfer_record:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.itFixAssertsTransferRecordService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "固定资产移交记录-通过id查询")
	@ApiOperation(value="固定资产移交记录-通过id查询", notes="固定资产移交记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ItFixAssertsTransferRecord> queryById(@RequestParam(name="id",required=true) String id) {
		ItFixAssertsTransferRecord itFixAssertsTransferRecord = itFixAssertsTransferRecordService.getById(id);
		if(itFixAssertsTransferRecord==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(itFixAssertsTransferRecord);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param itFixAssertsTransferRecord
    */
    //@RequiresPermissions("org.jeecg.modules.demo:it_fix_asserts_transfer_record:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ItFixAssertsTransferRecord itFixAssertsTransferRecord) {
        return super.exportXls(request, itFixAssertsTransferRecord, ItFixAssertsTransferRecord.class, "固定资产移交记录");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("it_fix_asserts_transfer_record:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ItFixAssertsTransferRecord.class);
    }

}
