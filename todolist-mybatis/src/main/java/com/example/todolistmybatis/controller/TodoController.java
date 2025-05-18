
package com.example.todolistmybatis.controller;

import com.example.todolistmybatis.dto.TodoRequest;
import com.example.todolistmybatis.entity.Todo;
import com.example.todolistmybatis.service.TodoService;
import org.slf4j.Logger;  // 修改为正确的 SLF4J 导入
import org.slf4j.LoggerFactory;  // 修改为正确的 SLF4J 导入
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:8081")
public class TodoController {

    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);  // 添加日志记录器

    @Autowired
    private TodoService todoService;


    // 获取指定用户的所有任务
    @GetMapping("/users/{username}")
    public ResponseEntity<List<Todo>> getAllTodosByUsername(
            @PathVariable String username,
            @RequestParam(required = false) Boolean done,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer priority,
            @RequestParam(required = false) Long tagId,
            @RequestParam(required = false) String search) {

        Map<String, Object> filters = new HashMap<>();
        if (done != null) filters.put("done", done);
        if (categoryId != null) filters.put("categoryId", categoryId);
        if (priority != null) filters.put("priority", priority);
        if (tagId != null) filters.put("tagId", tagId);
        if (search != null && !search.trim().isEmpty()) filters.put("search", search);

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
    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody TodoRequest todoRequest) {
        Todo todo = new Todo();
        todo.setDescription(todoRequest.getDescription());
        todo.setDone(todoRequest.getDone());
        todo.setTargetDate(LocalDate.parse(todoRequest.getTargetDate()));
        todo.setUsername(todoRequest.getUsername()); // 使用正确的 getter
        todo.setCategoryId(todoRequest.getCategoryId());
        todo.setPriority(todoRequest.getPriority());

        if (todoRequest.getReminderTime() != null && !todoRequest.getReminderTime().isEmpty()) {
            todo.setReminderTime(LocalDateTime.parse(todoRequest.getReminderTime(), DateTimeFormatter.ISO_DATE_TIME));
        }

        todo.setNotes(todoRequest.getNotes());
        return new ResponseEntity<>(todoService.create(todo, todoRequest.getTagIds()), HttpStatus.CREATED);
    }

    // 更新任务
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody TodoRequest todoRequest) {
        Todo existingTodo = todoService.getById(id);
        if (existingTodo == null) {
            return ResponseEntity.notFound().build();
        }

        Todo todo = new Todo();
        todo.setId(id);
        todo.setDescription(todoRequest.getDescription());
        todo.setDone(todoRequest.getDone());
        todo.setTargetDate(LocalDate.parse(todoRequest.getTargetDate()));
        todo.setUsername(todoRequest.getUsername()); // 使用正确的 getter
        todo.setCategoryId(todoRequest.getCategoryId());
        todo.setPriority(todoRequest.getPriority());

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
