package org.jeecg.modules.devops.chatbot;

import org.jeecg.modules.devops.chatbot.adapter.AzureAdapter;
import org.jeecg.modules.devops.chatbot.adapter.OpenaiAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ChatBotManager {

    private final ConcurrentMap<String, ChatBot> chatBots = new ConcurrentHashMap<>();

    @Autowired
    private ChatBotConfig config;

    public ChatBot get(final String key) {
        if (chatBots.get(key) == null) {
            return chatBots.computeIfAbsent(key, (ignore) -> new ChatBot(getAdapter()));
        }

        return chatBots.get(key);
    }

    private Adapter getAdapter() {
        switch (config.getProvider()) {
            case AZURE:
                return new AzureAdapter(config.getEndpoint(), config.getDeployment(), config.getToken(), config.getVersion());
            case OPENAI:
                return new OpenaiAdapter(config.getToken());
            default:
                throw new IllegalArgumentException();
        }
    }
}
