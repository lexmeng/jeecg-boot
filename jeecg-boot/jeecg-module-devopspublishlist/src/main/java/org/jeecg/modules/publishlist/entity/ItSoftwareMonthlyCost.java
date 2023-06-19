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
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: it_software_monthly_cost
 * @Author: jeecg-boot
 * @Date:   2023-06-12
 * @Version: V1.0
 */
@Data
@TableName("it_software_monthly_cost")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="it软件月成本", description="it软件月成本")
public class ItSoftwareMonthlyCost implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;

	/**software name*/
	@Excel(name = "软件名", width = 15)
    @ApiModelProperty(value = "软件名")
    private String softwareName;

    /**month*/
    @Excel(name = "月份", width = 15)
    @ApiModelProperty(value = "月份")
    private Integer month;

    /**year*/
    @Excel(name = "年份", width = 15)
    @ApiModelProperty(value = "年份")
    private Integer year;

    /**cost*/
    @Excel(name = "成本", width = 15)
    @ApiModelProperty(value = "成本")
    private Double cost;

    /**owner*/
    @Excel(name = "拥有者", width = 15)
    @ApiModelProperty(value = "拥有者")
    private String owner;

    /**department*/
    @Excel(name = "部门", width = 15)
    @ApiModelProperty(value = "部门")
    private String department;

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
