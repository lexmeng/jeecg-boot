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
import org.jeecg.modules.publishlist.bpservice.ItSoftwareMonthlyCostBPService;
import org.jeecg.modules.publishlist.entity.DocumentVersion;
import org.jeecg.modules.publishlist.entity.ItSoftwareMonthlyCost;
import org.jeecg.modules.publishlist.service.IDocumentVersionService;
import org.jeecg.modules.publishlist.service.IItSoftwareMonthlyCostService;
import org.jeecg.modules.publishlist.tools.FeishuMessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
* @Description: IT软件月成本
* @Author: jeecg-boot
* @Date:   2023-06-08
* @Version: V1.0
*/
@Api(tags="IT软件月成本")
@RestController
@RequestMapping("/it/itSoftwareMonthlyCostController")
@Slf4j
public class ItSoftwareMonthlyCostController extends JeecgController<ItSoftwareMonthlyCost, IItSoftwareMonthlyCostService> {
   @Autowired
   private IItSoftwareMonthlyCostService itSoftwareMonthlyCostService;

   @Autowired
   private ItSoftwareMonthlyCostBPService itSoftwareMonthlyCostBPService;

    /**
    * 分页列表查询
    *
    * @param itSoftwareMonthlyCost
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   //@AutoLog(value = "IT软件月成本-分页列表查询")
   @ApiOperation(value="IT软件月成本-分页列表查询", notes="IT软件月成本-分页列表查询")
   @GetMapping(value = "/list")
   public Result<IPage<ItSoftwareMonthlyCost>> queryPageList(ItSoftwareMonthlyCost itSoftwareMonthlyCost,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<ItSoftwareMonthlyCost> queryWrapper = QueryGenerator.initQueryWrapper(itSoftwareMonthlyCost, req.getParameterMap());
       Page<ItSoftwareMonthlyCost> page = new Page<ItSoftwareMonthlyCost>(pageNo, pageSize);
       IPage<ItSoftwareMonthlyCost> pageList = itSoftwareMonthlyCostService.page(page, queryWrapper);
       return Result.OK(pageList);
   }

   /**
    *   添加IT软件月成本
    *
    * @param itSoftwareMonthlyCost
    * @return
    */
   @AutoLog(value = "IT软件月成本-添加")
   @ApiOperation(value="IT软件月成本-添加", notes="IT软件月成本-添加")
   //@RequiresPermissions("org.jeecg.modules.demo:it_software_monthly_cost:add")
   @PostMapping(value = "/add")
   public Result<String> add(@RequestBody ItSoftwareMonthlyCost itSoftwareMonthlyCost) {
       itSoftwareMonthlyCostService.save(itSoftwareMonthlyCost);
       return Result.OK("添加成功！");
   }

   /**
    *  编辑
    *
    * @param itSoftwareMonthlyCost
    * @return
    */
   @AutoLog(value = "IT软件月成本-编辑")
   @ApiOperation(value="IT软件月成本-编辑", notes="IT软件月成本-编辑")
   //@RequiresPermissions("org.jeecg.modules.demo:it_software_monthly_cost:edit")
   @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
   public Result<String> edit(@RequestBody ItSoftwareMonthlyCost itSoftwareMonthlyCost) {
       itSoftwareMonthlyCostService.updateById(itSoftwareMonthlyCost);
       return Result.OK("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "IT软件月成本-通过id删除")
   @ApiOperation(value="IT软件月成本-通过id删除", notes="IT软件月成本-通过id删除")
   //@RequiresPermissions("org.jeecg.modules.demo:it_software_monthly_cost:delete")
   @DeleteMapping(value = "/delete")
   public Result<String> delete(@RequestParam(name="id",required=true) String id) {
       itSoftwareMonthlyCostService.removeById(id);
       return Result.OK("删除成功!");
   }

   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @AutoLog(value = "IT软件月成本-批量删除")
   @ApiOperation(value="IT软件月成本-批量删除", notes="IT软件月成本-批量删除")
   //@RequiresPermissions("org.jeecg.modules.demo:it_software_monthly_cost:deleteBatch")
   @DeleteMapping(value = "/deleteBatch")
   public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.itSoftwareMonthlyCostService.removeByIds(Arrays.asList(ids.split(",")));
       return Result.OK("批量删除成功!");
   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   //@AutoLog(value = "文档版本-通过id查询")
   @ApiOperation(value="IT软件月成本-通过id查询", notes="IT软件月成本-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<ItSoftwareMonthlyCost> queryById(@RequestParam(name="id",required=true) String id) {
       ItSoftwareMonthlyCost itSoftwareMonthlyCost = itSoftwareMonthlyCostService.getById(id);
       if(itSoftwareMonthlyCost==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(itSoftwareMonthlyCost);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param itSoftwareMonthlyCost
   */
   //@RequiresPermissions("org.jeecg.modules.demo:it_software_monthly_cost:exportXls")
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, ItSoftwareMonthlyCost itSoftwareMonthlyCost) {
       return super.exportXls(request, itSoftwareMonthlyCost, ItSoftwareMonthlyCost.class, "IT软件月成本");
   }

   /**
     * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
   //@RequiresPermissions("it_software_monthly_cost:importExcel")
   @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
   public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
       return super.importExcel(request, response, ItSoftwareMonthlyCost.class);
   }

   @Autowired
   private FeishuMessageUtils feishuMessageUtils;
   @ApiOperation(value="IT软件月成本-生成某年某月的月成本", notes="IT软件月成本-生成某年某月的月成本")
   @RequestMapping(value="/generateMonthlyCost", method = RequestMethod.POST)
   public Result<?> generateMonthlyCost(@RequestParam(name="year",required=true) String year, @RequestParam(name="month",required=true) String month){
       itSoftwareMonthlyCostBPService.generateMonthlyCost(year,month);
       //itSoftwareMonthlyCostBPService.validate(year, month);
       feishuMessageUtils.sendFeiShuMsg(String.format("%s年%s月软件价格记录生成完成",year, month));
       return Result.OK("生成完成");
   }

}
