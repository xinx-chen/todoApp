<template>
  <div class="reminder-widget">
    <div class="reminder-header">
      <i class="bi bi-bell-fill me-2 text-warning" style="font-size: 20px;"></i>
      <span>提醒事项</span>
    </div>
    <div class="reminder-list" v-if="reminders.length">
      <div 
        class="reminder-item" 
        v-for="(reminder, index) in reminders" 
        :key="reminder.id"
      >
        <div class="reminder-title">
          {{ reminder.description }}
        </div>
        <div class="reminder-time text-muted">
          {{ formatTime(reminder.reminderTime) }}
        </div>
      </div>
    </div>
    <div class="no-reminders text-muted" v-else>
      未来24小时内没有提醒事项。
    </div>
    <div class="reminder-footer text-center mt-2">
      <router-link to="/todos" class="text-decoration-none text-primary view-full">
        查看全部待办
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { onMounted, computed, watch } from 'vue'
import { useTodoStore } from '../../stores/todo'
import { useAuthStore } from '../../stores/auth'
import dayjs from 'dayjs'

const todoStore = useTodoStore()
const authStore = useAuthStore()

onMounted(() => {
  if (authStore.user?.username) {
    todoStore.fetchReminders(authStore.user.username)
  }
})

const reminders = computed(() => todoStore.reminders || [])

function formatTime(time) {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

</script>

<style scoped>
.reminder-header {
  font-weight: 600;
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  font-size: 18px;
}

.reminder-item {
  padding: 10px 0;
  border-bottom: 1px solid #eee;
  position: relative;
  padding-left: 20px;
}

.reminder-item::before {
  content: '';
  position: absolute;
  left: 0;
  top: 14px;
  width: 8px;
  height: 8px;
  background-color: #42b983; /* Vue绿 可替换为其他颜色 */
  border-radius: 50%;
}

.reminder-title {
  font-weight: 500;
  font-size: 16px;
}

.reminder-time {
  font-size: 14px;
  color: #888;
}

.no-reminders {
  padding: 10px 0;
  text-align: center;
  font-size: 14px;
}

.reminder-footer {
  border-top: 1px solid #eee;
  padding-top: 8px;
  font-size: 16px;
}

.reminder-footer:hover{
    text-decoration: underline;
}

.reminder-widget {
  width: 98%;
  max-height: 260px; /* 固定组件整体高度 */
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 3px rgba(0, 0, 0, 0.1);
  padding: 12px;
  font-size: 16px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.reminder-list {
  overflow-y: auto;
  flex: 1;
  padding-right: 4px; /* 防止滚动条遮挡文字 */
}

/* 滚动条美化 */
.reminder-list::-webkit-scrollbar {
  width: 6px;
}

.reminder-list::-webkit-scrollbar-thumb {
  background-color: #ccc;
  border-radius: 4px;
}

.reminder-list::-webkit-scrollbar-track {
  background-color: transparent;
}

</style>
