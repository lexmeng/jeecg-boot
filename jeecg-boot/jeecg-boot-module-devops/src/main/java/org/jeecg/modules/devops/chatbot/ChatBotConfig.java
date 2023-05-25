package org.jeecg.modules.devops.chatbot;

import lombok.Data;
import org.jeecg.modules.devops.chatbot.adapter.AzureAdapter;
import org.jeecg.modules.devops.chatbot.adapter.OpenaiAdapter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
@Data
@Configuration
@ConfigurationProperties(prefix = "openai")
public class ChatBotConfig {

    public static enum Provider {

        OPENAI(OpenaiAdapter.class), AZURE(AzureAdapter.class);

        private final Class<? extends Adapter> adapterClass;

        Provider(Class<? extends Adapter> adapterClass) {
            this.adapterClass = adapterClass;
        }

        public Class<? extends Adapter> adapterClass() {
            return adapterClass;
        }
    }

    @NotBlank
    private String token;

    private String endpoint;

    private String deployment;

    private String version = "2023-03-15-preview";

    private Provider provider = Provider.OPENAI;
}
