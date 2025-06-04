<template>
  <div class="sidebar-sticky pt-3">
    <!-- 顶部切换组件 -->
    <div class="view-toggle mb-3 btn-group btn-group-sm w-100" role="group">
      <button 
        type="button" 
        class="btn btn-outline-primary"
        :class="{ active: currentView === 'all' }"
        @click="sidebarStore.setView('all')"
      >
        全部
      </button>

      <button 
        type="button" 
        class="btn btn-outline-primary"
        :class="{ active: currentView === 'calendar' }"
        @click="sidebarStore.setView('calendar')"
      >
        日历
      </button>
      <button 
        type="button" 
        class="btn btn-outline-primary"
        :class="{ active: currentView === 'reminder' }"
        @click="sidebarStore.setView('reminder')"
      >
        提醒
      </button>
    </div>

    <!-- 根据切换状态动态显示组件 -->
    <div class="sidebar-calendar mb-4">
      <MiniCalendar 
        v-if="currentView === 'calendar' || currentView === 'all'"
        :initial-date="selectedDate" 
        @date-selected="onDateSelected"
      />
      <ReminderWidget 
        v-if="currentView === 'reminder' || currentView === 'all'"
        class="mt-3"
      />
    </div>

    <!-- 导航菜单 -->
    <ul class="nav flex-column">
      <li class="nav-item">
        <router-link class="nav-link" to="/home" exact-active-class="active">
          <i class="bi bi-house-door me-2"></i> 首页
        </router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" to="/todos" active-class="active">
          <i class="bi bi-list-check me-2"></i> 待办事项
        </router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" to="/calendar" active-class="active">
          <i class="bi bi-calendar3 me-2"></i> 日历视图
        </router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" to="/categories" active-class="active">
          <i class="bi bi-folder me-2"></i> 分类管理
        </router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" to="/tags" active-class="active">
          <i class="bi bi-tag me-2"></i> 标签管理
        </router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" to="/statistics" active-class="active">
          <i class="bi bi-bar-chart-line me-2"></i> 待办数据统计
        </router-link>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { storeToRefs } from 'pinia'
import { useSidebarStore } from '@/stores/sidebar'
import MiniCalendar from '../calendar/MiniCalendar.vue'
import ReminderWidget from '../reminder/ReminderWidget.vue'

const sidebarStore = useSidebarStore()
const { currentView, selectedDate } = storeToRefs(sidebarStore)

function onDateSelected(date) {
  sidebarStore.setSelectedDate(date)
}
</script>


<style scoped>
.sidebar-sticky {
  position: sticky;
  top: 0;
  height: calc(100vh - 48px);
  padding-top: 0.5rem;
  overflow-x: hidden;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
}

.sidebar-calendar {
  flex-shrink: 0;
}

/* 按钮风格可以进一步自定义 */
.view-toggle .btn {
  font-size: 13px;
  border-radius: 20px;
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
</style>
