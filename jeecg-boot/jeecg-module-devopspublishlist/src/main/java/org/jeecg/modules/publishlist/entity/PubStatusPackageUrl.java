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
 * @Description: 发布单不同状态的package url
 * @Author: jeecg-boot
 * @Date:   2023-06-07
 * @Version: V1.0
 */
@Data
@TableName("pub_status_package_url")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pub_status_package_url对象", description="发布单不同状态的package url")
public class PubStatusPackageUrl implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**产品包url*/
	@Excel(name = "产品包url", width = 15)
    @ApiModelProperty(value = "产品包url")
    private java.lang.String packageUrl;
    /**产品包名*/
    @Excel(name = "产品包名", width = 15)
    @ApiModelProperty(value = "产品包名")
    private java.lang.String name;
	/**发布单状态*/
	@Excel(name = "发布单状态", width = 15)
    @ApiModelProperty(value = "发布单状态")
    private java.lang.String pubStatus;
	/**存储类型*/
	@Excel(name = "存储类型", width = 15)
    @ApiModelProperty(value = "存储类型")
    private java.lang.String storageType;
	/**产品线名*/
	@Excel(name = "产品线名", width = 15)
    @ApiModelProperty(value = "产品线名")
    private java.lang.String productLineName;
	/**产品名*/
	@Excel(name = "产品名", width = 15)
    @ApiModelProperty(value = "产品名")
    private java.lang.String productName;
	/**版本*/
	@Excel(name = "版本", width = 15)
    @ApiModelProperty(value = "版本")
    private java.lang.String version;
	/**版本类型*/
	@Excel(name = "版本类型", width = 15)
    @ApiModelProperty(value = "版本类型")
    private java.lang.String versionType;
	/**发布单id*/
	@Excel(name = "发布单id", width = 15)
    @ApiModelProperty(value = "发布单id")
    private java.lang.String publishlistId;
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
