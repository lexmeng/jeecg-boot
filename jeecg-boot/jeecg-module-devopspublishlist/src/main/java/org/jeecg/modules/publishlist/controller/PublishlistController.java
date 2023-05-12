package org.jeecg.modules.publishlist.controller;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.publishlist.bpservice.JenkinsBPService;
import org.jeecg.modules.publishlist.bpservice.PublishlistBPService;
import org.jeecg.modules.publishlist.bpservice.ReleaseInfoBPService;
import org.jeecg.modules.publishlist.entity.DependentComponent;
import org.jeecg.modules.publishlist.entity.PackageUrl;
import org.jeecg.modules.publishlist.service.IDependentComponentService;
import org.jeecg.modules.publishlist.service.IPackageUrlService;
import org.jeecg.modules.publishlist.vo.PublishlistQueryResult;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.publishlist.entity.PublishlistProject;
import org.jeecg.modules.publishlist.entity.Publishlist;
import org.jeecg.modules.publishlist.vo.PublishlistPage;
import org.jeecg.modules.publishlist.service.IPublishlistService;
import org.jeecg.modules.publishlist.service.IPublishlistProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
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
	private ReleaseInfoBPService releaseInfoBPService;
	
	@Autowired
	private IDependentComponentService dependentComponentService;
	
	@Autowired
	private IPackageUrlService packageUrlService;

	@Autowired
	private JenkinsBPService jenkinsBPService;
	
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
	public Result<IPage<Publishlist>> queryPageList(Publishlist publishlist,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Publishlist> queryWrapper = QueryGenerator.initQueryWrapper(publishlist, req.getParameterMap());
		Page<Publishlist> page = new Page<Publishlist>(pageNo, pageSize);
		IPage<Publishlist> pageList = publishlistService.page(page, queryWrapper);
		return Result.OK(pageList);
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
	public Result<PublishlistQueryResult> queryById(@RequestParam(name="id",required=true) String id) {
		PublishlistQueryResult publishlistQueryResult = publishlistService.queryByMainId(id);

		if(publishlistQueryResult==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(publishlistQueryResult);
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

	 @ApiOperation(value="生成ProductHandbookPRContent", notes="生成ProductHandbookPRContent")
	 @GetMapping(value = "/generateProductHandbookPRContent")
	 public Result<String> generateProductHandbookPRContent(@RequestParam(name="id",required=true) String id) {
		 Publishlist publishlist = publishlistService.getById(id);
		 if(publishlist==null) {
			 return Result.error("未找到对应数据");
		 }

		 Map<String, String> placeholderContentMap = new HashMap<>();
		 String productHandbookPRContent = releaseInfoBPService.generateProductHandbookPRContent(id, placeholderContentMap);

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

	 @ApiOperation(value="调用jenkins提交ProductPackagePR", notes="调用jenkins提交ProductPackagePR")
	 @GetMapping(value = "/jenkinsCommitProductPackagePR")
	 public Result<String> jenkinsCommitProductPackagePR(@RequestParam(name="id",required=true) String id) {
		 Publishlist publishlist = publishlistService.getById(id);
		 if(publishlist==null) {
			 return Result.error("未找到对应数据");
		 }

		 jenkinsBPService.jenkinsCommitProductPackagePR();

		 return Result.OK("提交成功！");
	 }

	 @ApiOperation(value="调用jenkins提交ProductHandbookPR", notes="调用jenkins提交ProductHandbookPR")
	 @GetMapping(value = "/jenkinsCommitProductHandbookPR")
	 public Result<String> jenkinsCommitProductHandbookPR(@RequestParam(name="id",required=true) String id) {
		 Publishlist publishlist = publishlistService.getById(id);
		 if(publishlist==null) {
			 return Result.error("未找到对应数据");
		 }

		 jenkinsBPService.jenkinsCommitProductHandbookPR();

		 return Result.OK("提交成功！");
	 }



}
