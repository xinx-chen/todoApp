package com.example.todolistmybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.todolistmybatis.entity.TodoTag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TodoTagMapper extends BaseMapper<TodoTag> {
}
