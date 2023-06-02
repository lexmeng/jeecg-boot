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
 * @Description: Quard 测试过程数据
 * @Author: jeecg-boot
 * @Date:   2023-06-02
 * @Version: V1.0
 */
@Data
@TableName("test_quard_task")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="test_quard_task对象", description="Quard 测试过程数据")
public class TestQuardTask implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**jenkins任务号*/
	@Excel(name = "jenkins任务号", width = 15)
    @ApiModelProperty(value = "jenkins任务号")
    private java.lang.String jenkinsJobNum;
	/** 平台名*/
	@Excel(name = " 平台名", width = 15)
    @ApiModelProperty(value = " 平台名")
    private java.lang.String platformName;
	/**开始时间*/
	@Excel(name = "开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private java.util.Date startTime;
	/**截止时间*/
	@Excel(name = "截止时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "截止时间")
    private java.util.Date endTime;
	/**产品版本号*/
	@Excel(name = "产品版本号", width = 15)
    @ApiModelProperty(value = "产品版本号")
    private java.lang.String version;
	/**测试阶段*/
	@Excel(name = "测试阶段", width = 15)
    @ApiModelProperty(value = "测试阶段")
    private java.lang.String quardStage;
	/**测试类型*/
	@Excel(name = "测试类型", width = 15)
    @ApiModelProperty(value = "测试类型")
    private java.lang.String testType;
	/**升级回滚开始版本*/
	@Excel(name = "升级回滚开始版本", width = 15)
    @ApiModelProperty(value = "升级回滚开始版本")
    private java.lang.String upgradeFrom;
	/**升级回滚目标版本*/
	@Excel(name = "升级回滚目标版本", width = 15)
    @ApiModelProperty(value = "升级回滚目标版本")
    private java.lang.String upgradeTo;
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
