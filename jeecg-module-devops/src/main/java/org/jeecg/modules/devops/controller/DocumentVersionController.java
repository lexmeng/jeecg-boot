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

import org.jeecg.modules.devops.entity.DocumentVersion;
import org.jeecg.modules.devops.service.IDocumentVersionService;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 文档版本
 * @Author: jeecg-boot
 * @Date:   2023-06-08
 * @Version: V1.0
 */
@Api(tags="文档版本")
@RestController
@RequestMapping("/release/documentVersion")
@Slf4j
public class DocumentVersionController extends JeecgController<DocumentVersion, IDocumentVersionService> {
	@Autowired
	private IDocumentVersionService documentVersionService;

	/**
	 * 分页列表查询
	 *
	 * @param documentVersion
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "文档版本-分页列表查询")
	@ApiOperation(value="文档版本-分页列表查询", notes="文档版本-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<DocumentVersion>> queryPageList(DocumentVersion documentVersion,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DocumentVersion> queryWrapper = QueryGenerator.initQueryWrapper(documentVersion, req.getParameterMap());
		Page<DocumentVersion> page = new Page<DocumentVersion>(pageNo, pageSize);
		IPage<DocumentVersion> pageList = documentVersionService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param documentVersion
	 * @return
	 */
	@AutoLog(value = "文档版本-添加")
	@ApiOperation(value="文档版本-添加", notes="文档版本-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:pub_document_version:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody DocumentVersion documentVersion) {
		documentVersionService.save(documentVersion);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param documentVersion
	 * @return
	 */
	@AutoLog(value = "文档版本-编辑")
	@ApiOperation(value="文档版本-编辑", notes="文档版本-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:pub_document_version:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody DocumentVersion documentVersion) {
		documentVersionService.updateById(documentVersion);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "文档版本-通过id删除")
	@ApiOperation(value="文档版本-通过id删除", notes="文档版本-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:pub_document_version:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		documentVersionService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "文档版本-批量删除")
	@ApiOperation(value="文档版本-批量删除", notes="文档版本-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:pub_document_version:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.documentVersionService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "文档版本-通过id查询")
	@ApiOperation(value="文档版本-通过id查询", notes="文档版本-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<DocumentVersion> queryById(@RequestParam(name="id",required=true) String id) {
		DocumentVersion documentVersion = documentVersionService.getById(id);
		if(documentVersion==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(documentVersion);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param documentVersion
    */
    //@RequiresPermissions("org.jeecg.modules.demo:pub_document_version:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DocumentVersion documentVersion) {
        return super.exportXls(request, documentVersion, DocumentVersion.class, "文档版本");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("pub_document_version:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, DocumentVersion.class);
    }

}
