package org.jeecg.modules.devops.chatbot;

import cn.hutool.core.lang.Assert;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChatBot {

    private final Adapter adapter;

    private final ChatMessageHistory history;

    private ChatCompletionOptions options = ChatCompletionOptions.DEFAULT;

    public ChatBot(Adapter adapter) {
        this.adapter = adapter;
        this.history = new ChatMessageHistory();
    }

    public ChatBot withOptions(final ChatCompletionOptions options) {
        Assert.notNull(options);
        this.options = options;
        return this;
    }

    public ChatMessage call(final String content) {
        final ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), content);
        final ChatMessage completionMessage = adapter.createChatCompletion(chatCompletionRequest(userMessage)).getChoices().get(0).getMessage();
        history.add(userMessage, completionMessage);
        return completionMessage;

    }

    private ChatCompletionRequest chatCompletionRequest(final ChatMessage userMessage) {
        final ChatCompletionRequest.ChatCompletionRequestBuilder builder = ChatCompletionRequest.builder()
                .model(options.model)
                .temperature(options.temperature)
                .topP(options.topP)
                .n(options.n)
                .stream(options.stream)
                .stop(options.stop)
                .maxTokens(options.maxTokens)
                .presencePenalty(options.presencePenalty)
                .frequencyPenalty(options.frequencyPenalty)
                .logitBias(options.logitBias)
                .user(null);

        builder.messages(Stream.concat(history.getMessages().stream(), Stream.of(userMessage)).collect(Collectors.toList()));

        return builder.build();
    }

}
