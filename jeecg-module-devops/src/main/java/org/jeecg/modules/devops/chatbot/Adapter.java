package org.jeecg.modules.devops.chatbot;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;

public interface Adapter {

    ChatCompletionResult createChatCompletion(ChatCompletionRequest request);
}
