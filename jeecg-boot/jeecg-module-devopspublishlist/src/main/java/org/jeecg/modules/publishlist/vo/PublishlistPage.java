package org.jeecg.modules.publishlist.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.publishlist.entity.DependentComponent;
import org.jeecg.modules.publishlist.entity.PackageUrl;
import org.jeecg.modules.publishlist.entity.PublishlistProject;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 发布单
 * @Author: jeecg-boot
 * @Date:   2023-04-17
 * @Version: V1.0
 */
@Data
@ApiModel(value="publishlistPage对象", description="发布单")
public class PublishlistPage {

	/**主键*/
	@ApiModelProperty(value = "主键")
    private String id;
	/**发布单名*/
	@Excel(name = "发布单名", width = 15)
	@ApiModelProperty(value = "发布单名")
    private String name;
	/**产品线id*/
	@Excel(name = "产品线id", width = 15)
	@ApiModelProperty(value = "产品线id")
    private String productLineId;
	/**产品线名*/
	@Excel(name = "产品线名", width = 15)
	@ApiModelProperty(value = "产品线名")
	@Dict(dicCode = "dict_bu")
    private String productLineName;
	/**产品id*/
	@Excel(name = "产品id", width = 15)
	@ApiModelProperty(value = "产品id")
    private String productId;
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
	@Excel(name = "commid id", width = 15)
	@ApiModelProperty(value = "commid id")
    private String commitId;
	/**用户手册中文链接*/
	@Excel(name = "用户手册中文链接", width = 15)
	@ApiModelProperty(value = "用户手册中文链接")
    private String userManualEnLink;
	/**用户手册英文链接*/
	@Excel(name = "用户手册英文链接", width = 15)
	@ApiModelProperty(value = "用户手册英文链接")
    private String userManualChLink;
	/**产品行文变更文档链接*/
	@ApiModelProperty(value = "产品行文变更文档链接")
	private String productChangeDocLink;
	/**发布日期*/
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
    private java.util.Date createTime;
	/**更新人*/
	@ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;

	@ExcelCollection(name="发布单项目表")
	@ApiModelProperty(value = "发布单项目表")
	private List<PublishlistProject> publishlistProjectList;
	@ExcelCollection(name="依赖组件")
	@ApiModelProperty(value = "依赖组件")
	private List<DependentComponent> dependentComponentList;
	@ExcelCollection(name="产品包的下载地址")
	@ApiModelProperty(value = "产品包的下载地址")
	private List<PackageUrl> packageUrlList;

}
