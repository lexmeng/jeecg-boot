package org.jeecg.modules.publishlist.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.publishlist.bpservice.DevPrBPService;
import org.jeecg.modules.publishlist.bpservice.IssueBPService;
import org.jeecg.modules.publishlist.entity.DevPr;
import org.jeecg.modules.publishlist.entity.Issue;
import org.jeecg.modules.publishlist.entity.Publishlist;
import org.jeecg.modules.publishlist.exception.BussinessException;
import org.jeecg.modules.publishlist.service.IIssueService;
import org.jeecg.modules.publishlist.service.IPublishlistService;
import org.jeecg.modules.publishlist.tools.IssueDevStatusResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: issue本地记录
 * @Author: jeecg-boot
 * @Date:   2023-04-17
 * @Version: V1.0
 */
@Api(tags="issue本地记录")
@RestController
@RequestMapping("/release/issue")
@Slf4j
public class IssueController extends JeecgController<Issue, IIssueService> {
	@Autowired
	private IIssueService issueService;

	@Autowired
	private IssueBPService issueBPService;

	@Autowired
	private DevPrBPService devPrBPService;
	
	/**
	 * 分页列表查询
	 *
	 * @param issue
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "issue本地记录-分页列表查询")
	@ApiOperation(value="issue本地记录-分页列表查询", notes="issue本地记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Issue>> queryPageList(Issue issue,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Issue> queryWrapper = QueryGenerator.initQueryWrapper(issue, req.getParameterMap());
		Page<Issue> page = new Page<Issue>(pageNo, pageSize);
		IPage<Issue> pageList = issueService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param issue
	 * @return
	 */
	@AutoLog(value = "issue本地记录-添加")
	@ApiOperation(value="issue本地记录-添加", notes="issue本地记录-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:pub_issue:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Issue issue) {
		issueService.save(issue);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param issue
	 * @return
	 */
	@AutoLog(value = "issue本地记录-编辑")
	@ApiOperation(value="issue本地记录-编辑", notes="issue本地记录-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:pub_issue:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Issue issue) {
		issueService.updateById(issue);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "issue本地记录-通过id删除")
	@ApiOperation(value="issue本地记录-通过id删除", notes="issue本地记录-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:pub_issue:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		issueService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "issue本地记录-批量删除")
	@ApiOperation(value="issue本地记录-批量删除", notes="issue本地记录-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:pub_issue:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.issueService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}


	/**
	 * 打包的时候，查询包内的issue列表
	 * 流程：查询所有issue关联的pr，只要有一个pr是merged的状态，就算包内的issue
	 * @return
	 */
	@ApiOperation(value="打包时获取有pr merged的issue列表", notes="打包时获取有pr merged的issue列表")
	@GetMapping(value = "/getIssuesWhenPackage")
	public Result<List<Issue>> getIssuesWhenPackage(@RequestParam(name="publishlistId",required=true) String publishlistId){
        List<Issue> resultIssueList = new ArrayList<>();

		if(!issueBPService.isEnableUpdateIssue(publishlistId)){
			throw new BussinessException("发布单已经发布，不支持该操作！");
		}

		//1、先更新issue列表
		//issueBPService.updateIssueList(publishlistId);

		//2、查询issue关联的pr，并且确认每个pr的状态
		List<Issue> issueList = issueBPService.getIssueListByPublishlistId(publishlistId);
        if(issueList == null || issueList.isEmpty()){
			return Result.OK(resultIssueList);
		}

		for(Issue issue : issueList){
			/*
			IssueDevStatusResult devStatusResult = issueBPService.fetchIssuePR(issue.getProjectId(), issue.getJiraVersionName(), issue.getIssueId());

			for(IssueDevStatusResult.PullRequest pr : devStatusResult.getPullRequests()){
				if(pr.getStatus().equals("MERGED")){
					resultIssueList.add(issue);
					break;//如果找到1条，则跳出本循环
				}
			}
            */
			//从本地获取而不是从远程获取
			List<DevPr> devPrList = devPrBPService.getIssuePr(issue.getIssueId(), issue.getJiraVersionName());
			for(DevPr devPr : devPrList){
				if(devPr.getPrStatus().equals("MERGED")){
					resultIssueList.add(issue);
					break;//如果找到1条，则跳出本循环
				}
			}
		}

		//3、返回含merged pr的issue列表
		return Result.OK(resultIssueList);
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "issue本地记录-通过id查询")
	@ApiOperation(value="issue本地记录-通过id查询", notes="issue本地记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Issue> queryById(@RequestParam(name="id",required=true) String id) {
		Issue issue = issueService.getById(id);
		if(issue==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(issue);
	}

	 /**
	  * 更新issue列表
	  * @param publishlistId
	  */
	 @AutoLog(value = "更新issue列表")
	 @ApiOperation(value="更新issue列表", notes="更新issue列表")
	 @RequestMapping(value = "/updateIssueList", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> updateIssueList(@RequestParam(name="publishlistId",required=true)String publishlistId){
		 issueBPService.updateIssueList(publishlistId);
		 return Result.OK("更新成功！");
	 }


	 @ApiOperation(value = "获取 Issue 开发信息", notes = "获取 Issue 开发信息，包含分支信息和 PR 信息")
	 @GetMapping(value = "/devStatus")
	 public Result<IssueDevStatusResult> fetchIssueDevStatus(@RequestParam("issueId") String issueId) {
		 return Result.ok(issueBPService.fetchIssueDevStatus(issueId));
	 }
    /**
    * 导出excel
    *
    * @param request
    * @param issue
    */
    //@RequiresPermissions("org.jeecg.modules.demo:pub_issue:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Issue issue) {
        return super.exportXls(request, issue, Issue.class, "issue本地记录");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("pub_issue:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Issue.class);
    }

}
