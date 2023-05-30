package org.jeecg.modules.devops.blueocean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Validated
@Data
//@Configuration
//@ConfigurationProperties(prefix = "jenkins")
public class BlueOceanConfig implements Serializable {

    @NotBlank
    String url;

    @NotBlank
    String account;

    @NotBlank
    String password;

}
