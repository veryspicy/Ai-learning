package com.mayfly.study.config;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class InitVectorDabaseConfig {

    @Autowired
    private VectorStore vectorStore;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Value("classpath:ops.txt")
    private Resource opsFile;


    @PostConstruct
    public void init() throws UnsupportedEncodingException {
        //1. 读取文件
        TextReader textReader = new TextReader(opsFile);
        textReader.setCharset(Charset.defaultCharset());

        //2. 文件内容转换为向量（开启分词）
        List<Document> list = new TokenTextSplitter().transform(textReader.read());

        //3. 写入向量数据库RedisStack
        // vectorStore.add(list);

        //解决向量数据库数据重复问题，使用redis setnx命令处理
        String sourceMetadata = (String) textReader.getCustomMetadata().get("source");

        String textHash = DigestUtils.md5DigestAsHex(sourceMetadata.getBytes(StandardCharsets.UTF_8));
        String redisKey = "vector-xxx:" + textHash;
        // 判断是否存入过, rediskey如果可以插入表示以前没有过，可以加入向量数据
        Boolean retFlg = redisTemplate.opsForValue().setIfAbsent(redisKey, "1");

        System.out.println("****retFlag :" + retFlg);
        if (Boolean.TRUE.equals(retFlg)) {
            // 键不存，首次插入，可以保存进向量数据库
            vectorStore.add(list);
        }else{
            System.out.println("----向量初始化数据已加载，请不要重复");
        }

    }
}
