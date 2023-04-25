package org.jeecg.modules.publishlist.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.publishlist.entity.ReleaseInfo;
import org.jeecg.modules.publishlist.service.IReleaseInfoService;

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
 * @Description: release信息表
 * @Author: jeecg-boot
 * @Date:   2023-04-18
 * @Version: V1.0
 */
@Api(tags="release信息表")
@RestController
@RequestMapping("/io.kyligence/releaseInfo")
@Slf4j
public class ReleaseInfoController extends JeecgController<ReleaseInfo, IReleaseInfoService> {
	@Autowired
	private IReleaseInfoService releaseInfoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param releaseInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "release信息表-分页列表查询")
	@ApiOperation(value="release信息表-分页列表查询", notes="release信息表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ReleaseInfo>> queryPageList(ReleaseInfo releaseInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ReleaseInfo> queryWrapper = QueryGenerator.initQueryWrapper(releaseInfo, req.getParameterMap());
		Page<ReleaseInfo> page = new Page<ReleaseInfo>(pageNo, pageSize);
		IPage<ReleaseInfo> pageList = releaseInfoService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param releaseInfo
	 * @return
	 */
	@AutoLog(value = "release信息表-添加")
	@ApiOperation(value="release信息表-添加", notes="release信息表-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:release_info:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ReleaseInfo releaseInfo) {
		releaseInfoService.save(releaseInfo);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param releaseInfo
	 * @return
	 */
	@AutoLog(value = "release信息表-编辑")
	@ApiOperation(value="release信息表-编辑", notes="release信息表-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:release_info:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ReleaseInfo releaseInfo) {
		releaseInfoService.updateById(releaseInfo);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "release信息表-通过id删除")
	@ApiOperation(value="release信息表-通过id删除", notes="release信息表-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:release_info:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		releaseInfoService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "release信息表-批量删除")
	@ApiOperation(value="release信息表-批量删除", notes="release信息表-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:release_info:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.releaseInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "release信息表-通过id查询")
	@ApiOperation(value="release信息表-通过id查询", notes="release信息表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ReleaseInfo> queryById(@RequestParam(name="id",required=true) String id) {
		ReleaseInfo releaseInfo = releaseInfoService.getById(id);
		if(releaseInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(releaseInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param releaseInfo
    */
    //@RequiresPermissions("org.jeecg.modules.demo:release_info:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ReleaseInfo releaseInfo) {
        return super.exportXls(request, releaseInfo, ReleaseInfo.class, "release信息表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("release_info:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ReleaseInfo.class);
    }

}
