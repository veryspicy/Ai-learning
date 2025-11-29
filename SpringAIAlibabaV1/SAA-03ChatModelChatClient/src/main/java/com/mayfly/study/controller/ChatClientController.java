package com.mayfly.study.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatClientController {

    private final ChatClient dashScopeChatClient;

    /**
     * ChatClient不支持自动注入，依赖ChatModel对象接口，ChatClient.builder(dashScopeChatModel).build()
     * @param dashScopeChatModel
     */
    public ChatClientController(@Qualifier("dashscopeChatModel") ChatModel dashScopeChatModel) {
        this.dashScopeChatClient = ChatClient.builder(dashScopeChatModel).build();
    }

    @GetMapping("/chatclient/dochat")
    public String doChat(@RequestParam(value = "msg", defaultValue = "2+9=？") String msg) {
        return dashScopeChatClient.prompt().user(msg).call().content();
    }
}
