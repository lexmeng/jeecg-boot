package org.jeecg.modules.devops.blueocean.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BlueOceanJob implements Serializable {

    private String name;
    private Boolean disabled;
    private String displayName;
    private String fullDisplayName;
    private String fullName;
}
