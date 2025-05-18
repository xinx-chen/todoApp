package com.example.todolistmybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("todo")
public class Todo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String description;
    private Boolean done;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate targetDate;

    private String username;
    private Long categoryId;
    private Integer priority;
    private LocalDateTime reminderTime;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @TableField(exist = false)
    private Category category;

    @TableField(exist = false)
    private List<Tag> tags;
}