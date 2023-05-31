package org.jeecg.modules.publishlist.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 发布单
 * @Author: jeecg-boot
 * @Date:   2023-04-17
 * @Version: V1.0
 */
@Data
@TableName("pub_publishlist")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pub_publishlist对象", description="发布单")
public class Publishlist implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
    /**发布单名*/
    @Excel(name = "发布单名", width = 15)
    @ApiModelProperty(value = "发布单名")
    private String name;

    /**产品线名*/
    @Excel(name = "产品线名", width = 15)
    @ApiModelProperty(value = "产品线名")
    @Dict(dicCode = "dict_bu")
    private String productLineName;

    /**产品名*/
    @Excel(name = "产品名", width = 15)
    @ApiModelProperty(value = "产品名")
    @Dict(dicCode = "product")
    private String productName;
    /**版本名*/
    @Excel(name = "版本名", width = 15)
    @ApiModelProperty(value = "版本名")
    private String versionName;
    /**版本类型*/
    @Excel(name = "版本类型", width = 15)
    @ApiModelProperty(value = "版本类型")
    @Dict(dicCode = "version_type")
    private String versionType;
    /**jira版本名*/
    @Excel(name = "jira版本名", width = 15)
    @ApiModelProperty(value = "jira版本名")
    private String jiraVersionName;
    /**文档版本*/
    @Excel(name = "文档版本", width = 15)
    @ApiModelProperty(value = "文档版本")
    private String documentVersion;
    /**迭代冲刺号*/
    @Excel(name = "迭代冲刺号", width = 15)
    @ApiModelProperty(value = "迭代冲刺号")
    private String scrumNum;
    /**迭代阶段*/
    @Excel(name = "迭代阶段", width = 15)
    @ApiModelProperty(value = "迭代阶段")
    @Dict(dicCode = "sprint_stage")
    private String scrumStage;
    /**发布单状态*/
    @Excel(name = "发布单状态", width = 15)
    @ApiModelProperty(value = "发布单状态")
    @Dict(dicCode = "release_form_state")
    private String publishlistStage;
    /**发布时间*/
    @Excel(name = "发布时间", width = 15)
    @ApiModelProperty(value = "发布时间")
    private String publishDatetime;
    /**产品经理id*/
    @Excel(name = "产品经理id", width = 15)
    @ApiModelProperty(value = "产品经理id")
    @Dict(dicCode = "username", dicText = "realname", dictTable = "sys_user")
    private String pmId;
    /**产品经理名*/
    @Excel(name = "产品经理名", width = 15)
    @ApiModelProperty(value = "产品经理名")
    private String pmName;
    /**commid id*/
    @Excel(name = "commit_id", width = 15)
    @ApiModelProperty(value = "commit_id")
    private java.lang.String commitId;
    /**页内链接ID*/
    @Excel(name = "页内链接ID", width = 15)
    @ApiModelProperty(value = "页内链接ID")
    private String documentUrlId;
    /**用户手册中文链接*/
    @Excel(name = "用户手册中文链接", width = 15)
    @ApiModelProperty(value = "用户手册中文链接")
    private java.lang.String userManualEnLink;
    /**用户手册英文链接*/
    @Excel(name = "用户手册英文链接", width = 15)
    @ApiModelProperty(value = "用户手册英文链接")
    private java.lang.String userManualChLink;
	/**产品行文变更文档链接*/
	@Excel(name = "产品行文变更文档链接", width = 15)
    @ApiModelProperty(value = "产品行文变更文档链接")
    private String productChangeDocLink;
	/**发布日期*/
	@Excel(name = "发布日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发布日期")
    private Date releaseDate;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
    /**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**更新日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
}
