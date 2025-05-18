package com.example.todolistmybatis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.todolistmybatis.entity.Category;
import java.util.List;

public interface CategoryService extends IService<Category> {
    List<Category> findByUsername(String username);
    // 添加缺少的方法
    Category createCategory(Category category);

    Category updateCategory(Category category);
    // 可以添加自定义删除方法
    boolean deleteCategory(Long id);
}
