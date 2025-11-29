package com.mayfly.study.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class OllamaController {

    @Resource(name = "ollamaChatModel")
    private ChatModel chatModel;

    /**
     * 通用调用
     * @param msg
     * @return
     */
    @GetMapping(value = "/ollama/chat")
    public String chat(@RequestParam(name = "msg", defaultValue = "你是谁") String msg) {
        String result = chatModel.call(msg);
        System.out.println("---结果是："+result);
        return result;
    }

    /**
     * 流式返回调用
     * @param msg
     * @return
     */
    @GetMapping(value = "/ollama/streamchat")
    public Flux<String> stream(@RequestParam(name = "msg", defaultValue = "你是谁") String msg) {
        return chatModel.stream(msg);
    }

}
