package com.mayfly.study.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/prompttemplate/chat")
    public Flux<String> chat6(String topic, String output_format, String wordCount){
        PromptTemplate promptTemplate = new PromptTemplate(""+
                "讲一个关于{topic}的故事" +
                "并以{output_format}格式输出, " +
                "字数在{wordCount}左右"
                );

        Prompt prompt = promptTemplate.create(
                Map.of(
                        "topic", topic,
                        "output_format", output_format,
                        "word_count", wordCount
                )
        );
        return deepseekChatClient.prompt(prompt).stream().content();
    }

    @Value("classpath:/prompttemplate/mayfly-template.txt")
    private org.springframework.core.io.Resource userTemplate;

    @GetMapping("/prompttemplate/chat2")
    public String chat2(String topic, String output_format){
        PromptTemplate promptTemplate = new PromptTemplate(userTemplate);
        Prompt prompt = promptTemplate.create(Map.of("topic", topic, "output_format", output_format));
        return deepseekChatClient.prompt(prompt).call().content();
    }

    @GetMapping("/prompttemplate/chat3")
    public String chat3(String systemTopic, String userTopic){
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate("你是{systemTopic}助手， 只回答{systemTopic}其他无可奉告，以HTML格式返回结果。");
        Message sysMessage = systemPromptTemplate.createMessage(Map.of("systemTopic", systemTopic));

        PromptTemplate userPromptTemplate = new PromptTemplate("解释一下{userTopic}");
        Message userMessage = userPromptTemplate.createMessage(Map.of("userTopic", userTopic));

        Prompt prompt = new Prompt(List.of(sysMessage, userMessage));
        return deepseekChatClient.prompt(prompt).call().content();
    }

    @GetMapping("/prompttemplate/chat4")
    public String promptchat4(String question){
        SystemMessage systemMessage = new SystemMessage("你是一个java编程助手，拒绝回答非技术问题。");
        UserMessage userMessage = new UserMessage(question);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        String result = deepseekChatModel.call(prompt).getResult().getOutput().getText();
        System.out.println(result);
        return result;
    }

    @GetMapping("/prompttemplate/chat5")
    public Flux<String> promptchat5(String question){
        return deepseekChatClient.prompt("你是一个java编程助手，拒绝回答非技术问题。").user(question).stream().content();
    }

}
