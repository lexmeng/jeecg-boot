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
 * @Description: pr的ci ut结果表
 * @Author: jeecg-boot
 * @Date:   2023-06-08
 * @Version: V1.0
 */
@Data
@TableName("dev_ci_ut_pr")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="dev_ci_ut_pr对象", description="pr的ci ut结果表")
public class DevCiUtPr implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**产品线名*/
	@Excel(name = "产品线名", width = 15)
    @ApiModelProperty(value = "产品线名")
    private java.lang.String productLineName;
	/**产品名*/
	@Excel(name = "产品名", width = 15)
    @ApiModelProperty(value = "产品名")
    private java.lang.String productName;
	/**版本*/
	@Excel(name = "版本", width = 15)
    @ApiModelProperty(value = "版本")
    private java.lang.String versionName;
	/**版本类型*/
	@Excel(name = "版本类型", width = 15)
    @ApiModelProperty(value = "版本类型")
    private java.lang.String versionType;
	/**分支*/
	@Excel(name = "分支", width = 15)
    @ApiModelProperty(value = "分支")
    private java.lang.String branch;
	/**jenkins任务号*/
	@Excel(name = "jenkins任务号", width = 15)
    @ApiModelProperty(value = "jenkins任务号")
    private java.lang.String jenkinsJobNum;
	/**pr commit id*/
	@Excel(name = "pr commit id", width = 15)
    @ApiModelProperty(value = "pr commit id")
    private java.lang.String prbAcutalCommit;
	/**pr id*/
	@Excel(name = "pr id", width = 15)
    @ApiModelProperty(value = "pr id")
    private java.lang.String prbPullId;
	/**pr目标分支*/
	@Excel(name = "pr目标分支", width = 15)
    @ApiModelProperty(value = "pr目标分支")
    private java.lang.String prbTargetBranch;
	/**pr源分支*/
	@Excel(name = "pr源分支", width = 15)
    @ApiModelProperty(value = "pr源分支")
    private java.lang.String prbSourceBranch;
	/**git分支*/
	@Excel(name = "git分支", width = 15)
    @ApiModelProperty(value = "git分支")
    private java.lang.String gitBranch;
	/**pr描述*/
	@Excel(name = "pr描述", width = 15)
    @ApiModelProperty(value = "pr描述")
    private java.lang.String prbPullDescription;
	/**pr标题*/
	@Excel(name = "pr标题", width = 15)
    @ApiModelProperty(value = "pr标题")
    private java.lang.String prbPullTitle;
	/**pr链接*/
	@Excel(name = "pr链接", width = 15)
    @ApiModelProperty(value = "pr链接")
    private java.lang.String prbPullLink;
	/**pr长描述*/
	@Excel(name = "pr长描述", width = 15)
    @ApiModelProperty(value = "pr长描述")
    private java.lang.String prbPullLongDescription;
	/**pr comment*/
	@Excel(name = "pr comment", width = 15)
    @ApiModelProperty(value = "pr comment")
    private java.lang.String prbCommentBody;
	/**仓库*/
	@Excel(name = "仓库", width = 15)
    @ApiModelProperty(value = "仓库")
    private java.lang.String prbGhRepository;
	/**credential id*/
	@Excel(name = "credential id", width = 15)
    @ApiModelProperty(value = "credential id")
    private java.lang.String prbCredentialsId;
	/**ut开始时间*/
	@Excel(name = "ut开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "ut开始时间")
    private java.util.Date utBeginTime;
	/**ut结束时间*/
	@Excel(name = "ut结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "ut结束时间")
    private java.util.Date utEndTime;
	/**ut结果*/
	@Excel(name = "ut结果", width = 15)
    @ApiModelProperty(value = "ut结果")
    private java.lang.String result;
	/**提取出的issue名*/
	@Excel(name = "提取出的issue名", width = 15)
    @ApiModelProperty(value = "提取出的issue名")
    private java.lang.String issueName;
	/**根据issue名关联的issue号*/
	@Excel(name = "根据issue名关联的issue号", width = 15)
    @ApiModelProperty(value = "根据issue名关联的issue号")
    private java.lang.String issueNum;
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
