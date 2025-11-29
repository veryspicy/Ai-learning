package com.mayfly.study.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatModelController {

    @Resource
    @Qualifier("ollamaChatModel")
    private ChatModel dashScopeChatModel;



    @GetMapping("/chatmodel/dochat")
    public String doChat(@RequestParam(value = "msg", defaultValue = "你是谁") String msg) {
        String result = dashScopeChatModel.call(msg);
        System.out.println("响应："+ result);
        return result;

    }
}
