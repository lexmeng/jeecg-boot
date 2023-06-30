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
 * @Description: 固定资产表
 * @Author: jeecg-boot
 * @Date:   2023-06-27
 * @Version: V1.0
 */
@Data
@TableName("it_fa")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="it_fa对象", description="固定资产表")
public class ItFa implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
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
	/**固定资产编号*/
	@Excel(name = "固定资产编号", width = 15)
    @ApiModelProperty(value = "固定资产编号")
    private java.lang.String fixedAssetsNumber;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private java.lang.String equipmentName;
	/**设备型号*/
	@Excel(name = "设备型号", width = 15)
    @ApiModelProperty(value = "设备型号")
    private java.lang.String equipmentModel;
	/**使用状态*/
	@Excel(name = "使用状态", width = 15, dicCode = "use_type")
	@Dict(dicCode = "use_type")
    @ApiModelProperty(value = "使用状态")
    private java.lang.String useState;
	/**使用部门*/
	@Excel(name = "使用部门", width = 15)
    @Dict(dicCode = "use_type")
    @ApiModelProperty(value = "使用部门")
    private java.lang.String useOrgCode;
	/**保管人员*/
	@Excel(name = "保管人员", width = 15)
    @ApiModelProperty(value = "保管人员")
    private java.lang.String useOwner;
	/**存放地点*/
	@Excel(name = "存放地点", width = 15, dicCode = "it_site")
	@Dict(dicCode = "it_site")
    @ApiModelProperty(value = "存放地点")
    private java.lang.String site;
	/**设备原值*/
	@Excel(name = "设备原值", width = 15)
    @ApiModelProperty(value = "设备原值")
    private java.lang.Double equOriValue;
	/**设备净值*/
	@Excel(name = "设备净值", width = 15)
    @ApiModelProperty(value = "设备净值")
    private java.lang.Double equNetValue;
	/**设备分类*/
	@Excel(name = "设备分类", width = 15, dicCode = "equ_type")
	@Dict(dicCode = "equ_type")
    @ApiModelProperty(value = "设备分类")
    private java.lang.String equipmentClass;
	/**时间*/
	@Excel(name = "时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "时间")
    private java.util.Date itTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remarks;
}
