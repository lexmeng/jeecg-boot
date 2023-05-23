package org.jeecg.modules.devops.blueocean.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BlueOceanRun implements Serializable {

    private String id;
    private String result;
    private String state;
    private Double durationInMillis;
    private String startTime;
}
