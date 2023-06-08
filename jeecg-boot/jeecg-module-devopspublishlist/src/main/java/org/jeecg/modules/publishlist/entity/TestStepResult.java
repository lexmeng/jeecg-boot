package org.jeecg.modules.publishlist.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 雷神测试结果数据
 * @Author: jeecg-boot
 * @Date:   2023-06-08
 * @Version: V1.0
 */
@Data
@TableName("test_step_result")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="test_step_result对象", description="雷神测试结果数据")
public class TestStepResult implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**jenkins任务号*/
	@Excel(name = "jenkins任务号", width = 15)
    @ApiModelProperty(value = "jenkins任务号")
    private java.lang.String jenkinsJobNum;
	/**计划名*/
	@Excel(name = "计划名", width = 15)
    @ApiModelProperty(value = "计划名")
    private java.lang.String planName;
	/**报告链接*/
	@Excel(name = "报告链接", width = 15)
    @ApiModelProperty(value = "报告链接")
    private java.lang.String reportLink;
	/**用例总数*/
	@Excel(name = "用例总数", width = 15)
    @ApiModelProperty(value = "用例总数")
    private java.lang.String totalCaseNum;
	/**失败用例数*/
	@Excel(name = "失败用例数", width = 15)
    @ApiModelProperty(value = "失败用例数")
    private java.lang.String failedCaseNum;
	/**通过用例数*/
	@Excel(name = "通过用例数", width = 15)
    @ApiModelProperty(value = "通过用例数")
    private java.lang.String passedCaseNum;
	/**总耗时*/
	@Excel(name = "总耗时", width = 15)
    @ApiModelProperty(value = "总耗时")
    private java.lang.Integer timeConsumed;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
}
