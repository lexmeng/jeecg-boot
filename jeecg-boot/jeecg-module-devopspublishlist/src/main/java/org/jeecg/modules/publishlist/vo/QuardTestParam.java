package org.jeecg.modules.publishlist.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="QuardTestParam对象", description="Quard测试请求参数")
public class QuardTestParam {

    @ApiModelProperty(value = "Quard测试类型")
    private String quardType;

    @ApiModelProperty(value = "Quard测试平台")
    private String platform;

    @ApiModelProperty(value = "Quard代码仓库")
    private String quardRepo;

    @ApiModelProperty(value = "Quard测试分支")
    private String quardBranch;

    @ApiModelProperty(value = "reuseNum")
    private String reuseNum;

    @ApiModelProperty(value = "upgradeNum")
    private String upgradeNum;

    @ApiModelProperty(value = "测试用例集合")
    private String selectTests;

    @ApiModelProperty(value = "测试包链接")
    private String packageUrl;

}
