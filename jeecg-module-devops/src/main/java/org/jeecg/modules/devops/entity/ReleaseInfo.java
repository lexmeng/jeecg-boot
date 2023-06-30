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
 * @Description: release信息表
 * @Author: jeecg-boot
 * @Date:   2023-04-18
 * @Version: V1.0
 */
@Data
//@TableName("release_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
//@ApiModel(value="release_info对象", description="release信息表")
public class ReleaseInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**issue号*/
	@Excel(name = "issue号", width = 15)
    @ApiModelProperty(value = "issue号")
    private String issueNum;
	/**issue名*/
	@Excel(name = "issue名", width = 15)
    @ApiModelProperty(value = "issue名")
    private String issueName;
	/**issue英文名*/
	@Excel(name = "issue英文名", width = 15)
    @ApiModelProperty(value = "issue英文名")
    private String issueEnName;
	/**issue中文名*/
	@Excel(name = "issue中文名", width = 15)
    @ApiModelProperty(value = "issue中文名")
    private String issueChName;
	/**发布单id*/
	@Excel(name = "发布单id", width = 15)
    @ApiModelProperty(value = "发布单id")
    private String publishlistId;
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
}
