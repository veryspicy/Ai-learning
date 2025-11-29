package com.mayfly.study.controller;

import com.mayfly.study.utils.DateTimeTools;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
public class ToolCallingController {
    @Resource(name = "qwen")
    private ChatModel qwenChatModel;

    @GetMapping("/toolcall/chat")
    public String Chat(@RequestParam(name = "msg", defaultValue = "你是谁现在几点") String msg) {
        //1. 工具注册到工具集合里
        ToolCallback[] tools = ToolCallbacks.from(new DateTimeTools());
        //2. 将工具集配置到ChatOptions对象
        ChatOptions options = ToolCallingChatOptions.builder().toolCallbacks(tools).build();
        //3. 构建提示词
        Prompt prompt = new Prompt(msg, options);
        //4. 调用大模型
        return qwenChatModel.call(prompt).getResult().getOutput().getText();
    }

    @Resource()
    private ChatClient chatClient; // 使用向量数据库的记忆的无法调用tool，暂时不知道原因

    @GetMapping("/toolcall/chat2")
    public Flux<String> Chat2(@RequestParam(name = "msg", defaultValue = "你是谁现在几点") String msg) {
        return chatClient.prompt(msg)
                .tools(new DateTimeTools())
                .stream()
                .content();
    }
    
}
