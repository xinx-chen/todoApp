package com.example.todolistmybatis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.todolistmybatis.dto.UserRegistrationDto;
import com.example.todolistmybatis.entity.User;
import com.example.todolistmybatis.mapper.UserMapper;
import com.example.todolistmybatis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User registerNewUser(UserRegistrationDto registrationDto) {
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(hashPassword(registrationDto.getPassword()));  // 使用自定义哈希方法

        userMapper.insert(user);
        return user;
    }

    // 简单的密码哈希方法
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            // 简单处理异常，实际应用中应该有更好的错误处理
            throw new RuntimeException("密码哈希失败", e);
        }
    }

    @Override
    public User findByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean existsByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return userMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        return userMapper.selectCount(queryWrapper) > 0;
    }
    @Override
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        // 使用与哈希密码相同的方法来验证
        String hashedPassword = hashPassword(rawPassword);
        return hashedPassword.equals(encodedPassword);
    }

}
