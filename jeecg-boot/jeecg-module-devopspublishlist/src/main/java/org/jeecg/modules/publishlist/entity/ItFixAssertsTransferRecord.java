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
 * @Description: 固定资产移交记录
 * @Author: jeecg-boot
 * @Date:   2023-06-27
 * @Version: V1.0
 */
@Data
@TableName("it_fix_asserts_transfer_record")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="it_fix_asserts_transfer_record对象", description="固定资产移交记录")
public class ItFixAssertsTransferRecord implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**固定资产id*/
	@Excel(name = "固定资产id", width = 15)
    @ApiModelProperty(value = "固定资产id")
    private java.lang.String fixAssertsId;
	/**原保管人员*/
	@Excel(name = "原保管人员", width = 15)
    @ApiModelProperty(value = "原保管人员")
    private java.lang.String fromOwnName;
	/**原部门*/
	@Excel(name = "原部门", width = 15)
    @ApiModelProperty(value = "原部门")
    private java.lang.String fromDepartment;
	/**原存放地点*/
	@Excel(name = "原存放地点", width = 15)
    @ApiModelProperty(value = "原存放地点")
    private java.lang.String fromLocation;
	/**原使用状态*/
	@Excel(name = "原使用状态", width = 15)
    @ApiModelProperty(value = "原使用状态")
    private java.lang.String fromUseStatus;
	/**目的保管人员*/
	@Excel(name = "目的保管人员", width = 15)
    @ApiModelProperty(value = "目的保管人员")
    private java.lang.String toOwnerName;
	/**目的部门*/
	@Excel(name = "目的部门", width = 15)
    @ApiModelProperty(value = "目的部门")
    private java.lang.String toDepartment;
	/**目的存放地点*/
	@Excel(name = "目的存放地点", width = 15)
    @ApiModelProperty(value = "目的存放地点")
    private java.lang.String toLocation;
	/**目的使用状态*/
	@Excel(name = "目的使用状态", width = 15)
    @ApiModelProperty(value = "目的使用状态")
    private java.lang.String toUseStatus;
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
