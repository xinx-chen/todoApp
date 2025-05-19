<template>
  <div class="mini-calendar">
    <v-calendar 
      :attributes="attributes"
      :masks="masks"
      :first-day-of-week="1"
      :min-date="new Date()"
      :is-expanded="false"
      title-position="left"
      @dayclick="onDayClick"
    />
    <div class="calendar-footer">
      <router-link to="/calendar" class="view-full-calendar">
        查看完整日历
      </router-link>
    </div>
  </div>
</template>

<script setup>
// 脚本部分保持不变
</script>

<style scoped>
.mini-calendar {
  padding: 5px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

/* 自定义日历样式 - 缩小尺寸 */
:deep(.vc-container) {
  border: none;
  border-radius: 8px;
  max-width: 100%;
  font-size: 0.8em;
}

:deep(.vc-header) {
  padding: 2px 0;
}

:deep(.vc-weeks) {
  padding: 0;
}

:deep(.vc-day) {
  padding: 2px 0;
}

:deep(.vc-day-content) {
  font-weight: 500;
  width: 20px;
  height: 20px;
  line-height: 20px;
  font-size: 0.85em;
}

:deep(.vc-title) {
  font-size: 0.9em;
}

:deep(.vc-arrow) {
  width: 20px;
  height: 20px;
}

:deep(.vc-day-content:hover) {
  background-color: rgba(52, 152, 219, 0.1);
  border-radius: 50%;
}

.calendar-footer {
  text-align: center;
  margin-top: 5px;
  padding-top: 5px;
  border-top: 1px solid #eee;
}

.view-full-calendar {
  color: #3498db;
  text-decoration: none;
  font-size: 0.8em;
}

.view-full-calendar:hover {
  text-decoration: underline;
}
</style>
