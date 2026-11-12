package com.example.malldemo.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // 关闭浮点数自动转整数（核心这一行）
        mapper.disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT);
        return mapper;
    }
}
