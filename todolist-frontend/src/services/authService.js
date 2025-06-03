// src/services/authService.js
import api from './api';

export default {
  async login(credentials) {
    try {
      const response = await api.post('/users/login', {
        username: credentials.username,
        password: credentials.password
      });
      // 根据后端返回格式，处理登录成功的情况
      if (response.data.code === 200) {
        const userObj = {
          token: response.data.data.token,
          username: credentials.username,
          isAuthenticated: true,
          loginTime: new Date().toISOString()
        };
        localStorage.setItem('user', JSON.stringify(userObj));
        return userObj;
      } else {
        throw new Error(response.data.message || '登录失败');
      }
    } catch (error) {
      // 统一错误处理
      const errorMsg = error.response?.data?.message ||
        error.message ||
        '登录失败，请检查用户名和密码';
      throw new Error(errorMsg);
    }
  },

  async register(userData) {
    const response = await api.post('/users/register', {
      username: userData.username,
      password: userData.password,
      email: userData.email
    });

    // 根据后端返回格式，处理注册的情况
    if (response.data.code === 200) {
      return { success: true, message: '注册成功' };
    } else if (response.data.code === 1001) {
      throw new Error('用户名已存在，请选择其他用户名');
    } else if (response.data.code === 1002) {
      throw new Error('邮箱已被注册，请使用其他邮箱');
    }
    else {
      throw new Error(response.data || '注册失败');
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
