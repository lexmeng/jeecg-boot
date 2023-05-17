package org.jeecg.modules.publishlist.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.publishlist.entity.DependentComponent;
import org.jeecg.modules.publishlist.entity.PackageUrl;
import org.jeecg.modules.publishlist.entity.Publishlist;
import org.jeecg.modules.publishlist.entity.PublishlistProject;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value="publishlistQueryResult对象", description="发布单查询结果")
public class PublishlistQueryResult implements Serializable {
    @ExcelCollection(name="发布单")
    @ApiModelProperty(value = "发布单")
    private Publishlist publishlist;
    @ExcelCollection(name="依赖组件")
    @ApiModelProperty(value = "依赖组件")
    private List<DependentComponent> dependentComponentList;

    @ExcelCollection(name="产品包的下载地址")
    @ApiModelProperty(value = "产品包的下载地址")
    private List<PackageUrl> packageUrlList;

    @ExcelCollection(name="发布单项目表")
    @ApiModelProperty(value = "发布单项目表")
    private List<PublishlistProject> publishlistProjectList;
}
