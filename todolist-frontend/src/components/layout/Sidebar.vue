<template>
  <div class="sidebar-sticky pt-3">
    <ul class="nav flex-column">
      <li class="nav-item">
        <router-link class="nav-link" to="/home" exact-active-class="active">
          <i class="bi bi-house-door me-2"></i>
          首页
        </router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" to="/todos" active-class="active">
          <i class="bi bi-list-check me-2"></i>
          待办事项
        </router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" to="/categories" active-class="active">
          <i class="bi bi-folder me-2"></i>
          分类管理
        </router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" to="/tags" active-class="active">
          <i class="bi bi-tag me-2"></i>
          标签管理
        </router-link>
      </li>
    </ul>
  </div>
</template>

<script setup>
// 移除了 filterTodos 相关代码
</script>

<style scoped>
.sidebar-sticky {
  position: sticky;
  top: 0;
  height: calc(100vh - 48px);
  padding-top: 0.5rem;
  overflow-x: hidden;
  overflow-y: auto;
}

.sidebar .nav-link {
  font-weight: 500;
  color: #495057;
  padding: 10px 15px;
  border-radius: 8px;
  margin-bottom: 5px;
  transition: all 0.3s ease;
}

.sidebar .nav-link:hover {
  background-color: rgba(52, 152, 219, 0.1);
}

.sidebar .nav-link.active {
  color: #3498db;
  background-color: rgba(52, 152, 219, 0.1);
}

/* 移除了不再需要的样式 */
</style>
