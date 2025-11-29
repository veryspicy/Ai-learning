package com.mayfly.study.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
public class PromptController {

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


    @GetMapping(value = "/stream/chatFlux5")
    public Flux<String> streamChatFlux5(@RequestParam(name = "question",defaultValue = "你是谁") String question){
        return deepseekChatClient.prompt()
                        .system("你是一个法律助手，只回答法律问题，其他问题回复：我只能回答法律相关问题，其他无可奉告")
                        .user(question).stream().content();
    }

    @GetMapping(value = "/prompt/chat3")
    public Flux<String> chat3(String question){
        SystemMessage systemMessage = new SystemMessage("你是一个讲故事的助手，" + "每个故事控制在600字以内且以HTML格式返回");

        UserMessage userMessage = new UserMessage(question);
        Prompt prompt = new Prompt(systemMessage, userMessage);
        return deepseekChatModel.stream(prompt)
                .mapNotNull(response -> response.getResults().getFirst().getOutput().getText());
    }
    @GetMapping(value = "/prompt/chat4")
    public String chat4(String question){
        AssistantMessage assistantMessage = qwenChatClient.prompt()
                .user(question)
                .call()
                .chatResponse()
                .getResult()
                .getOutput();

        return assistantMessage.getText();
    }

    @GetMapping(value = "/prompt/chat5")
    public String chat5(String city){
        String anwser = deepseekChatClient.prompt()
                .user(city + "未来3天天气预报情况如何？")
                .call()
                .chatResponse()
                .getResult()
                .getOutput()
                .getText();

        ToolResponseMessage toolResponseMessage = new ToolResponseMessage(
                List.of(new ToolResponseMessage.ToolResponse("1", "获得天气", city)
                )
        );

        String toolResponse = toolResponseMessage.getText();
        return anwser + " " + toolResponse;
    }

}
