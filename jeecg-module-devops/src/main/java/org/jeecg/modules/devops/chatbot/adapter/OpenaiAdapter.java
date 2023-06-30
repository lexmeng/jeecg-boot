package org.jeecg.modules.devops.chatbot.adapter;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.service.OpenAiService;
import org.jeecg.modules.devops.chatbot.Adapter;

import java.time.Duration;

public class OpenaiAdapter implements Adapter {

    private final OpenAiService service;

    public OpenaiAdapter(String token) {
        this.service = new OpenAiService(token, Duration.ZERO);
    }

    @Override
    public ChatCompletionResult createChatCompletion(ChatCompletionRequest request) {
        return service.createChatCompletion(request);
    }
}
