package org.jeecg.modules.publishlist.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="Quard测试过程数据对象", description="Quard测试过程数据")
public class QuardTestProcess {

    @ApiModelProperty(value = "当前测试平台名")
    private String platform;

    @ApiModelProperty(value = "jenkins的Job号")
    private String jenkinsJobId;

    @ApiModelProperty(value = "当前阶段开始的时间")
    private String testTime;

    @ApiModelProperty(value = "当前KE的版本，升级回滚测试不提供。只有在兼容、全量测试、按需测试时提供版本")
    private String KEVersion;

    @ApiModelProperty(value = "状态。- deployCode，部署quard测试代码；- deployPackage，分发KE测试包。- deployKE，部署KE包，配置KE配置文件（kerberos，config，etc）。- testing，执行quard测试用例，此时不会告知测试用例执行状态。  - finished，quard用例测试完成状态，并写入测试结果，（pass/fail/skip etc)")
    private String quardStage;

    @ApiModelProperty(value = "测试类型。包含UPGRADE、Compatibility、on-demand、FullTest（on-demand测试是指按需求的手动触发的测试）")
    private String testType;

    @ApiModelProperty(value = "从该版本号开始升级回滚")
    private String upgradeFrom;

    @ApiModelProperty(value = "升级回滚目的版本号")
    private String upgradeTo;

    @ApiModelProperty(value = "测试结果")
    private AllureResult allureResult;



}
