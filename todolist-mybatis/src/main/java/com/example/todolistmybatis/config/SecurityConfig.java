package com.example.todolistmybatis.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
public class SecurityConfig {
    // 空配置类，仅用于禁用 Spring Security
}
