package com.example.todolistmybatis.service;

import com.example.todolistmybatis.entity.Todo;

import java.util.List;
import java.util.Map;

public interface TodoService {
    List<Todo> findByUsername(String username);
    List<Todo> findByUsernameWithFilters(String username, Map<String, Object> filters);
    Todo getById(Long id);
    Todo create(Todo todo, List<Long> tagIds);
    Todo update(Long id, Todo todo, List<Long> tagIds);
    void delete(Long id);
    List<Todo> findByCategory(Long categoryId);
    List<Todo> findByTag(Long tagId);
    Map<String, Object> getStatistics(String username);
    List<Todo> findUpcomingReminders(String username);
}