<template>
  <div class="mini-calendar">
    <div class="calendar-header">
      <span class="month-title">{{ currentMonthYear }}</span>
      <div class="nav-buttons">
        <button class="nav-btn" @click="prevMonth">
          <i class="bi bi-chevron-left"></i>
        </button>
        <button class="nav-btn" @click="nextMonth">
          <i class="bi bi-chevron-right"></i>
        </button>
      </div>
    </div>
    <div class="weekday-header">
      <div class="weekday" v-for="day in weekdays" :key="day">{{ day }}</div>
    </div>
    <div class="days-grid">
      <div 
        v-for="day in calendarDays" 
        :key="day.id"
        class="day-cell"
        :class="{
          'other-month': !day.isCurrentMonth,
          'today': day.isToday,
          'selected': isSelected(day.date),
          'has-holiday': isHoliday(day.date)
        }"
        @click="selectDate(day.date)"
      >
        {{ day.dayNumber }}
      </div>
    </div>
    <div class="calendar-footer">
      <router-link to="/calendar" class="view-full-calendar text-primary">
        查看完整日历
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useTodoStore } from '../../stores/todo';
import { useAuthStore } from '../../stores/auth';

// 获取 store
const todoStore = useTodoStore();
const authStore = useAuthStore();

// 接收初始日期参数
const props = defineProps({
  initialDate: {
    type: Date,
    default: null
  }
});

const emit = defineEmits(['date-selected']);

// 当前选中的日期和当前显示的月份
const selectedDate = ref(new Date());
const currentMonth = ref(new Date().getMonth());
const currentYear = ref(new Date().getFullYear());

// 星期几标题
const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];

// 当前月份和年份的显示文本
const currentMonthYear = computed(() => {
  const monthNames = ['一月', '二月', '三月', '四月', '五月', '六月', 
                      '七月', '八月', '九月', '十月', '十一月', '十二月'];
  return `${monthNames[currentMonth.value]} ${currentYear.value}`;
});

// 节假日数据
const holidays = ref([
  { date: new Date(2025, 0, 1), title: '元旦', color: '#e74c3c' },
  { date: new Date(2025, 1, 10), title: '春节初一', color: '#e74c3c' },
  { date: new Date(2025, 1, 11), title: '春节初二', color: '#e74c3c' },
  { date: new Date(2025, 1, 12), title: '春节初三', color: '#e74c3c' },
  { date: new Date(2025, 3, 5), title: '清明节', color: '#e74c3c' },
  { date: new Date(2025, 4, 1), title: '劳动节', color: '#e74c3c' },
  { date: new Date(2025, 5, 22), title: '端午节', color: '#e74c3c' },
  { date: new Date(2025, 9, 1), title: '国庆节', color: '#e74c3c' },
  { date: new Date(2025, 9, 2), title: '国庆节假期', color: '#e74c3c' },
  { date: new Date(2025, 9, 3), title: '国庆节假期', color: '#e74c3c' },
  { date: new Date(2025, 9, 4), title: '中秋节', color: '#e74c3c' },
]);

// 计算当前月的日历数据
const calendarDays = computed(() => {
  const days = [];
  const firstDay = new Date(currentYear.value, currentMonth.value, 1);
  const lastDay = new Date(currentYear.value, currentMonth.value + 1, 0);
  
  // 获取当月第一天是星期几
  let firstDayOfWeek = firstDay.getDay(); // 0是周日，1是周一...
  
  // 获取上个月的最后几天
  const prevMonthLastDay = new Date(currentYear.value, currentMonth.value, 0);
  const daysInPrevMonth = prevMonthLastDay.getDate();
  
  // 添加上个月的最后几天
  for (let i = 0; i < firstDayOfWeek; i++) {
    const prevMonthDay = daysInPrevMonth - firstDayOfWeek + i + 1;
    const date = new Date(currentYear.value, currentMonth.value - 1, prevMonthDay);
    days.push({
      id: `prev-${prevMonthDay}`,
      dayNumber: prevMonthDay,
      date: date,
      isCurrentMonth: false,
      isToday: isSameDay(date, new Date())
    });
  }
  
  // 添加当月的天数
  for (let i = 1; i <= lastDay.getDate(); i++) {
    const date = new Date(currentYear.value, currentMonth.value, i);
    days.push({
      id: `current-${i}`,
      dayNumber: i,
      date: date,
      isCurrentMonth: true,
      isToday: isSameDay(date, new Date())
    });
  }
  
  // 添加下个月的前几天以填满6行7列
  const totalCells = 42; // 6行 x 7列
  const nextMonthDays = totalCells - days.length;
  for (let i = 1; i <= nextMonthDays; i++) {
    const date = new Date(currentYear.value, currentMonth.value + 1, i);
    days.push({
      id: `next-${i}`,
      dayNumber: i,
      date: date,
      isCurrentMonth: false,
      isToday: isSameDay(date, new Date())
    });
  }
  
  return days;
});

