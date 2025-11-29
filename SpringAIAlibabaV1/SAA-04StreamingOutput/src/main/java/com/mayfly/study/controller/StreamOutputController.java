package com.mayfly.study.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class StreamOutputController {

    @Resource(name = "deepseek")
    private ChatModel deepseekChatModel;

    @Resource(name = "qwen")
    private ChatModel qwenChatModel;

    @GetMapping(value = "/stream/chatFlux1")
    public Flux<String> streamChatFlux1(@RequestParam(name ="question", defaultValue = "你是谁") String question) {
        return deepseekChatModel.stream(question);
    }

    @GetMapping(value = "/stream/chatFlux2")
    public Flux<String> streamChatflux2(@RequestParam(name = "question",defaultValue = "你是谁") String question) {
        return qwenChatModel.stream(question);
    }

    @Resource(name= "deepseekChatClient")
    private ChatClient deepseekChatClient;
    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;

    @GetMapping(value = "/stream/chatFlux3")
    public Flux<String> streamChatFlux3(@RequestParam(name = "question",defaultValue = "你是谁") String question){
        return deepseekChatClient.prompt().user(question).stream().content();
    }

    @GetMapping(value = "/stream/chatFlux4")
    public Flux<String> streamChatFlux4(@RequestParam(name = "question",defaultValue = "你是谁") String question){
        return qwenChatClient.prompt().user(question).stream().content();
    }
}
