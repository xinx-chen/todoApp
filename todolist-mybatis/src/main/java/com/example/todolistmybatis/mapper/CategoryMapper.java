package com.example.todolistmybatis.mapper;

import com.example.todolistmybatis.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    // 自定义方法
    List<Category> findByUsername(String username);
}

