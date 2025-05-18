<template>
  <div class="home">
    <div class="hero-section">
      <div class="container-fluid">
        <h1 class="hero-title">欢迎使用 TodoList</h1>
        <p class="hero-subtitle">
          高效管理您的日常任务，提高工作效率
        </p>
        <div v-if="!authStore.user" class="hero-actions">
          <router-link to="/login" class="btn btn-primary btn-lg me-2">
            <i class="bi bi-box-arrow-in-right me-1"></i> 登录
          </router-link>
          <router-link to="/register" class="btn btn-outline-light btn-lg">
            <i class="bi bi-person-plus me-1"></i> 注册
          </router-link>
        </div>
        <div v-else class="hero-actions">
          <div class="d-flex flex-column align-items-center">
            <router-link to="/todos" class="btn btn-primary btn-lg mb-3">
              <i class="bi bi-list-check me-1"></i> 查看我的待办事项
            </router-link>
            <button class="btn btn-outline-light btn-lg" @click="logout">
              <i class="bi bi-box-arrow-right me-1"></i> 退出登录
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="container mt-5">
      <div class="row">
        <div class="col-md-4 mb-4" @click="navigateTo('/todos')">
          <div class="feature-card">
            <div class="feature-icon">
              <i class="bi bi-list-check"></i>
            </div>
            <h3>任务管理</h3>
            <p>
              创建、编辑和删除待办事项，设置优先级和截止日期，标记完成状态。
            </p>
          </div>
        </div>
        
        <div class="col-md-4 mb-4" @click="navigateTo('/categories')">
          <div class="feature-card">
            <div class="feature-icon">
              <i class="bi bi-folder"></i>
            </div>
            <h3>分类整理</h3>
            <p>
              使用自定义分类和颜色标签，将待办事项分门别类，一目了然。
            </p>
          </div>
        </div>
        
        <div class="col-md-4 mb-4" @click="navigateTo('/todos')">
          <div class="feature-card">
            <div class="feature-icon">
              <i class="bi bi-bell"></i>
            </div>
            <h3>提醒功能</h3>
            <p>
              设置提醒时间，系统会在任务到期前通知您，避免错过重要事项。
            </p>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 用户信息和退出登录区域 -->
    <div v-if="authStore.user" class="container mt-4">
      <div class="user-profile-card">
        <div class="user-info">
          <div class="avatar">
            <i class="bi bi-person-circle"></i>
          </div>
          <div class="details">
            <h4>{{ authStore.user.username || '用户' }}</h4>
            <p class="text-muted">欢迎使用 TodoList</p>
          </div>
        </div>
        <button class="btn btn-outline-danger" @click="logout">
          <i class="bi bi-box-arrow-right me-1"></i> 退出登录
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useAuthStore } from '../stores/auth';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();

const navigateTo = (path) => {
  if (authStore.user) {
    router.push(path);
  } else {
    router.push('/login');
  }
};

const logout = () => {
  authStore.logout();
  router.push('/login');
};
</script>

<style scoped>
.hero-section {
  background: linear-gradient(135deg, #3a1c71 0%, #d76d77 50%, #ffaf7b 100%);
  color: white;
  padding: 80px 0;
  border-radius: 16px;
  margin-bottom: 40px;
  text-align: center;
}

.hero-title {
  font-size: 3rem;
  font-weight: 700;
  margin-bottom: 20px;
}

.hero-subtitle {
  font-size: 1.5rem;
  margin-bottom: 30px;
  opacity: 0.9;
}

.hero-actions {
  margin-top: 30px;
}

.feature-card {
  background-color: white;
  border-radius: 12px;
  padding: 30px;
  height: 100%;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  text-align: center;
  cursor: pointer;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.feature-icon {
  font-size: 2.5rem;
  margin-bottom: 20px;
  color: #3498db;
}

.feature-card h3 {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 15px;
  color: #2c3e50;
}

.feature-card p {
  color: #6c757d;
  line-height: 1.6;
}

/* 用户信息卡片样式 */
.user-profile-card {
  background-color: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar {
  font-size: 2.5rem;
  color: #3498db;
  margin-right: 20px;
}

.details h4 {
  margin-bottom: 5px;
  color: #2c3e50;
}

@media (max-width: 768px) {
  .user-profile-card {
    flex-direction: column;
    text-align: center;
  }
  
  .user-info {
    flex-direction: column;
    margin-bottom: 20px;
  }
  
  .avatar {
    margin-right: 0;
    margin-bottom: 10px;
  }
}
</style>
