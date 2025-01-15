package com.example.rdb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {
    public ObjectMapper objectMapper() {
        var mapper = new ObjectMapper();
        return mapper;
    }
}
