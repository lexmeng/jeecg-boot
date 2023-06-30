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
 * @Description: quard job请求表
 * @Author: jeecg-boot
 * @Date:   2023-06-02
 * @Version: V1.0
 */
@Data
@TableName("test_quard_request")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="test_quard_request对象", description="quard job请求表")
public class TestQuardRequest implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**测试任务类别*/
	@Excel(name = "测试任务类别", width = 15)
    @ApiModelProperty(value = "测试任务类别")
    private java.lang.String testType;
	/**平台名*/
	@Excel(name = "平台名", width = 15)
    @ApiModelProperty(value = "平台名")
    private java.lang.String platformName;
	/**quard代码仓库*/
	@Excel(name = "quard代码仓库", width = 15)
    @ApiModelProperty(value = "quard代码仓库")
    private java.lang.String quardRepo;
	/**quard代码分支*/
	@Excel(name = "quard代码分支", width = 15)
    @ApiModelProperty(value = "quard代码分支")
    private java.lang.String quardBranch;
	/**升级回滚时的当前版本*/
	@Excel(name = "升级回滚时的当前版本", width = 15)
    @ApiModelProperty(value = "升级回滚时的当前版本")
    private java.lang.String upgradeFromNum;
	/**升级回滚时的升级版本*/
	@Excel(name = "升级回滚时的升级版本", width = 15)
    @ApiModelProperty(value = "升级回滚时的升级版本")
    private java.lang.String upgradeToNum;
	/**测试用例集合*/
	@Excel(name = "测试用例集合", width = 15)
    @ApiModelProperty(value = "测试用例集合")
    private java.lang.String selectTests;
	/**测试包链接*/
	@Excel(name = "测试包链接", width = 15)
    @ApiModelProperty(value = "测试包链接")
    private java.lang.String packageUrl;
	/**jenkins job号*/
	@Excel(name = "jenkins job号", width = 15)
    @ApiModelProperty(value = "jenkins job号")
    private java.lang.String jenkinsJobNum;
	/**jenkins job url*/
	@Excel(name = "jenkins job url", width = 15)
    @ApiModelProperty(value = "jenkins job url")
    private java.lang.String jenkinsJobUrl;
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
