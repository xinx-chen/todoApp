package com.example.todolistmybatis.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Category {
    private Long id;
    private String name;
    private String color;
    private String username;  // 使用 username 而不是 userId
    private LocalDateTime createdAt;
}
