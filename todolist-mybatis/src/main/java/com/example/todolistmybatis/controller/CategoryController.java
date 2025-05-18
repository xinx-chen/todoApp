package com.example.todolistmybatis.controller;

import com.example.todolistmybatis.entity.Category;
import com.example.todolistmybatis.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/users/{username}")
    public ResponseEntity<List<Category>> getAllCategoriesByUsername(@PathVariable String username) {
        List<Category> categories = categoryService.findByUsername(username);
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);
        return ResponseEntity.ok(createdCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id); // 确保ID正确设置
        Category updatedCategory = categoryService.updateCategory(category);
        return ResponseEntity.ok(updatedCategory);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        boolean result = categoryService.removeById(id);
        if (result) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/batch")
    public ResponseEntity<?> batchDeleteCategories(@RequestBody List<Long> ids) {
        boolean result = categoryService.removeByIds(ids);
        return ResponseEntity.ok().build();
    }

}
