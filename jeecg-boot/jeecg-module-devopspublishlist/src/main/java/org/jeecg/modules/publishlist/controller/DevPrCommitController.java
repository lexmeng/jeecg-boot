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

import org.jeecg.modules.publishlist.bpservice.DevPrBPService;
import org.jeecg.modules.publishlist.entity.DevPrCommit;
import org.jeecg.modules.publishlist.service.IDevPrCommitService;
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
 * @Description: pr commit表-跟pr ut一一对应
 * @Author: jeecg-boot
 * @Date:   2023-06-12
 * @Version: V1.0
 */
@Api(tags="pr commit表-跟pr ut一一对应")
@RestController
@RequestMapping("/dev/devPrCommit")
@Slf4j
public class DevPrCommitController extends JeecgController<DevPrCommit, IDevPrCommitService> {
	@Autowired
	private IDevPrCommitService devPrCommitService;

	@Autowired
	private DevPrBPService devPrBPService;
	
	/**
	 * 分页列表查询
	 *
	 * @param devPrCommit
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "pr commit表-跟pr ut一一对应-分页列表查询")
	@ApiOperation(value="pr commit表-跟pr ut一一对应-分页列表查询", notes="pr commit表-跟pr ut一一对应-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<DevPrCommit>> queryPageList(DevPrCommit devPrCommit,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DevPrCommit> queryWrapper = QueryGenerator.initQueryWrapper(devPrCommit, req.getParameterMap());
		Page<DevPrCommit> page = new Page<DevPrCommit>(pageNo, pageSize);
		IPage<DevPrCommit> pageList = devPrCommitService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *   PR UT运行完会添加一条PR Commit
	 *   PR Merged进去会添加一条PR Commit
	 * @param devPrCommit
	 * @return
	 */
	@AutoLog(value = "pr commit表-跟pr ut一一对应-添加")
	@ApiOperation(value="pr commit表-跟pr ut一一对应-添加", notes="pr commit表-跟pr ut一一对应-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:dev_pr_commit:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody DevPrCommit devPrCommit) {
		devPrCommitService.save(devPrCommit);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param devPrCommit
	 * @return
	 */
	@AutoLog(value = "pr commit表-跟pr ut一一对应-编辑")
	@ApiOperation(value="pr commit表-跟pr ut一一对应-编辑", notes="pr commit表-跟pr ut一一对应-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:dev_pr_commit:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody DevPrCommit devPrCommit) {
		devPrCommitService.updateById(devPrCommit);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "pr commit表-跟pr ut一一对应-通过id删除")
	@ApiOperation(value="pr commit表-跟pr ut一一对应-通过id删除", notes="pr commit表-跟pr ut一一对应-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:dev_pr_commit:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		devPrCommitService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "pr commit表-跟pr ut一一对应-批量删除")
	@ApiOperation(value="pr commit表-跟pr ut一一对应-批量删除", notes="pr commit表-跟pr ut一一对应-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:dev_pr_commit:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.devPrCommitService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "pr commit表-跟pr ut一一对应-通过id查询")
	@ApiOperation(value="pr commit表-跟pr ut一一对应-通过id查询", notes="pr commit表-跟pr ut一一对应-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<DevPrCommit> queryById(@RequestParam(name="id",required=true) String id) {
		DevPrCommit devPrCommit = devPrCommitService.getById(id);
		if(devPrCommit==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(devPrCommit);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param devPrCommit
    */
    //@RequiresPermissions("org.jeecg.modules.demo:dev_pr_commit:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DevPrCommit devPrCommit) {
        return super.exportXls(request, devPrCommit, DevPrCommit.class, "pr commit表-跟pr ut一一对应");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("dev_pr_commit:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, DevPrCommit.class);
    }

}
