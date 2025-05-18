package com.example.todolistmybatis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.todolistmybatis.entity.Todo;
import com.example.todolistmybatis.entity.Tag;
import com.example.todolistmybatis.mapper.TodoMapper;
import com.example.todolistmybatis.service.CategoryService;
import com.example.todolistmybatis.service.TagService;
import com.example.todolistmybatis.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl extends ServiceImpl<TodoMapper, Todo> implements TodoService {

    private final CategoryService categoryService;
    private final TagService tagService;

    @Autowired
    public TodoServiceImpl(CategoryService categoryService, TagService tagService) {
        this.categoryService = categoryService;
        this.tagService = tagService;
    }

    @Override
    public List<Todo> findByUsername(String username) {
        QueryWrapper<Todo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<Todo> todos = this.baseMapper.selectList(queryWrapper);

        // 加载分类和标签
        todos.forEach(this::loadCategoryAndTags);

        return todos;
    }

    @Override
    public List<Todo> findByUsernameWithFilters(String username, Map<String, Object> filters) {
        QueryWrapper<Todo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);

        // 应用过滤条件
        if (filters.containsKey("categoryId")) {
            queryWrapper.eq("category_id", filters.get("categoryId"));
        }

        if (filters.containsKey("done")) {
            queryWrapper.eq("done", filters.get("done"));
        }

        if (filters.containsKey("priority")) {
            queryWrapper.eq("priority", filters.get("priority"));
        }

        if (filters.containsKey("search")) {
            queryWrapper.like("description", filters.get("search"));
        }

        List<Todo> todos = this.baseMapper.selectList(queryWrapper);

        // 如果有标签过滤，需要在内存中过滤
        if (filters.containsKey("tagId")) {
            Long tagId = Long.valueOf(filters.get("tagId").toString());
            todos = todos.stream()
                    .filter(todo -> {
                        List<Tag> tags = tagService.findByTodoId(todo.getId());
                        return tags.stream().anyMatch(tag -> tag.getId().equals(tagId));
                    })
                    .collect(Collectors.toList());
        }

        // 加载分类和标签
        todos.forEach(this::loadCategoryAndTags);

        return todos;
    }

    @Override
    public Todo getById(Long id) {
        Todo todo = this.baseMapper.selectById(id);
        if (todo != null) {
            loadCategoryAndTags(todo);
        }
        return todo;
    }

    @Override
    @Transactional
    public Todo create(Todo todo, List<Long> tagIds) {
        this.baseMapper.insert(todo);

        if (tagIds != null && !tagIds.isEmpty()) {
            tagService.assignTagsToTodo(todo.getId(), tagIds);
        }

        return getById(todo.getId());
    }

    @Override
    @Transactional
    public Todo update(Long id, Todo todo, List<Long> tagIds) {
        todo.setId(id);
        this.baseMapper.updateById(todo);

        if (tagIds != null) {
            tagService.assignTagsToTodo(id, tagIds);
        }

        return getById(id);
    }

    @Override
    public void delete(Long id) {
        this.baseMapper.deleteById(id);
    }

    @Override
    public List<Todo> findByCategory(Long categoryId) {
        QueryWrapper<Todo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        List<Todo> todos = this.baseMapper.selectList(queryWrapper);
        todos.forEach(this::loadCategoryAndTags);
        return todos;
    }

    @Override
    public List<Todo> findByTag(Long tagId) {
        List<Todo> todos = this.baseMapper.selectList(null);
        List<Todo> filteredTodos = todos.stream()
                .filter(todo -> {
                    List<Tag> tags = tagService.findByTodoId(todo.getId());
                    return tags.stream().anyMatch(tag -> tag.getId().equals(tagId));
                })
                .collect(Collectors.toList());

        filteredTodos.forEach(this::loadCategoryAndTags);
        return filteredTodos;
    }

    @Override
    public Map<String, Object> getStatistics(String username) {
        List<Todo> todos = findByUsername(username);

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", todos.size());
        statistics.put("completed", todos.stream().filter(Todo::getDone).count());
        statistics.put("pending", todos.stream().filter(todo -> !todo.getDone()).count());

        // 按优先级统计
        Map<Integer, Long> byPriority = todos.stream()
                .collect(Collectors.groupingBy(Todo::getPriority, Collectors.counting()));
        statistics.put("byPriority", byPriority);

        // 按分类统计
        Map<String, Long> byCategory = todos.stream()
                .filter(todo -> todo.getCategory() != null)
                .collect(Collectors.groupingBy(todo -> todo.getCategory().getName(), Collectors.counting()));
        statistics.put("byCategory", byCategory);

        return statistics;
    }

    @Override
    public List<Todo> findUpcomingReminders(String username) {
        QueryWrapper<Todo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username)
                .eq("done", false)
                .isNotNull("reminder_time")
                .ge("reminder_time", LocalDateTime.now())
                .le("reminder_time", LocalDateTime.now().plusDays(1))
                .orderByAsc("reminder_time");

        List<Todo> todos = this.baseMapper.selectList(queryWrapper);
        todos.forEach(this::loadCategoryAndTags);
        return todos;
    }

    private void loadCategoryAndTags(Todo todo) {
        // 加载分类
        if (todo.getCategoryId() != null) {
            todo.setCategory(categoryService.getById(todo.getCategoryId()));
        }

        // 加载标签
        todo.setTags(tagService.findByTodoId(todo.getId()));
    }
}
