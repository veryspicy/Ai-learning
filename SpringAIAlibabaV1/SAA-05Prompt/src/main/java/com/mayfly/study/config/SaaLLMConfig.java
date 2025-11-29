package com.mayfly.study.config;



import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SaaLLMConfig {

    private final String DEEPSEEK_MODEL = "deepseek-v3.2-exp";
    private final String QWEN_MODEL = "qwen3-max";
    private final String API_KEY = System.getenv("aliQwen-api");


    public DashScopeApi getDashScopeApi() {
        return  DashScopeApi.builder().apiKey(API_KEY).build();
    }

//    public OpenAiApi getOpenAiApi() {return  OpenAiApi.builder().apiKey(API_KEY).build();}

    @Bean(name= "deepseek")
    public ChatModel deepseek(){
        return DashScopeChatModel.builder()
                .dashScopeApi(getDashScopeApi())
                .defaultOptions(DashScopeChatOptions.builder().withModel(DEEPSEEK_MODEL).build())
                .build();
    }
    @Bean(name= "qwen")
    public ChatModel qwen(){
        return DashScopeChatModel.builder()
                .dashScopeApi(getDashScopeApi())
                .defaultOptions(DashScopeChatOptions.builder().withModel(QWEN_MODEL).build())
                .build();
    }

//    @Bean(name= "deepseek")
//    public ChatModel deepseek(){
//        return OpenAiChatModel.builder()
//                .openAiApi(getOpenAiApi())
//                .defaultOptions(OpenAiChatOptions.builder().model(DEEPSEEK_MODEL).build())
//                .build();
//    }
//    @Bean(name= "qwen")
//    public ChatModel qwen(){
//        return OpenAiChatModel.builder()
//                .openAiApi(getOpenAiApi())
//                .defaultOptions(OpenAiChatOptions.builder().model(QWEN_MODEL).build())
//                .build();
//    }

    @Bean(name="deepseekChatClient")
    public ChatClient deepseekChatClient(@Qualifier("deepseek") ChatModel deepseekChatModel){
        return ChatClient.builder(deepseekChatModel)
                .defaultOptions(ChatOptions.builder().model(DEEPSEEK_MODEL).build())
                .build();
    }
    @Bean(name="qwenChatClient")
    public ChatClient qwenChatClient(@Qualifier("qwen") ChatModel qwenChatModel){
        return ChatClient.builder(qwenChatModel)
                .defaultOptions(ChatOptions.builder().model(QWEN_MODEL).build())
                .build();
    }
}
