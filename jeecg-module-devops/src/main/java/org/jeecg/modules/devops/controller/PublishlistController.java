package org.jeecg.modules.devops.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.devops.aspect.AutoLogPublishlist;
import org.jeecg.modules.devops.bpservice.JenkinsBPService;
import org.jeecg.modules.devops.bpservice.PublishlistBPService;
import org.jeecg.modules.devops.bpservice.ReleaseInfoBPService;
import org.jeecg.modules.devops.entity.DependentComponent;
import org.jeecg.modules.devops.entity.PackageUrl;
import org.jeecg.modules.devops.entity.Publishlist;
import org.jeecg.modules.devops.entity.PublishlistProject;
import org.jeecg.modules.devops.service.IDependentComponentService;
import org.jeecg.modules.devops.service.IPackageUrlService;
import org.jeecg.modules.devops.service.IPublishlistProjectService;
import org.jeecg.modules.devops.service.IPublishlistService;
import org.jeecg.modules.devops.vo.PublishlistPage;
import org.jeecg.modules.devops.domainservice.IPublishlistDomainService;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 发布单
 * @Author: jeecg-boot
 * @Date:   2023-04-17
 * @Version: V1.0
 */
@Api(tags="发布单")
@RestController
@RequestMapping("/release")
@Slf4j
public class PublishlistController extends JeecgController<Publishlist, IPublishlistService> {
	@Autowired
	private IPublishlistService publishlistService;
	@Autowired
	private IPublishlistProjectService publishlistProjectService;

	@Autowired
	private PublishlistBPService publishlistBPService;

	@Autowired
	private IPublishlistDomainService publishlistDomainService;

	@Autowired
	private ReleaseInfoBPService releaseInfoBPService;

	@Autowired
	private IDependentComponentService dependentComponentService;

	@Autowired
	private IPackageUrlService packageUrlService;

	@Autowired
	private JenkinsBPService jenkinsBPService;

	@Autowired
	private ISysBaseAPI sysBaseAPI;

