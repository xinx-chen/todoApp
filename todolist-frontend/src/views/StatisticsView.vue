<template>
  <MainLayout>
    <div class="todos-view">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="fw-bold text-primary">待办数据统计</h1>
      </div>
      <div v-if="loading" class="text-center py-5">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">加载中...</span>
        </div>
      </div>
      <div v-else-if="error" class="alert alert-danger">
        {{ error }}
      </div>
      <div v-else>
        <!-- 总览卡片 -->
        <div class="row mb-4">
          <div class="col-md-4 mb-3">
            <div class="card text-white bg-primary shadow-sm">
              <div class="card-body">
                <h5 class="card-title">总任务数</h5>
                <p class="card-text fs-4 fw-bold">{{ stats.total }}</p>
              </div>
            </div>
          </div>
          <div class="col-md-4 mb-3">
            <div class="card text-white bg-success shadow-sm">
              <div class="card-body">
                <h5 class="card-title">已完成</h5>
                <p class="card-text fs-4 fw-bold">{{ stats.completed }}</p>
              </div>
            </div>
          </div>
          <div class="col-md-4 mb-3">
            <div class="card text-white bg-warning shadow-sm">
              <div class="card-body">
                <h5 class="card-title">待完成</h5>
                <p class="card-text fs-4 fw-bold">{{ stats.pending }}</p>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 空状态提示 -->
        <div v-if="stats.total === 0" class="empty-state text-center py-5">
          <div class="empty-state-icon mb-4">
            <i class="fas fa-clipboard-list fa-4x text-muted"></i>
          </div>
          <h4 class="text-muted mb-3">暂无任务数据</h4>
          <p class="text-muted mb-4">您还没有创建任何任务，开始添加第一个任务吧！</p>
          <router-link to="/todos/create" class="btn btn-primary">
            <i class="fas fa-plus me-2"></i>创建新任务
          </router-link>
        </div>
        
        <!-- 分组统计 -->
        <div v-else>
          <div class="row">
            <div class="col-md-6 mb-4">
              <div class="card shadow-sm">
                <div class="card-header bg-light">
                  <h5 class="mb-0 fw-bold">按优先级统计</h5>
                </div>
                <div class="card-body p-0">
                  <ul class="list-group list-group-flush">
                    <li
                      v-for="(count, priority) in stats.byPriority"
                      :key="'priority-' + priority"
                      class="list-group-item d-flex justify-content-between align-items-center"
                      :class="getPriorityClass(priority)"
                    >
                      <span>
                        <i :class="getPriorityIcon(priority)" class="me-2"></i>
                        优先级 {{ priorityMap[priority] ?? priority }}
                      </span>
                      <span class="badge rounded-pill bg-white text-dark fw-normal">{{ count }}</span>
                    </li>
                  </ul>
                  <div v-if="Object.keys(stats.byPriority).length === 0" class="text-center py-4 text-muted">
                    <i class="fas fa-info-circle me-2"></i>暂无优先级统计数据
                  </div>
                </div>
              </div>
            </div>
            
            <div class="col-md-6 mb-4">
              <div class="card shadow-sm">
                <div class="card-header bg-light">
                  <h5 class="mb-0 fw-bold">按分类统计</h5>
                </div>
                <div class="card-body p-0">
                  <ul class="list-group list-group-flush">
                    <li
                      v-for="(count, category) in stats.byCategory"
                      :key="'category-' + category"
                      class="list-group-item d-flex justify-content-between align-items-center"
                    >
                      <span>
                        <i class="fas fa-tag me-2 text-muted"></i>
                        {{ category || '未分类' }}
                      </span>
                      <span class="badge rounded-pill bg-primary">{{ count }}</span>
                    </li>
                  </ul>
                  <div v-if="Object.keys(stats.byCategory).length === 0" class="text-center py-4 text-muted">
                    <i class="fas fa-info-circle me-2"></i>暂无分类统计数据
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import MainLayout from '../components/layout/MainLayout.vue';
import { useTodoStore } from '../stores/todo';
import { useAuthStore } from '../stores/auth';

const todoStore = useTodoStore();
const authStore = useAuthStore();
const loading = ref(true);
const error = ref(null);

const stats = computed(() => todoStore.statistics ?? {
  total: 0,
  completed: 0,
  pending: 0,
  byPriority: {},
  byCategory: {}
});

const priorityMap = {
  0: '低',
  1: '中',
  2: '高',
  3: '紧急'
};

const getPriorityClass = (priority) => {
  switch (parseInt(priority)) {
    case 0: return 'priority-low';
    case 1: return 'priority-medium';
    case 2: return 'priority-high';
    case 3: return 'priority-urgent';
    default: return '';
  }
};

const getPriorityIcon = (priority) => {
  switch (parseInt(priority)) {
    case 0: return 'fas fa-arrow-down text-success';
    case 1: return 'fas fa-minus text-primary';
    case 2: return 'fas fa-arrow-up text-warning';
    case 3: return 'fas fa-exclamation-triangle text-danger';
    default: return 'fas fa-question-circle';
  }
};

onMounted(async () => {
  try {
    await todoStore.fetchStatistics(authStore.user?.username)
  } catch (err) {
    error.value = '加载统计信息失败，请稍后重试。'
  } finally {
    loading.value = false
  }
});
</script>

<style scoped>
.todos-view {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.card {
  border: none;
  border-radius: 10px;
  transition: transform 0.2s;
}

.card:hover {
  transform: translateY(-2px);
}

.card-header {
  border-radius: 10px 10px 0 0 !important;
}

.list-group-item {
  padding: 1rem 1.25rem;
  border-left: none;
  border-right: none;
}

/* 优先级颜色样式 */
.priority-low {
  background-color: rgba(25, 135, 84, 0.1);
  border-left: 4px solid #198754;
}

.priority-medium {
  background-color: rgba(13, 110, 253, 0.1);
  border-left: 4px solid #0d6efd;
}

.priority-high {
  background-color: rgba(255, 193, 7, 0.1);
  border-left: 4px solid #ffc107;
}

.priority-urgent {
  background-color: rgba(220, 53, 69, 0.1);
  border-left: 4px solid #dc3545;
}

/* 空状态样式 */
.empty-state {
  background-color: #f8f9fa;
  border-radius: 10px;
  padding: 3rem;
  margin: 2rem 0;
}

.empty-state-icon {
  opacity: 0.6;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .todos-view {
    padding: 15px;
  }
  
  .card-body {
    padding: 1rem;
  }
  
  .list-group-item {
    padding: 0.75rem 1rem;
  }
  
  .empty-state {
    padding: 2rem 1rem;
  }
}
</style>