package com.example.todolistmybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.todolistmybatis.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    @Select("SELECT t.* FROM tag t JOIN todo_tag tt ON t.id = tt.tag_id WHERE tt.todo_id = #{todoId}")
    List<Tag> findTagsByTodoId(@Param("todoId") Long todoId);
}