package org.jeecg.modules.devops.service;

import com.theokanning.openai.completion.chat.ChatMessage;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.devops.chatbot.ChatBotManager;
import org.jeecg.modules.devops.chatbot.ChatCompletionOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatBotService {

    @Autowired
    private ChatBotManager manager;

    public ChatMessage call(String content, ChatCompletionOptions options) {
        final LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        return manager.get(loginUser.getUsername()).withOptions(options).call(content);
    }

}
