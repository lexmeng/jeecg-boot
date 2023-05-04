package org.jeecg.modules.publishlist.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.publishlist.entity.IssueHistory;
import org.jeecg.modules.publishlist.service.IIssueHistoryService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: issue历史表
 * @Author: jeecg-boot
 * @Date:   2023-04-17
 * @Version: V1.0
 */
@Api(tags="issue历史表")
@RestController
@RequestMapping("/release/issueHistory")
@Slf4j
public class IssueHistoryController extends JeecgController<IssueHistory, IIssueHistoryService> {
	@Autowired
	private IIssueHistoryService issueHistoryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param issueHistory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "issue历史表-分页列表查询")
	@ApiOperation(value="issue历史表-分页列表查询", notes="issue历史表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<IssueHistory>> queryPageList(IssueHistory issueHistory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<IssueHistory> queryWrapper = QueryGenerator.initQueryWrapper(issueHistory, req.getParameterMap());
		Page<IssueHistory> page = new Page<IssueHistory>(pageNo, pageSize);
		IPage<IssueHistory> pageList = issueHistoryService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param issueHistory
	 * @return
	 */
	@AutoLog(value = "issue历史表-添加")
	@ApiOperation(value="issue历史表-添加", notes="issue历史表-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:issue_history:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody IssueHistory issueHistory) {
		issueHistoryService.save(issueHistory);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param issueHistory
	 * @return
	 */
	@AutoLog(value = "issue历史表-编辑")
	@ApiOperation(value="issue历史表-编辑", notes="issue历史表-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:issue_history:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody IssueHistory issueHistory) {
		issueHistoryService.updateById(issueHistory);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "issue历史表-通过id删除")
	@ApiOperation(value="issue历史表-通过id删除", notes="issue历史表-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:issue_history:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		issueHistoryService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "issue历史表-批量删除")
	@ApiOperation(value="issue历史表-批量删除", notes="issue历史表-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:issue_history:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.issueHistoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "issue历史表-通过id查询")
	@ApiOperation(value="issue历史表-通过id查询", notes="issue历史表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<IssueHistory> queryById(@RequestParam(name="id",required=true) String id) {
		IssueHistory issueHistory = issueHistoryService.getById(id);
		if(issueHistory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(issueHistory);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param issueHistory
    */
    //@RequiresPermissions("org.jeecg.modules.demo:issue_history:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, IssueHistory issueHistory) {
        return super.exportXls(request, issueHistory, IssueHistory.class, "issue历史表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("issue_history:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, IssueHistory.class);
    }

}
