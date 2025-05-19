<template>
  <MainLayout>
    <div class="calendar-view">
      <Calendar :initial-date="initialDate" />
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import Calendar from '../components/calendar/Calendar.vue';
import MainLayout from '../components/layout/MainLayout.vue';

const route = useRoute();
const initialDate = ref(null);

onMounted(() => {
  // 检查URL查询参数中是否有日期
  if (route.query.date) {
    initialDate.value = new Date(route.query.date);
  }
});
</script>

<style scoped>
.calendar-view {
  padding: 10px;
}
</style>
