
package com.example.todolistmybatis.controller;

import com.example.todolistmybatis.common.Result;
import com.example.todolistmybatis.dto.TodoRequest;
import com.example.todolistmybatis.entity.Todo;
import com.example.todolistmybatis.service.TodoService;
import com.example.todolistmybatis.util.ImageUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;  // 修改为正确的 SLF4J 导入
import org.slf4j.LoggerFactory;  // 修改为正确的 SLF4J 导入
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:8081")
public class TodoController {

    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);  // 添加日志记录器

    @Autowired
    private TodoService todoService;
    @Autowired
    private ImageUploadUtil imageUploadUtil;  // 添加 ImageUploadUtil 依赖


    // 获取指定用户的所有任务
    @GetMapping("/users/{username}")
    public ResponseEntity<List<Todo>> getAllTodosByUsername(
            @PathVariable String username,
            @RequestParam(required = false) Boolean done,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer priority,
            @RequestParam(required = false) Long tagId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean showTodayOnly) {

        Map<String, Object> filters = new HashMap<>();
        if (done != null) filters.put("done", done);
        if (categoryId != null) filters.put("categoryId", categoryId);
        if (priority != null) filters.put("priority", priority);
        if (tagId != null) filters.put("tagId", tagId);
        if (search != null && !search.trim().isEmpty()) filters.put("search", search);
        if(showTodayOnly !=null ) {
            filters.put("showTodayOnly",showTodayOnly);
        }

        return ResponseEntity.ok(todoService.findByUsernameWithFilters(username, filters));
    }

    // 获取统计信息
    @GetMapping("/users/{username}/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics(@PathVariable String username) {
        return ResponseEntity.ok(todoService.getStatistics(username));
    }

    // 获取即将到期的提醒
    @GetMapping("/users/{username}/reminders")
    public ResponseEntity<List<Todo>> getUpcomingReminders(@PathVariable String username) {
        return ResponseEntity.ok(todoService.findUpcomingReminders(username));
    }

    // 根据ID获取任务
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Todo todo = todoService.getById(id);
        return todo != null ? ResponseEntity.ok(todo) : ResponseEntity.notFound().build();
    }

    // 创建新任务
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createTodo(
            @RequestPart("todoRequest") TodoRequest todoRequest,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        String url = null;

        // 处理文件上传（如果有图片）
        if (image != null && !image.isEmpty()) {
            ImageUploadUtil imageUploadUtil = new ImageUploadUtil();
            Result<String> uploadResult = imageUploadUtil.uploadImage(image);

            // 如果图片上传失败，直接返回错误信息
            if (uploadResult.getCode() != 200) {
                log.error("图片上传失败: {}", uploadResult.getMessage());
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                        .body(Collections.singletonMap("error", uploadResult.getMessage()));
            }

            // 上传成功，设置图片URL到请求对象
            url = uploadResult.getData();
            log.info("图片上传成功: {}", uploadResult.getData());
        }

        // 其他处理逻辑...
        try {
            Todo todo = new Todo();
            todo.setDescription(todoRequest.getDescription());
            todo.setDone(todoRequest.getDone());
            todo.setTargetDate(LocalDate.parse(todoRequest.getTargetDate()));
            todo.setUsername(todoRequest.getUsername());
            todo.setCategoryId(todoRequest.getCategoryId());
            todo.setPriority(todoRequest.getPriority());
            todo.setImageUrl(url);

            if (todoRequest.getReminderTime() != null && !todoRequest.getReminderTime().isEmpty()) {
                todo.setReminderTime(LocalDateTime.parse(todoRequest.getReminderTime(), DateTimeFormatter.ISO_DATE_TIME));
            }

            todo.setNotes(todoRequest.getNotes());

            Todo createdTodo = todoService.create(todo, todoRequest.getTagIds());
            return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);

        } catch (Exception e) {
            log.error("创建待办事项失败: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "创建待办事项失败: " + e.getMessage()));
        }
    }

    // 更新任务
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateTodo(
            @PathVariable Long id,
            @RequestPart("todoRequest") TodoRequest todoRequest,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        //检查是否存在待办事项
        Todo existingTodo = todoService.getById(id);
        if (existingTodo == null) {
            return ResponseEntity.notFound().build();
        }
        log.info("request: {}", todoRequest);

        String url = null;

        // 处理文件上传（如果有图片）
        if (image != null && !image.isEmpty()) {

            Result<String> uploadResult = imageUploadUtil.uploadImage(image);

            // 如果图片上传失败，直接返回错误信息
            if (uploadResult.getCode() != 200) {
                log.error("图片上传失败: {}", uploadResult.getMessage());
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                        .body(Collections.singletonMap("error", uploadResult.getMessage()));
            }

            // 上传成功，设置图片URL到请求对象
            url = uploadResult.getData();
            log.info("图片上传成功: {}", uploadResult.getData());
        }

        Todo todo = new Todo();
        todo.setId(id);
        todo.setDescription(todoRequest.getDescription());
        todo.setDone(todoRequest.getDone());
        todo.setTargetDate(LocalDate.parse(todoRequest.getTargetDate()));
        todo.setUsername(todoRequest.getUsername()); // 使用正确的 getter
        todo.setCategoryId(todoRequest.getCategoryId());
        todo.setPriority(todoRequest.getPriority());
        if(url != null){
            // 如果有图片URL，则设置
            todo.setImageUrl(url);
        }
        else {
            // 如果没有图片URL，则保留原有的图片URL
            todo.setImageUrl(todoRequest.getImageUrl());
        }
        log.info("todo.geturl(): {}", todo.getImageUrl());

        if (todoRequest.getReminderTime() != null && !todoRequest.getReminderTime().isEmpty()) {
            todo.setReminderTime(LocalDateTime.parse(todoRequest.getReminderTime(), DateTimeFormatter.ISO_DATE_TIME));
        }

        todo.setNotes(todoRequest.getNotes());
        return ResponseEntity.ok(todoService.update(id, todo, todoRequest.getTagIds()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.delete(id); // 使用 TodoService 接口中的 delete 方法
        return ResponseEntity.noContent().build();
    }



}
