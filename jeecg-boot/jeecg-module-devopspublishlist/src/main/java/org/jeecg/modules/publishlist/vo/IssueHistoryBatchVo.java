package org.jeecg.modules.publishlist.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.publishlist.entity.IssueHistory;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

import java.util.List;

@Data
@ApiModel(value="Issue历史批次", description="Issue历史批次")
public class IssueHistoryBatchVo {

    @Excel(name = "批次号", width = 15)
    @ApiModelProperty(value = "批次号")
    private Integer batchNum;

    @ExcelCollection(name="增加的Issue集")
    @ApiModelProperty(value = "增加的Issue集")
    private List<IssueHistory> addIssueList;

    @ExcelCollection(name="减少的Issue集")
    @ApiModelProperty(value = "减少的Issue集")
    private List<IssueHistory> subIssueList;


}