	/**
	 * 分页列表查询
	 *
	 * @param publishlist
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "发布单-分页列表查询")
	@ApiOperation(value="发布单-分页列表查询", notes="发布单-分页列表查询")
	@GetMapping(value = "/list")
	@AutoLogPublishlist("Enter PublishlistController queryPageList method")
	public Result<IPage<PublishlistPage>> queryPageList(Publishlist publishlist,
                                                        @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                        @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                        HttpServletRequest req) {
		QueryWrapper<Publishlist> queryWrapper = QueryGenerator.initQueryWrapper(publishlist, req.getParameterMap());
		Page<Publishlist> page = new Page<Publishlist>(pageNo, pageSize);
		IPage<Publishlist> pageList = publishlistService.page(page, queryWrapper);

		IPage<PublishlistPage> resultList = new Page<PublishlistPage>(pageNo, pageSize);
		resultList.setTotal(pageList.getTotal());
		resultList.setPages(pageList.getPages());

		List<PublishlistPage> queryList = new ArrayList<>();
		for(Publishlist tempPublishlist : pageList.getRecords()){
			PublishlistPage queryResult = publishlistService.queryByMainIdPage(tempPublishlist.getId());
			queryList.add(queryResult);
		}
		resultList.setRecords(queryList);

		return Result.OK(resultList);
	}

	/**
	 *   添加
	 *
	 * @param publishlistPage
	 * @return
	 */
	@AutoLog(value = "发布单-添加")
	@ApiOperation(value="发布单-添加", notes="发布单-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody PublishlistPage publishlistPage) {
		Publishlist publishlist = new Publishlist();
		BeanUtils.copyProperties(publishlistPage, publishlist);
		LoginUser user = sysBaseAPI.getUserByName(publishlist.getPmId());
		publishlist.setPmName(user.getRealname());
		publishlistBPService.savePublishlist(publishlist, publishlistPage.getPublishlistProjectList(),publishlistPage.getDependentComponentList(),publishlistPage.getPackageUrlList());

		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param publishlistPage
	 * @return
	 */
	@AutoLog(value = "发布单-编辑")
	@ApiOperation(value="发布单-编辑", notes="发布单-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody PublishlistPage publishlistPage) {
		Publishlist publishlist = new Publishlist();
		BeanUtils.copyProperties(publishlistPage, publishlist);
		LoginUser user = sysBaseAPI.getUserByName(publishlist.getPmId());
		publishlist.setPmName(user.getRealname());

		Publishlist publishlistEntity = publishlistService.getById(publishlist.getId());
		if(publishlistEntity==null) {
			return Result.error("未找到对应数据");
		}
		publishlistService.updateMain(publishlist, publishlistPage.getPublishlistProjectList(),publishlistPage.getDependentComponentList(),publishlistPage.getPackageUrlList());
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "发布单-通过id删除")
	@ApiOperation(value="发布单-通过id删除", notes="发布单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		publishlistService.delMain(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "发布单-批量删除")
	@ApiOperation(value="发布单-批量删除", notes="发布单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.publishlistService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "发布单-通过id查询")
	@ApiOperation(value="发布单-通过id查询", notes="发布单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<PublishlistPage> queryById(@RequestParam(name="id",required=true) String id) {
		PublishlistPage publishlistQueryResult = publishlistService.queryByMainIdPage(id);

		if(publishlistQueryResult==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(publishlistQueryResult);
	}


	 @AutoLog(value = "发布单-开发状态")
	 @ApiOperation(value="发布单-开发状态", notes="发布单-开发状态")
	 @RequestMapping(value = "/develop", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> develop(@RequestParam(name="publishlistId",required=true) String publishlistId) {
		 publishlistDomainService.develop(publishlistId);
		 return Result.OK("开发状态成功！");
	 }

	 @AutoLog(value = "发布单-测试状态")
	 @ApiOperation(value="发布单-测试状态", notes="发布单-测试状态")
	 @RequestMapping(value = "/test", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> test(@RequestParam(name="publishlistId",required=true) String publishlistId) {
		 publishlistDomainService.test(publishlistId);
		 return Result.OK("测试状态成功！");
	 }

	 @AutoLog(value = "发布单-发布")
	 @ApiOperation(value="发布单-发布", notes="发布单-发布")
	 @RequestMapping(value = "/publish", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> publish(@RequestParam(name="publishlistId",required=true) String publishlistId) {
		 publishlistBPService.publish(publishlistId);
		 return Result.OK("发布成功！");
	 }


	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "发布单项目表通过主表ID查询")
	@ApiOperation(value="发布单项目表主表ID查询", notes="发布单项目表-通主表ID查询")
	@GetMapping(value = "/queryPublishlistProjectByMainId")
	public Result<List<PublishlistProject>> queryPublishlistProjectListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PublishlistProject> publishlistProjectList = publishlistProjectService.selectByMainId(id);
		return Result.OK(publishlistProjectList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "依赖组件通过主表ID查询")
	@ApiOperation(value="依赖组件主表ID查询", notes="依赖组件-通主表ID查询")
	@GetMapping(value = "/queryDependentComponentByMainId")
	public Result<List<DependentComponent>> queryDependentComponentListByMainId(@RequestParam(name="id",required=true) String id) {
		List<DependentComponent> dependentComponentList = dependentComponentService.selectByMainId(id);
		return Result.OK(dependentComponentList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "产品包的下载地址通过主表ID查询")
	@ApiOperation(value="产品包的下载地址主表ID查询", notes="产品包的下载地址-通主表ID查询")
	@GetMapping(value = "/queryPackageUrlByMainId")
	public Result<List<PackageUrl>> queryPackageUrlListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PackageUrl> packageUrlList = packageUrlService.selectByMainId(id);
		return Result.OK(packageUrlList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param publishlist
    */
    //@RequiresPermissions("org.jeecg.modules.demo:pub_publishlist:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Publishlist publishlist) {
        return super.exportXls(request, publishlist, Publishlist.class, "发布单");
    }

    /**
    * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("pub_publishlist:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Publishlist.class);
    }

	 /**
	  * 生成ReleaseNote
	  * @param
	  * @return
	  */
	 @ApiOperation(value="生成ReleaseNote", notes="生成ReleaseNote")
	 @GetMapping(value = "/generateReleaseNoteContent")
	 public Result<String> generateReleaseNoteContent(@RequestParam(name="id",required=true) String id) {
		 Publishlist publishlist = publishlistService.getById(id);
		 if(publishlist==null) {
			 return Result.error("未找到对应数据");
		 }

         Map<String, String> placeholderContentMap = new HashMap<>();
		 String releaseNoteContent = releaseInfoBPService.generateReleaseNoteContent(id, placeholderContentMap);

		 return Result.OK(releaseNoteContent);
	 }

	 @ApiOperation(value="生成ReleaseMailContent", notes="生成ReleaseMailContent")
	 @GetMapping(value = "/generateReleaseMailContent")
	 public Result<String> generateReleaseMailContent(@RequestParam(name="id",required=true) String id) {
		 Publishlist publishlist = publishlistService.getById(id);
		 if(publishlist==null) {
			 return Result.error("未找到对应数据");
		 }

		 Map<String, String> placeholderContentMap = new HashMap<>();
		 String releaseMailContent = releaseInfoBPService.generateReleaseMailContent(id, placeholderContentMap);

		 return Result.OK(releaseMailContent);
	 }

	 @ApiOperation(value="生成ProductHandbookPREnContent", notes="生成ProductHandbookPREnContent")
	 @GetMapping(value = "/generateProductHandbookPREnContent")
	 public Result<String> generateProductHandbookPREnContent(@RequestParam(name="id",required=true) String id) {
		 Publishlist publishlist = publishlistService.getById(id);
		 if(publishlist==null) {
			 return Result.error("未找到对应数据");
		 }

		 Map<String, String> placeholderContentMap = new HashMap<>();
		 String productHandbookPRContent = releaseInfoBPService.generateProductHandbookPREnContent(id, placeholderContentMap);

		 return Result.OK(productHandbookPRContent);
	 }

	 @ApiOperation(value="生成ProductHandbookPRChContent", notes="生成ProductHandbookPRChContent")
	 @GetMapping(value = "/generateProductHandbookPRChContent")
	 public Result<String> generateProductHandbookPRChContent(@RequestParam(name="id",required=true) String id) {
		 Publishlist publishlist = publishlistService.getById(id);
		 if(publishlist==null) {
			 return Result.error("未找到对应数据");
		 }

		 Map<String, String> placeholderContentMap = new HashMap<>();
		 String productHandbookPRContent = releaseInfoBPService.generateProductHandbookPRChContent(id, placeholderContentMap);

		 return Result.OK(productHandbookPRContent);
	 }

	 @ApiOperation(value="生成ProductPackagePRContent", notes="生成ProductPackagePRContent")
	 @GetMapping(value = "/generateProductPackagePRContent")
	 public Result<String> generateProductPackagePRContent(@RequestParam(name="id",required=true) String id) {
		 Publishlist publishlist = publishlistService.getById(id);
		 if(publishlist==null) {
			 return Result.error("未找到对应数据");
		 }

		 Map<String, String> placeholderContentMap = new HashMap<>();
		 String productPackagePRContent = releaseInfoBPService.generateProductPackagePRContent(id, placeholderContentMap);

		 return Result.OK(productPackagePRContent);
	 }

	 @ApiOperation(value="生成官网内容", notes="生成官网内容")
	 @GetMapping(value = "/generateWebsiteContent")
	 public Result<String> generateWebsiteContent(@RequestParam(name="id",required=true) String id) {
		 Publishlist publishlist = publishlistService.getById(id);
		 if(publishlist==null) {
			 return Result.error("未找到对应数据");
		 }

		 Map<String, String> placeholderContentMap = new HashMap<>();
		 String companyWebsiteContent = releaseInfoBPService.generateCompanyWebsiteContent(id, placeholderContentMap);

		 return Result.OK(companyWebsiteContent);
	 }

	 @ApiOperation(value="调用jenkins提交ProductPackagePR", notes="调用jenkins提交ProductPackagePR")
	 @GetMapping(value = "/jenkinsCommitProductPackagePR")
	 public Result<String> jenkinsCommitProductPackagePR(@RequestParam(name="id",required=true) String id, @RequestParam(name="rowNumInsert",required = true) String rowNumInsert) {
		 Publishlist publishlist = publishlistService.getById(id);
		 if(publishlist==null) {
			 return Result.error("未找到对应数据");
		 }

		 Map<String, String> placeholderContentMap = new HashMap<>();
		 String productPackagePRContent = releaseInfoBPService.generateProductPackagePRContent(id, placeholderContentMap);

		 jenkinsBPService.jenkinsCommitProductPackagePR(publishlist.getVersionName(), productPackagePRContent, publishlist.getDocumentVersion(), rowNumInsert);

		 return Result.OK("提交成功！");
	 }

	 @ApiOperation(value="调用jenkins提交ProductHandbookPR", notes="调用jenkins提交ProductHandbookPR")
	 @GetMapping(value = "/jenkinsCommitProductHandbookPR")
	 public Result<String> jenkinsCommitProductHandbookPR(@RequestParam(name="id",required=true) String id) {
		 Publishlist publishlist = publishlistService.getById(id);
		 if(publishlist==null) {
			 return Result.error("未找到对应数据");
		 }

		 Map<String, String> placeholderContentMap = new HashMap<>();
		 String productHandbookPREnContent = releaseInfoBPService.generateProductHandbookPREnContent(id, placeholderContentMap);
		 String productHandbookPRChContent = releaseInfoBPService.generateProductHandbookPRChContent(id, placeholderContentMap);

		 jenkinsBPService.jenkinsCommitProductHandbookPR(publishlist.getVersionName(), productHandbookPRChContent, productHandbookPREnContent, publishlist.getDocumentVersion());

		 return Result.OK("提交成功！");
	 }


}
