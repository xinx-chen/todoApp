package com.example.todolistmybatis.controller;

import com.example.todolistmybatis.dto.UserLoginDto;
import com.example.todolistmybatis.dto.UserRegistrationDto;
import com.example.todolistmybatis.entity.User;
import com.example.todolistmybatis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        // 检查用户名是否已存在
        if (userService.existsByUsername(registrationDto.getUsername())) {
            return ResponseEntity.badRequest().body("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (userService.existsByEmail(registrationDto.getEmail())) {
            return ResponseEntity.badRequest().body("邮箱已被注册");
        }

        // 创建新用户
        User user = userService.registerNewUser(registrationDto);
        return ResponseEntity.ok("用户注册成功");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto loginDto) {
        // 验证用户凭据
        User user = userService.findByUsername(loginDto.getUsername());

        if (user == null) {
            return ResponseEntity.badRequest().body("用户不存在");
        }

        // 验证密码
        if (!userService.validatePassword(loginDto.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("密码错误");
        }

        // 登录成功，返回用户信息（可以返回JWT令牌等）
        return ResponseEntity.ok("登录成功");
    }
}
