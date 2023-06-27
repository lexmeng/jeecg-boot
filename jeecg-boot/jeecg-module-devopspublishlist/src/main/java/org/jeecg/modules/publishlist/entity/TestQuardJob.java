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
 * @Description: quard 发起任务
 * @Author: jeecg-boot
 * @Date:   2023-06-24
 * @Version: V1.0
 */
@Data
@TableName("test_quard_job")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="test_quard_job对象", description="quard 发起任务")
public class TestQuardJob implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/** 发布单 id*/
	@Excel(name = " 发布单 id", width = 15)
    @ApiModelProperty(value = " 发布单 id")
    private String publishlistId;
	/**测试类型*/
	@Excel(name = "测试类型", width = 15, dicCode = "quard_type")
	@Dict(dicCode = "quard_type")
    @ApiModelProperty(value = "测试类型")
    private String quardType;
	/**测试平台版本*/
	@Excel(name = "测试平台版本", width = 15, dictTable = "test_platform", dicText = "name", dicCode = "name")
	@Dict(dictTable = "test_platform", dicText = "name", dicCode = "name")
    @ApiModelProperty(value = "测试平台版本")
    private String quardPlatform;
	/**quard代码分支*/
	@Excel(name = "quard代码分支", width = 15)
    @ApiModelProperty(value = "quard代码分支")
    private String quardBranch;
	/**升级回滚版本号*/
	@Excel(name = "升级回滚版本号", width = 15)
    @ApiModelProperty(value = "升级回滚版本号")
    private String fromVersion;
	/**目标版本号*/
	@Excel(name = "目标版本号", width = 15)
    @ApiModelProperty(value = "目标版本号")
    private String targetVersion;
	/**指定 case*/
	@Excel(name = "指定 case", width = 15)
    @ApiModelProperty(value = "指定 case")
    private String selectTests;
	/**包存放地址*/
	@Excel(name = "包存放地址", width = 15)
    @ApiModelProperty(value = "包存放地址")
    private String packageUrl;
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
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
}
