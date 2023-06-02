package org.jeecg.modules.publishlist.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="Quard Allure结果数据", description="Quard Allure结果数据")
public class AllureResult {

    @ApiModelProperty(value = "失败用例数")
    private Integer failed;
    @ApiModelProperty(value = "阻塞用例数")
    private Integer broken;
    @ApiModelProperty(value = "忽略用例数")
    private Integer skipped;
    @ApiModelProperty(value = "通过用例数")
    private Integer passed;
    @ApiModelProperty(value = "未知的用例数")
    private Integer unknown;
    @ApiModelProperty(value = "总用例数")
    private Integer total;
}
