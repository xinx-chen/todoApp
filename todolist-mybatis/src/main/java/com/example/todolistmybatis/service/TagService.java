package com.example.todolistmybatis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.todolistmybatis.entity.Tag;
import java.util.List;

public interface TagService extends IService<Tag> {
    List<Tag> findByUsername(String username);
    // 添加以下缺少的方法
    List<Tag> findByTodoId(Long todoId);
    void assignTagsToTodo(Long todoId, List<Long> tagIds);
}