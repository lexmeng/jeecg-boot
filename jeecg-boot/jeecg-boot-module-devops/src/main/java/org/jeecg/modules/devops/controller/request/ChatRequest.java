package org.jeecg.modules.devops.controller.request;

import lombok.Data;
import lombok.ToString;
import org.jeecg.modules.devops.chatbot.ChatCompletionOptions;

import java.io.Serializable;

@Data
@ToString
public class ChatRequest implements Serializable {
    private String content;
    private ChatCompletionOptions options = ChatCompletionOptions.DEFAULT;
}
