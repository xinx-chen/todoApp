<template>
  <header class="navbar navbar-dark sticky-top bg-primary flex-md-nowrap p-0 shadow">
    <a class="navbar-brand col-md-3 col-lg-2 me-0 px-3" href="#">TodoList 应用</a>
    <button class="navbar-toggler position-absolute d-md-none collapsed" type="button" 
            data-bs-toggle="collapse" data-bs-target="#sidebarMenu" 
            aria-controls="sidebarMenu" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="w-100"></div>
    <div class="navbar-nav">
      <div class="nav-item text-nowrap d-flex align-items-center">
        <template v-if="isAuthenticated">
          <span class="nav-link px-3 text-white">{{ username }}</span>
          <button class="btn btn-outline-light me-2" @click="logout">退出登录</button>
        </template>
        <template v-else>
          <router-link to="/login" class="btn btn-outline-light me-2">登录</router-link>
          <router-link to="/register" class="btn btn-outline-light me-2">注册</router-link>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue';
import { useAuthStore } from '../../stores/auth';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();

const isAuthenticated = computed(() => {
  return authStore.user !== null;
});

const username = computed(() => {
  return authStore.user?.username || '用户';
});

const logout = () => {
  authStore.logout();
  router.push('/login');
};
</script>
