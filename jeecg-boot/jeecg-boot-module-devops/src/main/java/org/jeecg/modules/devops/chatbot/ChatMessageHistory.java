package org.jeecg.modules.devops.chatbot;

import com.lark.oapi.core.utils.Lists;
import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatMessageHistory {

    private static final int LIMITS = 16;

    private final List<ChatMessage> messages = new ArrayList<>();

    public void add(final ChatMessage... messages) {
        this.messages.addAll(Lists.newArrayList(messages));
        limitContext();
    }

    private void limitContext() {
        if (this.messages.size() > LIMITS) {
            final List<ChatMessage> excessMessages = messages.subList(0, messages.size() - LIMITS);
            excessMessages.clear();
        }
    }

    public List<ChatMessage> getMessages() {
        return Collections.unmodifiableList(this.messages);
    }
}
