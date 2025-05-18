package com.example.todolistmybatis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.todolistmybatis.entity.Category;
import com.example.todolistmybatis.mapper.CategoryMapper;
import com.example.todolistmybatis.service.CategoryService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<Category> findByUsername(String username) {
        // 使用 QueryWrapper 替代自定义 SQL 方法
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return list(queryWrapper);
    }

    @Override
    public Category createCategory(Category category) {
        baseMapper.insert(category);
        return category;
    }

    @Override
    public Category updateCategory(Category category) {
        // 确保更新成功
        boolean updated = updateById(category);
        if (!updated) {
            throw new RuntimeException("更新分类失败，ID: " + category.getId());
        }
        // 重新查询获取最新数据
        return getById(category.getId());
    }
    @Override
    public boolean deleteCategory(Long id) {
        // 可以在这里添加自定义逻辑，如检查关联数据等
        return removeById(id);
    }
}
