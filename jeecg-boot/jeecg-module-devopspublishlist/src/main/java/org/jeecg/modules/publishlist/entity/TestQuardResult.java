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
 * @Description: Quard测试结果记录
 * @Author: jeecg-boot
 * @Date:   2023-06-02
 * @Version: V1.0
 */
@Data
@TableName("test_quard_result")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="test_quard_result对象", description="Quard测试结果记录")
public class TestQuardResult implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**quard任务请求号*/
	@Excel(name = "quard任务请求号", width = 15)
    @ApiModelProperty(value = "quard任务请求号")
    private java.lang.String quardRequestId;
	/**jenkins job 号*/
	@Excel(name = "jenkins job 号", width = 15)
    @ApiModelProperty(value = "jenkins job 号")
    private java.lang.String jenkinsJobNum;
	/**总用例数*/
	@Excel(name = "总用例数", width = 15)
    @ApiModelProperty(value = "总用例数")
    private java.lang.Integer totalCaseNum;
	/**失败用例数*/
	@Excel(name = "失败用例数", width = 15)
    @ApiModelProperty(value = "失败用例数")
    private java.lang.Integer failedCaseNum;
	/**阻塞用例数*/
	@Excel(name = "阻塞用例数", width = 15)
    @ApiModelProperty(value = "阻塞用例数")
    private java.lang.Integer brokenCaseNum;
	/**跳过用例数*/
	@Excel(name = "跳过用例数", width = 15)
    @ApiModelProperty(value = "跳过用例数")
    private java.lang.Integer skippedCaseNum;
	/**通过用例数*/
	@Excel(name = "通过用例数", width = 15)
    @ApiModelProperty(value = "通过用例数")
    private java.lang.Integer passedCaseNum;
	/**未知用例数*/
	@Excel(name = "未知用例数", width = 15)
    @ApiModelProperty(value = "未知用例数")
    private java.lang.Integer unknownCaseNum;
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
