package com.example.todolistmybatis.dto;

import lombok.Data;
import java.util.List;

@Data
public class TodoRequest {
    private String description;
    private Boolean done;
    private String targetDate;
    private String username; // 添加 username 字段
    private Long categoryId;
    private Integer priority;
    private String reminderTime;
    private String notes;
    private String imageUrl; // 添加 imageUrl 字段，用于存储图片链接
    private List<Long> tagIds;
}
