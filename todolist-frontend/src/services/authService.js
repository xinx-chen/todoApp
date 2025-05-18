// src/services/authService.js
import api from './api';

export default {
  async login(credentials) {
    try {
      const response = await api.post('/users/login', {
        username: credentials.username,
        password: credentials.password
      });
      
      // 根据您的后端返回格式，处理登录成功的情况
      if (response.data === "登录成功") {
        // 由于后端没有返回用户信息，我们需要构建一个用户对象
        const userObj = {
          username: credentials.username,
          isAuthenticated: true,
          loginTime: new Date().toISOString()
        };
        
        // 存储用户信息到本地存储
        localStorage.setItem('user', JSON.stringify(userObj));
        return userObj;
      } else {
        throw new Error(response.data || '登录失败');
      }
    } catch (error) {
      // 处理错误响应
      if (error.response && error.response.data) {
        throw new Error(error.response.data);
      } else {
        throw new Error('登录失败，请检查用户名和密码');
      }
    }
  },
  
  async register(userData) {
    try {
      const response = await api.post('/users/register', {
        username: userData.username,
        password: userData.password,
        email: userData.email
      });
      
      // 根据您的后端返回格式，处理注册成功的情况
      if (response.data === "用户注册成功") {
        return { success: true, message: '注册成功' };
      } else {
        throw new Error(response.data || '注册失败');
      }
    } catch (error) {
      // 处理错误响应
      if (error.response && error.response.data) {
        throw new Error(error.response.data);
      } else {
        throw new Error('注册失败，请稍后再试');
      }
    }
  },
  
  logout() {
    // 由于没有后端登出API，只需清除本地存储
    localStorage.removeItem('user');
  },
  
  // 检查用户是否已登录
  isAuthenticated() {
    const user = localStorage.getItem('user');
    return user !== null && user !== "登录成功";
  },
  
  // 获取当前登录用户信息
  getCurrentUser() {
    try {
      const userStr = localStorage.getItem('user');
      if (userStr && userStr !== "登录成功") {
        return JSON.parse(userStr);
      }
      return null;
    } catch (e) {
      localStorage.removeItem('user');
      return null;
    }
  }
};
