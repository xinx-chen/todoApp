package com.example.todolistmybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("todo_tag")
public class TodoTag {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long todoId;
    private Long tagId;
}
