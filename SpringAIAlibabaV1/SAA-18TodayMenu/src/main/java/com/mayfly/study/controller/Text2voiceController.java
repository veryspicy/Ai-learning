package com.mayfly.study.controller;

import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisOptions;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisModel;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisPrompt;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResponse;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

@RestController
public class Text2voiceController {

    @Resource
    private SpeechSynthesisModel speechSynthesisModel;

    public static final String BAILIAN_VOICE_MODEL = "cosyvoice-v3-flash";
    public static final String BAILIAN_VOICE_TIMBER = "longyingcui";

    @GetMapping("/t2v/voice")
    public String t2vVoice(@RequestParam(name = "msg", defaultValue = "温馨提示，您的余额已不足10元，请尽快充值") String msg) throws FileNotFoundException {
            String filePath = "d:\\codeRepo\\SpringAIAlibabaV1\\" + UUID.randomUUID() + ".mp3";
            
            //语音参数设置
            DashScopeSpeechSynthesisOptions options = DashScopeSpeechSynthesisOptions.builder()
                .model(BAILIAN_VOICE_MODEL)
                .voice(BAILIAN_VOICE_TIMBER)
                .build();
            //调用大模型语音生成对象

        SpeechSynthesisResponse response = speechSynthesisModel.call(new SpeechSynthesisPrompt(msg, options));
        ByteBuffer byteBuffer = response.getResult().getOutput().getAudio();
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            fileOutputStream.write(byteBuffer.array());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return filePath;

    }
}
