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
    private List<Long> tagIds;
}
