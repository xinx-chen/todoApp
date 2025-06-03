package com.example.todolistmybatis.controller;

import com.example.todolistmybatis.common.ErrorCode;
import com.example.todolistmybatis.common.Result;
import com.example.todolistmybatis.dto.LoginResponse;
import com.example.todolistmybatis.dto.UserLoginDto;
import com.example.todolistmybatis.dto.UserRegistrationDto;
import com.example.todolistmybatis.entity.User;
import com.example.todolistmybatis.service.UserService;
import com.example.todolistmybatis.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenUtil tokenUtil;

    @PostMapping("/register")
    public Result registerUser(@RequestBody UserRegistrationDto registrationDto) {
        // 检查用户名是否已存在
        if (userService.existsByUsername(registrationDto.getUsername())) {
            return Result.fail(ErrorCode.USER_EXIST.getCode(),ErrorCode.USER_EXIST.getMessage());
        }

        // 检查邮箱是否已存在
        if (userService.existsByEmail(registrationDto.getEmail())) {
            return Result.fail(ErrorCode.EMAIL_EXIST.getCode(), ErrorCode.EMAIL_EXIST.getMessage());
        }

        // 创建新用户
        User user = userService.registerNewUser(registrationDto);
        return Result.success("用户注册成功");
    }

    @PostMapping("/login")
    public Result loginUser(@RequestBody UserLoginDto loginDto) {
        // 验证用户凭据
        User user = userService.findByUsername(loginDto.getUsername());

        // 检查用户是否存在、密码是否正确
        if (user == null || !userService.validatePassword(loginDto.getPassword(), user.getPassword())) {
            return Result.fail(ErrorCode.USER_NAME_PWD_WRONG.getCode(),ErrorCode.USER_NAME_PWD_WRONG.getMessage());
        }

        String token = tokenUtil.getToken(user.getUsername());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);

        // 登录成功，返回用户信息（可以返回JWT令牌等）
        return Result.success(loginResponse);
    }
}