// 判断两个日期是否是同一天
function isSameDay(date1, date2) {
  return date1.getDate() === date2.getDate() &&
         date1.getMonth() === date2.getMonth() &&
         date1.getFullYear() === date2.getFullYear();
}

// 判断日期是否被选中
function isSelected(date) {
  return isSameDay(date, selectedDate.value);
}

// 判断日期是否为节假日
function isHoliday(date) {
  return holidays.value.some(holiday => isSameDay(holiday.date, date));
}

// 选择日期
function selectDate(date) {
  selectedDate.value = date;
  emit('date-selected', date);
}

// 上个月
function prevMonth() {
  if (currentMonth.value === 0) {
    currentMonth.value = 11;
    currentYear.value--;
  } else {
    currentMonth.value--;
  }
}

// 下个月
function nextMonth() {
  if (currentMonth.value === 11) {
    currentMonth.value = 0;
    currentYear.value++;
  } else {
    currentMonth.value++;
  }
}

// 监听初始日期变化
watch(() => props.initialDate, (newVal) => {
  if (newVal) {
    selectedDate.value = newVal;
    currentMonth.value = newVal.getMonth();
    currentYear.value = newVal.getFullYear();
  }
}, { immediate: true });

onMounted(() => {
  // 如果有初始日期，使用它，否则使用今天
  if (props.initialDate) {
    selectedDate.value = props.initialDate;
    currentMonth.value = props.initialDate.getMonth();
    currentYear.value = props.initialDate.getFullYear();
  }
});
</script>

<style scoped>
.mini-calendar {
  width: 98%;
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 2px 3px rgba(75, 75, 75, 0.1);
  padding: 10px;
  font-size: 14px;
}

.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.month-title {
  font-weight: 500;
  font-size: 15px;
}

.nav-buttons {
  display: flex;
  gap: 4px;
}

.nav-btn {
  background: none;
  border: none;
  cursor: pointer;
  padding: 2px 4px;
  color: #666;
  border-radius: 4px;
}

.nav-btn:hover {
  background-color: #f0f0f0;
}

.weekday-header {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  text-align: center;
  font-size: 12px;
  color: #666;
  margin-bottom: 4px;
}

.weekday {
  padding: 2px 0;
}

.days-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  grid-gap: 2px;
}

.day-cell {
  aspect-ratio: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  border-radius: 50%;
  font-size: 13px;
  transition: background-color 0.2s;
}

.day-cell:hover {
  background-color: #f0f0f0;
}

.day-cell.other-month {
  color: #bbb;
}

.day-cell.today {
  background-color: #e6f7ff;
  font-weight: bold;
  color: #1890ff;
}

.day-cell.selected {
  background-color: #1890ff;
  color: white;
}

.day-cell.has-holiday {
  color: #e74c3c;
  font-weight: 500;
}

.day-cell.selected.has-holiday {
  color: white;
}

.calendar-footer {
  text-align: center;
  margin-top: 8px;
  padding-top: 6px;
  border-top: 1px solid #eee;
}

.view-full-calendar {
  text-decoration: none;
  font-size: 16px;
}

.view-full-calendar:hover {
  text-decoration: underline;
}
</style>
