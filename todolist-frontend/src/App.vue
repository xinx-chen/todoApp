<template>
  <div class="app">
    <router-view v-slot="{ Component }">
      <component :is="Component" />
    </router-view>
  </div>
</template>

<script setup>
import { useAuthStore } from './stores/auth'
import { onMounted } from 'vue'

const authStore = useAuthStore()

onMounted(() => {
  // 检查本地存储中是否有用户信息
  authStore.checkAuth()
})
</script>

<style>
body {
  margin: 0;
  font-family: 'Nunito', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  background-color: #f8f9fa;
}

/* 添加全局样式 */
.card {
  border-radius: 12px;
  border: none;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.card:hover {
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
}

.btn {
  border-radius: 8px;
  padding: 8px 16px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.btn-primary {
  background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
  border: none;
}

.btn-primary:hover {
  background: linear-gradient(135deg, #2980b9 0%, #1c6ea4 100%);
  transform: translateY(-2px);
}

.form-control, .form-select {
  border-radius: 8px;
  padding: 10px 15px;
  border: 1px solid #e0e0e0;
}

.form-control:focus, .form-select:focus {
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.25);
  border-color: #3498db;
}
</style>
