package org.jeecg.modules.devops.controller;

import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.devops.controller.request.ChatRequest;
import org.jeecg.modules.devops.service.ChatBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/chatbot")
public class ChatBotController {

    @Autowired
    private ChatBotService chatBotService;

    @PostMapping("/call")
    public Result<ChatMessage> call(@RequestBody ChatRequest request) {
        return Result.ok(chatBotService.call(request.getContent(), request.getOptions()));
    }
}