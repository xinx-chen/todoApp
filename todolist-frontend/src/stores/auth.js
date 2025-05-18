// src/stores/auth.js
import { defineStore } from 'pinia'
import { ref } from 'vue'
import authService from '../services/authService'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(authService.getCurrentUser())
  const loading = ref(false)
  const error = ref(null)

  // 检查本地存储中的认证状态
  function checkAuth() {
    user.value = authService.getCurrentUser()
  }

  // 登录
  async function login(credentials) {
    loading.value = true
    error.value = null
    try {
      const userData = await authService.login(credentials)
      user.value = userData
      return true
    } catch (e) {
      error.value = e.message
      user.value = null
      return false
    } finally {
      loading.value = false
    }
  }

  // 注册
  async function register(userData) {
    loading.value = true
    error.value = null
    try {
      await authService.register(userData)
      return true
    } catch (e) {
      error.value = e.message
      return false
    } finally {
      loading.value = false
    }
  }

  // 登出
  function logout() {
    authService.logout()
    user.value = null
  }

  return { user, loading, error, login, register, logout, checkAuth }
})
