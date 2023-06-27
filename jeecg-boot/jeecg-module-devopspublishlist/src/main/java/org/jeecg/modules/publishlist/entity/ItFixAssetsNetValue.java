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
 * @Description: 固定资产净值历史表
 * @Author: jeecg-boot
 * @Date:   2023-06-27
 * @Version: V1.0
 */
@Data
@TableName("it_fix_assets_net_value")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="it_fix_assets_net_value对象", description="固定资产净值历史表")
public class ItFixAssetsNetValue implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**固定资产编号*/
	@Excel(name = "固定资产编号", width = 15)
    @ApiModelProperty(value = "固定资产编号")
    private java.lang.String fixAssertsId;
	/**净值*/
	@Excel(name = "净值", width = 15)
    @ApiModelProperty(value = "净值")
    private java.lang.Double netValue;
	/**年*/
	@Excel(name = "年", width = 15)
    @ApiModelProperty(value = "年")
    private java.lang.Integer year;
	/**月*/
	@Excel(name = "月", width = 15)
    @ApiModelProperty(value = "月")
    private java.lang.Integer month;
	/**日*/
	@Excel(name = "日", width = 15)
    @ApiModelProperty(value = "日")
    private java.lang.Integer day;
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
