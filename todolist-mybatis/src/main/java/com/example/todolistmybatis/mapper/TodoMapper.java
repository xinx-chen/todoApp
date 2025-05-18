package com.example.todolistmybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.todolistmybatis.entity.Todo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TodoMapper extends BaseMapper<Todo> {
}