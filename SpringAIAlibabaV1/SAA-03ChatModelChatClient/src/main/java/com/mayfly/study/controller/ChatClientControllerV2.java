package com.mayfly.study.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class ChatClientControllerV2 {

    @Resource
    private ChatClient dashScopeChatClientV2;
    @Resource
    private ChatModel chatModel;

    @GetMapping("/chatmodelv2/dochat")
    public String doChat2(@RequestParam(name= "msg",defaultValue =  "你是谁") String msg) {
        return chatModel.call(msg);
    }

    @GetMapping("/chatmodelv2/dochat")
    public String dochat3(@RequestParam(name = "msg", defaultValue = "你是谁") String msg) {
        return dashScopeChatClientV2.prompt().user(msg).call().content();


    }

}
