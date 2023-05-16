package org.jeecg.modules.devops.chatbot;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ChatBotManager {

    private final ConcurrentMap<String, ChatBot> chatBots = new ConcurrentHashMap<>();

    public ChatBot get(final String key) {
        if (chatBots.get(key) == null) {
            return chatBots.computeIfAbsent(key, (ignore) -> new ChatBot());
        }

        return chatBots.get(key);
    }
}
