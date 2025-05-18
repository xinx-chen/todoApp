package com.example.todolistmybatis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.todolistmybatis.entity.Tag;
import com.example.todolistmybatis.entity.TodoTag;
import com.example.todolistmybatis.mapper.TagMapper;
import com.example.todolistmybatis.mapper.TodoTagMapper;
import com.example.todolistmybatis.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    private final TodoTagMapper todoTagMapper;

    @Autowired
    public TagServiceImpl(TodoTagMapper todoTagMapper) {
        this.todoTagMapper = todoTagMapper;
    }

    @Override
    public List<Tag> findByUsername(String username) {
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Tag> findByTodoId(Long todoId) {
        // 通过 TodoTag 关联表查询与特定 Todo 关联的所有标签
        QueryWrapper<TodoTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("todo_id", todoId);
        List<TodoTag> todoTags = todoTagMapper.selectList(queryWrapper);

        // 提取标签 ID 列表
        List<Long> tagIds = todoTags.stream()  // 修复拼写错误：tooTags -> todoTags
                .map(TodoTag::getTagId)
                .collect(Collectors.toList());

        // 如果没有标签，返回空列表
        if (tagIds.isEmpty()) {
            return Collections.emptyList();  // 使用 Collections.emptyList() 替代 List.of()
        }

        // 查询这些标签
        return this.baseMapper.selectBatchIds(tagIds);
    }

    @Override
    @Transactional
    public void assignTagsToTodo(Long todoId, List<Long> tagIds) {
        // 先删除现有关联
        todoTagMapper.delete(new QueryWrapper<TodoTag>().eq("todo_id", todoId));

        if (tagIds != null && !tagIds.isEmpty()) {
            // 验证标签是否存在
            List<Long> existingTagIds = this.listByIds(tagIds)
                    .stream()
                    .map(Tag::getId)
                    .collect(Collectors.toList());

            for (Long tagId : existingTagIds) {
                TodoTag todoTag = new TodoTag();
                todoTag.setTodoId(todoId);
                todoTag.setTagId(tagId);
                todoTagMapper.insert(todoTag);
            }
        }
    }


}
