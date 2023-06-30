package org.jeecg.modules.devops.entity;
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
 * @Description: pr commit表-跟pr ut一一对应
 * @Author: jeecg-boot
 * @Date:   2023-06-12
 * @Version: V1.0
 */
@Data
@TableName("dev_pr_commit")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="dev_pr_commit对象", description="pr commit表-跟pr ut一一对应")
public class DevPrCommit implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**commit id*/
	@Excel(name = "commit id", width = 15)
    @ApiModelProperty(value = "commit id")
    private java.lang.String commitId;
	/**pr id*/
	@Excel(name = "pr id", width = 15)
    @ApiModelProperty(value = "pr id")
    private java.lang.String prId;
	/**issue id*/
	@Excel(name = "issue id", width = 15)
    @ApiModelProperty(value = "issue id")
    private java.lang.String issueId;
	/**项目id*/
	@Excel(name = "项目id", width = 15)
    @ApiModelProperty(value = "项目id")
    private java.lang.String projectId;
	/**jira 版本id*/
	@Excel(name = "jira 版本id", width = 15)
    @ApiModelProperty(value = "jira 版本id")
    private java.lang.String jiraVersionName;
	/**pr名*/
	@Excel(name = "pr名", width = 15)
    @ApiModelProperty(value = "pr名")
    private java.lang.String prName;
	/**评论数*/
	@Excel(name = "评论数", width = 15)
    @ApiModelProperty(value = "评论数")
    private java.lang.Integer commentCount;
	/**源分支*/
	@Excel(name = "源分支", width = 15)
    @ApiModelProperty(value = "源分支")
    private java.lang.String sourceBranch;
	/**目的分支*/
	@Excel(name = "目的分支", width = 15)
    @ApiModelProperty(value = "目的分支")
    private java.lang.String destinationBranch;
	/**pr状态*/
	@Excel(name = "pr状态", width = 15)
    @ApiModelProperty(value = "pr状态")
    private java.lang.String prStatus;
	/**pr的url*/
	@Excel(name = "pr的url", width = 15)
    @ApiModelProperty(value = "pr的url")
    private java.lang.String prUrl;
	/**pr的最新更新*/
	@Excel(name = "pr的最新更新", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "pr的最新更新")
    private java.util.Date prLastUpdate;
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
