<!-- src/components/layout/MainLayout.vue -->
<template>
  <div class="main-layout">
    <AppHeader />
    <div class="container-fluid">
      <div class="row">
        <!-- 侧边栏 -->
        <div class="col-md-3 col-lg-2 d-md-block bg-light sidebar collapse" id="sidebarMenu">
          <Sidebar />
        </div>
        
        <!-- 主内容区 -->
        <div class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
          <slot></slot>
        </div>
      </div>
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import AppHeader from './AppHeader.vue'
import AppFooter from './AppFooter.vue'
import Sidebar from './Sidebar.vue'
</script>

<style scoped>
.sidebar {
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  z-index: 100;
  padding: 48px 0 0;
  box-shadow: inset -1px 0 0 rgba(0, 0, 0, 0.1);
}

@media (max-width: 767.98px) {
  .sidebar {
    top: 5rem;
  }
}
</style>
