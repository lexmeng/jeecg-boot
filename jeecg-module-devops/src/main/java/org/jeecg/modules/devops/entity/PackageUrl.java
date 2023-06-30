package org.jeecg.modules.devops.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 产品包的下载地址
 * @Author: jeecg-boot
 * @Date:   2023-05-06
 * @Version: V1.0
 */
@Data
@TableName("pub_package_url")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pub_package_url对象", description="产品包的下载地址")
public class PackageUrl implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**云存储类型*/
	@Excel(name = "云存储类型", width = 15)
    @ApiModelProperty(value = "云存储类型")
    private java.lang.String storageType;
	/**包url*/
	@Excel(name = "包url", width = 15)
    @ApiModelProperty(value = "包url")
    private java.lang.String packageUrl;
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
}
