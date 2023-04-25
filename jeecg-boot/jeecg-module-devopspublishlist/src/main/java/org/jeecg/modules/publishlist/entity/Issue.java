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
 * @Description: issue本地记录
 * @Author: jeecg-boot
 * @Date:   2023-04-18
 * @Version: V1.0
 */
@Data
@TableName("issue")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="issue对象", description="issue本地记录")
public class Issue implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**jira的issue号*/
	@Excel(name = "jira的issue号", width = 15)
    @ApiModelProperty(value = "jira的issue号")
    private String issueNum;
	/**jira的issue名*/
	@Excel(name = "jira的issue名", width = 15)
    @ApiModelProperty(value = "jira的issue名")
    private String issueName;
	/**jira的issue类型*/
	@Excel(name = "jira的issue类型", width = 15)
    @ApiModelProperty(value = "jira的issue类型")
    private String issueType;
	/**jira的issue链接*/
	@Excel(name = "jira的issue链接", width = 15)
    @ApiModelProperty(value = "jira的issue链接")
    private String issueLink;
	/**发布单号*/
	@Excel(name = "发布单号", width = 15)
    @ApiModelProperty(value = "发布单号")
    private String publishlistId;
	/**项目id*/
	@Excel(name = "项目id", width = 15)
    @ApiModelProperty(value = "项目id")
    private String projectId;
	/**jira版本名*/
	@Excel(name = "jira版本名", width = 15)
    @ApiModelProperty(value = "jira版本名")
    private String jiraVersionName;
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
