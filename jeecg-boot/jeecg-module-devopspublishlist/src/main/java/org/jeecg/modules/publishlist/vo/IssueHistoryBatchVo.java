package org.jeecg.modules.publishlist.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.publishlist.entity.IssueHistory;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

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

    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**更新日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;


}
