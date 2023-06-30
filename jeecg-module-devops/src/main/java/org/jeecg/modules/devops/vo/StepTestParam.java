package org.jeecg.modules.devops.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="StepTestParam对象", description="Step测试请求参数")
public class StepTestParam {
    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "isCh")
    private Boolean isCh;

    @ApiModelProperty(value = "是否重新部署【159.27.224.211/gw05】环境")
    private Boolean redeployGw05;

    @ApiModelProperty(value = "是否重新部署【159.27.224.168/gw06】环境")
    private Boolean redeployGw06;

    @ApiModelProperty(value = "是否重新部署【159.27.120.134/gw08】环境")
    private Boolean redeployGw08;

    @ApiModelProperty(value = "Step库测试计划名称")
    private String planName;

    @ApiModelProperty(value = "产品线")
    private String productline;

    @ApiModelProperty(value = "版本名")
    private String versionName;

    @ApiModelProperty(value = "阶段")
    private String envStage;

    @ApiModelProperty(value = "检查时间")
    private String checkTime;
}
