package org.jeecg.modules.publishlist.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
@ApiModel(value="Jenkins调用参数", description="Jenkins调用参数")
public class JenkinsJobReq {
    @ApiModelProperty(value = "类型-OFS or AWS")
    private String typeName;

    @ApiModelProperty(value = "文件夹名")
    private String folderName;

    @ApiModelProperty(value = "任务名")
    private String jobName;

    @ApiModelProperty(value = "任务参数")
    private Map<String,String> paramMap;
}
