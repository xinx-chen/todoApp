package com.example.todolistmybatis.service;

import com.example.todolistmybatis.dto.UserRegistrationDto;
import com.example.todolistmybatis.entity.User;

public interface UserService {
    User registerNewUser(UserRegistrationDto registrationDto);
    User findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean validatePassword(String rawPassword, String encodedPassword);
}
