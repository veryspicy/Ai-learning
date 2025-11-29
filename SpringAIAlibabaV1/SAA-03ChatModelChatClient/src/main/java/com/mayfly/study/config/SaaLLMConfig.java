package com.mayfly.study.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SaaLLMConfig {

    @Bean
    public DashScopeApi dashScopeApi() {
        return DashScopeApi.builder().apiKey(System.getenv("aliQwen-api")).build();
    }

    @Bean
    public ChatClient chatClient(@Qualifier("dashscopeChatModel") ChatModel dashScopeChatModel) {
        return ChatClient.builder(dashScopeChatModel).build();
    }
}
