package com.example.todolistmybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.todolistmybatis.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
