package com.mayfly.study.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Consumer;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
public class ChatMemory4RedisController {

    @Resource(name = "deepseekChatClient")
    private ChatClient qwenChatClient;

    @GetMapping("/chatmemory/chat")
    public String chat(String message, String userId) {

        /*  return qwenChatClient.prompt(message).advisors(new Consumer<ChatClient.AdvisorSpec>() {
            @Override
            public void accept(ChatClient.AdvisorSpec advisorSpec) {
                advisorSpec.param(CONVERSATION_ID, userId);
            }
        }).call().content();*/

            return qwenChatClient
                    .prompt(message)
                    .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, userId) )
                    .call()
                    .content();

    }

}
