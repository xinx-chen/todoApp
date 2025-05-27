<template>
  <div class="calendar-container">
    <div class="calendar-header">
      <div class="calendar-controls">
        <button class="btn btn-sm btn-outline-primary me-2" @click="today">今天</button>
        <button class="btn btn-sm btn-outline-secondary me-2" @click="prev">
          <i class="bi bi-chevron-left"></i>
        </button>
        <button class="btn btn-sm btn-outline-secondary me-3" @click="next">
          <i class="bi bi-chevron-right"></i>
        </button>
        <h4 class="current-date">{{ currentMonthYear }}</h4>
      </div>
      <div class="view-controls">
        <div class="btn-group">
          <button 
            class="btn btn-sm" 
            :class="viewType === 'day' ? 'btn-primary' : 'btn-outline-primary'"
            @click="viewType = 'day'"
          >日</button>
          <button 
            class="btn btn-sm" 
            :class="viewType === 'week' ? 'btn-primary' : 'btn-outline-primary'"
            @click="viewType = 'week'"
          >周</button>
          <button 
            class="btn btn-sm" 
            :class="viewType === 'month' ? 'btn-primary' : 'btn-outline-primary'"
            @click="viewType = 'month'"
          >月</button>
        </div>
      </div>
      <div class="calendar-legend">
        <div class="legend-item">
          <span class="dot" style="background-color: #2ecc71;"></span>
          <span>法定节假日</span>
        </div>
      </div>
    </div>

    <!-- 月视图 -->
    <div v-if="viewType === 'month'" class="month-view">
      <div class="month-grid">
        <div class="month-header">
          <div class="weekday" v-for="day in ['周一', '周二', '周三', '周四', '周五', '周六', '周日']" :key="day">
            {{ day }}
          </div>
        </div>
        <div class="month-body">
        <div 
          v-for="day in monthDays" 
          :key="day.key" 
          class="month-day" 
          :class="{ 
            'other-month': !day.isCurrentMonth, 
            'today': day.isToday,
            'has-holiday': isHoliday(day.fullDate)
          }"
          @click="selectDay(day.fullDate)"
        >
          <div class="day-number">{{ day.date }}</div>
          <div v-if="isHoliday(day.fullDate)" class="holiday-marker">
            {{ getHolidayName(day.fullDate) }}
          </div>
          <!-- 添加待办事项显示 -->
          <div class="todo-items" v-if="getTodosForDate(day.fullDate).length > 0">
            <div
            v-for="todo in getTodosForDate(day.fullDate)"
            :key="todo.id"
            class="todo-item"
            :class="`priority-${todo.priority}`"
          >
            {{ todo.description }}
          </div>
          </div>
        </div>
        </div>
      </div>
    </div>

    <!-- 周视图 -->
    <div v-if="viewType === 'week'" class="week-view">
      <div class="week-header">
        <div class="day-header" v-for="day in weekDays" :key="day.key">
          <div class="day-name">{{ day.name }}</div>
          <div class="day-date" :class="{ 'today': day.isToday }">{{ day.date }}</div>
        </div>
      </div>
      <div class="week-body">
        <div class="day-column" v-for="day in weekDays" :key="day.key">
        <!-- 修改day-content部分 -->
        <div 
          class="day-content" 
          :class="{ 
            'today': day.isToday, 
            'has-holiday': isHoliday(day.fullDate)
          }"
          @click="selectDay(day.fullDate)"
        >
          <div v-if="isHoliday(day.fullDate)" class="holiday-item">
            {{ getHolidayName(day.fullDate) }}
          </div>
          <!-- 添加待办事项显示 -->
          <div class="todo-items" v-if="getTodosForDate(day.fullDate).length > 0">
            <div
            v-for="todo in getTodosForDate(day.fullDate)"
            :key="todo.id"
            class="todo-item"
            :class="`priority-${todo.priority}`"
          >
            {{ todo.description }}
          </div>
          </div>
          <div v-else class="no-events">
            无事
          </div>
        </div>
        </div>
      </div>
    </div>

    <!-- 日视图 -->
    <div v-if="viewType === 'day'" class="day-view">
      <div class="day-header">
        <h5>{{ formattedSelectedDate }}</h5>
      </div>
      <!-- 修改day-body部分 -->
      <div class="day-body">
        <div v-if="isHoliday(selectedDate)" class="holiday-detail">
          <div class="holiday-title">{{ getHolidayName(selectedDate) }}</div>
          <div class="holiday-description">
            {{ getHolidayDescription(selectedDate) }}
          </div>
        </div>
        <div class="todos-list" v-if="getTodosForDate(selectedDate).length > 0">
          <h6>待办事项</h6>
          <div
          v-for="todo in getTodosForDate(selectedDate)"
          :key="todo.id"
          class="todo-item"
          :class="`priority-${todo.priority}`"
        >
          {{ todo.description }}
        </div>
        </div>
        <div v-else class="no-holiday">
          <p>{{ formattedSelectedDate }} 无事</p>
          <div class="day-hours">
            <div class="hour-row" v-for="hour in 24" :key="hour">
              <div class="hour-label">{{ hour - 1 }}:00</div>
              <div class="hour-content"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useTodoStore } from '../../stores/todo';
import { useAuthStore } from '../../stores/auth';

// 接收初始日期参数
const props = defineProps({
  initialDate: {
    type: Date,
    default: null
  }
});

// 获取 store
const todoStore = useTodoStore();
const authStore = useAuthStore();

// 日历实例引用
const calendar = ref(null);

// 视图类型：月、周、日
const viewType = ref('month'); // 默认为月视图

// 当前选中的日期
const selectedDate = ref(new Date());

// 日历配置
const masks = {
  weekdays: 'WW',
};

// 节假日数据 - 2025年中国法定节假日
const holidays = ref([
  // 元旦
  { date: new Date(2025, 0, 1), title: '元旦', color: '#2ecc71', description: '元旦是公历新年的第一天，即1月1日，是世界多数国家通用的节日。2025年放假1天，不调休。' },
  
  // 春节
  { date: new Date(2025, 1, 8), title: '除夕', color: '#2ecc71', description: '除夕是农历一年的最后一天，是中国最重要的传统节日之一，家家户户都要团圆，吃年夜饭。' },
  { date: new Date(2025, 1, 9), title: '春节初一', color: '#2ecc71', description: '春节是中国最重要的传统节日，也是中国人一年当中最隆重、最热闹的一个古老节日。2025年春节放假8天。' },
  { date: new Date(2025, 1, 10), title: '春节初二', color: '#2ecc71', description: '春节是中国最重要的传统节日，初二一般是回娘家的日子。' },
  { date: new Date(2025, 1, 11), title: '春节初三', color: '#2ecc71', description: '春节是中国最重要的传统节日，初三称为"赤狗日"，传统上人们会在这天休息。' },
  { date: new Date(2025, 1, 12), title: '春节初四', color: '#2ecc71', description: '春节是中国最重要的传统节日，初四是恢复营业的日子，称为"接财神"。' },
  { date: new Date(2025, 1, 13), title: '春节初五', color: '#2ecc71', description: '春节是中国最重要的传统节日，初五称为"破五"，是迎财神的日子。' },
  { date: new Date(2025, 1, 14), title: '春节初六', color: '#2ecc71', description: '春节是中国最重要的传统节日，初六是"送穷日"，人们会扔掉垃圾，象征性地把穷赶走。' },
  { date: new Date(2025, 1, 15), title: '春节初七', color: '#2ecc71', description: '春节是中国最重要的传统节日，初七是"人日"，传统上认为是人类的生日。' },
  
  // 清明节
  { date: new Date(2025, 3, 4), title: '清明节', color: '#2ecc71', description: '清明节是中国传统节日，也是最重要的祭祀节日之一，是祭祖和扫墓的日子。2025年放假1天。' },
  
  // 劳动节
  { date: new Date(2025, 4, 1), title: '劳动节', color: '#2ecc71', description: '国际劳动节又称"五一国际劳动节"，是世界上80多个国家的全国性节日。2025年放假5天。' },
  { date: new Date(2025, 4, 2), title: '劳动节假期', color: '#2ecc71', description: '劳动节假期第二天。' },
  { date: new Date(2025, 4, 3), title: '劳动节假期', color: '#2ecc71', description: '劳动节假期第三天。' },
  { date: new Date(2025, 4, 4), title: '劳动节假期', color: '#2ecc71', description: '劳动节假期第四天。' },
  { date: new Date(2025, 4, 5), title: '劳动节假期', color: '#2ecc71', description: '劳动节假期第五天。' },
  
  // 端午节 - 修正月份从5改为4
  { date: new Date(2025, 4, 31), title: '端午节', color: '#2ecc71', description: '端午节是中国民间的传统节日，也是最重要的传统节日之一，已被列入世界非物质文化遗产名录。2025年放假1天。' },
  
  // 中秋节和国庆节
  { date: new Date(2025, 9, 1), title: '国庆节', color: '#2ecc71', description: '国庆节是中华人民共和国成立的纪念日，定为每年的10月1日。2025年国庆节与中秋节连休8天。' },
  { date: new Date(2025, 9, 2), title: '国庆节假期', color: '#2ecc71', description: '国庆节假期第二天。' },
  { date: new Date(2025, 9, 3), title: '国庆节假期', color: '#2ecc71', description: '国庆节假期第三天。' },
  { date: new Date(2025, 9, 4), title: '国庆节假期', color: '#2ecc71', description: '国庆节假期第四天。' },
  { date: new Date(2025, 9, 5), title: '中秋节', color: '#2ecc71', description: '中秋节是中国的传统节日，也是仅次于春节的第二大传统节日，是家人团圆的象征。2025年与国庆节连休。' },
  { date: new Date(2025, 9, 6), title: '国庆、中秋假期', color: '#2ecc71', description: '国庆节与中秋节连休假期。' },
  { date: new Date(2025, 9, 7), title: '国庆、中秋假期', color: '#2ecc71', description: '国庆节与中秋节连休假期。' },
  { date: new Date(2025, 9, 8), title: '国庆、中秋假期', color: '#2ecc71', description: '国庆节与中秋节连休假期。' },
]);



// 格式化选中的日期
const formattedSelectedDate = computed(() => {
  if (!selectedDate.value) return '';
  return selectedDate.value.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
});

// 当前月份和年份
const currentMonthYear = computed(() => {
  return selectedDate.value.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long'
  });
});

// 判断日期是否为节假日
const isHoliday = (date) => {
  if (!date) return false;
  
  return holidays.value.some(holiday => {
    return holiday.date.getFullYear() === date.getFullYear() &&
           holiday.date.getMonth() === date.getMonth() &&
           holiday.date.getDate() === date.getDate();
  });
};

// 获取节假日名称
const getHolidayName = (date) => {
  if (!date) return '';
  
  const holiday = holidays.value.find(holiday => {
    return holiday.date.getFullYear() === date.getFullYear() &&
           holiday.date.getMonth() === date.getMonth() &&
           holiday.date.getDate() === date.getDate();
  });
  
  return holiday ? holiday.title : '';
};

// 获取节假日描述
const getHolidayDescription = (date) => {
  if (!date) return '';
  
  const holiday = holidays.value.find(holiday => {
    return holiday.date.getFullYear() === date.getFullYear() &&
           holiday.date.getMonth() === date.getMonth() &&
           holiday.date.getDate() === date.getDate();
  });
  
  return holiday ? holiday.description : '';
};

// 组合所有日历标记
const attributes = computed(() => [
  // 节假日标记
  ...holidays.value.map(holiday => ({
    key: `holiday-${holiday.date}`,
    dates: holiday.date,
    dot: { color: holiday.color },
    popover: {
      label: holiday.title,
    }
  }))
]);

// 当前周的日期
const weekDays = computed(() => {
  const days = [];
  const currentDate = new Date(selectedDate.value);
  const dayOfWeek = currentDate.getDay() || 7; // 将周日的0转为7
  const mondayDate = new Date(currentDate);
  mondayDate.setDate(currentDate.getDate() - (dayOfWeek - 1));
  
  for (let i = 0; i < 7; i++) {
    const date = new Date(mondayDate);
    date.setDate(mondayDate.getDate() + i);
    
    const today = new Date();
    const isToday = date.getDate() === today.getDate() && 
                   date.getMonth() === today.getMonth() && 
                   date.getFullYear() === today.getFullYear();
    
    days.push({
      key: `day-${i}`,
      name: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'][i],
      date: date.getDate(),
      fullDate: new Date(date),
      isToday: isToday
    });
  }
  
  return days;
});

// 当前月的所有日期
const monthDays = computed(() => {
  const days = [];
  const currentDate = new Date(selectedDate.value);
  const year = currentDate.getFullYear();
  const month = currentDate.getMonth();
  
  // 获取当月第一天是星期几
  const firstDay = new Date(year, month, 1);
  const firstDayOfWeek = firstDay.getDay() || 7; // 将周日的0转为7
  
  // 获取当月的天数
  const lastDay = new Date(year, month + 1, 0);
  const daysInMonth = lastDay.getDate();
  
  // 获取上个月的最后几天
  const prevMonthLastDay = new Date(year, month, 0);
  const prevMonthDays = prevMonthLastDay.getDate();
  
  // 添加上个月的最后几天
  for (let i = firstDayOfWeek - 1; i > 0; i--) {
    const date = new Date(year, month - 1, prevMonthDays - i + 1);
    days.push({
      key: `prev-${i}`,
      date: prevMonthDays - i + 1,
      fullDate: date,
      isCurrentMonth: false,
      isToday: false
    });
  }
  
  // 添加当月的天数
  const today = new Date();
  for (let i = 1; i <= daysInMonth; i++) {
    const date = new Date(year, month, i);
    const isToday = i === today.getDate() && 
                   month === today.getMonth() && 
                   year === today.getFullYear();
    
    days.push({
      key: `current-${i}`,
      date: i,
      fullDate: date,
      isCurrentMonth: true,
      isToday: isToday
    });
  }
  
  // 添加下个月的前几天
  const totalDays = 42; // 6行 x 7列
  const remainingDays = totalDays - days.length;
  for (let i = 1; i <= remainingDays; i++) {
    const date = new Date(year, month + 1, i);
    days.push({
      key: `next-${i}`,
      date: i,
      fullDate: date,
      isCurrentMonth: false,
      isToday: false
    });
  }
  
  return days;
});

// 点击日期时的处理函数
const onDayClick = (day) => {
  selectedDate.value = day.date;
  viewType.value = 'day'; // 切换到日视图
};

// 选择日期
const selectDay = (date) => {
  selectedDate.value = date;
  viewType.value = 'day'; // 切换到日视图
};

// 导航到今天
const today = () => {
  selectedDate.value = new Date();
};

// 导航到上一个时间段
const prev = () => {
  if (viewType.value === 'month') {
    const newDate = new Date(selectedDate.value);
    newDate.setMonth(newDate.getMonth() - 1);
    selectedDate.value = newDate;
  } else if (viewType.value === 'week') {
    const newDate = new Date(selectedDate.value);
    newDate.setDate(newDate.getDate() - 7);
    selectedDate.value = newDate;
  } else if (viewType.value === 'day') {
    const newDate = new Date(selectedDate.value);
    newDate.setDate(newDate.getDate() - 1);
    selectedDate.value = newDate;
  }
};

// 导航到下一个时间段
const next = () => {
  if (viewType.value === 'month') {
    const newDate = new Date(selectedDate.value);
    newDate.setMonth(newDate.getMonth() + 1);
    selectedDate.value = newDate;
  } else if (viewType.value === 'week') {
    const newDate = new Date(selectedDate.value);
    newDate.setDate(newDate.getDate() + 7);
    selectedDate.value = newDate;
  } else if (viewType.value === 'day') {
    const newDate = new Date(selectedDate.value);
    newDate.setDate(newDate.getDate() + 1);
    selectedDate.value = newDate;
  }
};

// 监听初始日期变化
watch(() => props.initialDate, (newVal) => {
  if (newVal) {
    selectedDate.value = newVal;
  }
}, { immediate: true });

onMounted(async () => {
  // 如果有初始日期，使用它，否则使用今天
  if (props.initialDate) {
    selectedDate.value = props.initialDate;
  } else {
    selectedDate.value = new Date();
  }
  
  // 设置默认视图为月视图
  viewType.value = 'month';
});

// 在script setup部分添加以下代码

// 获取当前用户的所有待办事项
const todos = ref([]);

// 获取指定日期的待办事项
const getTodosForDate = (date) => {
  if (!date) return [];
  
  const dateStr = date.toISOString().split('T')[0];
  return todos.value.filter(todo => todo.targetDate === dateStr);
};

// 获取当前用户的待办事项
const fetchTodos = async () => {
  try {
    await todoStore.fetchTodos(authStore.user.username);
    console.log('待办事项获取成功:', todoStore.todos);
    todos.value = todoStore.todos;
  } catch (error) {
    console.error('获取待办事项失败:', error);
  }
};


// 监听selectedDate变化，当切换到日视图时获取待办事项
watch([selectedDate, viewType], ([newDate, newViewType]) => {
  if (newViewType === 'day') {
    fetchTodos();
  }
}, { immediate: true });

// 在onMounted中添加
onMounted(async () => {
  // 如果有初始日期，使用它，否则使用今天
  if (props.initialDate) {
    selectedDate.value = props.initialDate;
  } else {
    selectedDate.value = new Date();
  }
  
  // 设置默认视图为月视图
  viewType.value = 'month';
  
  // 获取初始待办事项
  await fetchTodos();
});
</script>

<style scoped>
.calendar-container {
  padding: 20px;
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 10px;
}

.calendar-controls {
  display: flex;
  align-items: center;
}

.current-date {
  margin: 0;
  font-size: 1.2rem;
}

.calendar-legend {
  display: flex;
  gap: 15px;
}

.legend-item {
  display: flex;
  align-items: center;
  font-size: 0.8rem;
  color: #666;
}

.dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  margin-right: 5px;
}

.holiday-marker {
  background-color: rgba(46, 204, 113, 0.1);
  color: #2ecc71;
  padding: 3px 6px;
  border-radius: 4px;
  font-size: 0.8rem;
  display: inline-block;
  margin-top: 5px;
}

.holiday-item {
  margin-bottom: 8px;
  padding: 10px;
  border-radius: 4px;
  background-color: rgba(46, 204, 113, 0.1);
  color: #2ecc71;
  font-size: 1rem;
  text-align: center;
}

.holiday-detail {
  padding: 20px;
  background-color: rgba(46, 204, 113, 0.05);
  border-radius: 8px;
  margin-bottom: 20px;
}

.holiday-title {
  font-size: 1.8rem;
  font-weight: 500;
  color: #2ecc71;
  margin-bottom: 15px;
}

/* 月视图样式 */
.month-view {
  margin-top: 10px;
}

.month-grid {
  display: flex;
  flex-direction: column;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  overflow: hidden;
}

.month-header {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  background-color: #f8f9fa;
  border-bottom: 1px solid #e0e0e0;
}

.weekday {
  padding: 15px;
  text-align: center;
  font-weight: 500;
  color: #666;
}

.month-body {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  grid-auto-rows: minmax(100px, auto);
}

.month-day {
  border-right: 1px solid #e0e0e0;
  border-bottom: 1px solid #e0e0e0;
  padding: 10px;
  position: relative;
  min-height: 100px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.month-day:nth-child(7n) {
  border-right: none;
}

.month-day:nth-last-child(-n+7) {
  border-bottom: none;
}

.month-day.other-month {
  background-color: #f9f9f9;
  color: #aaa;
}

.month-day.today {
  background-color: rgba(52, 152, 219, 0.05);
}

.month-day:hover {
  background-color: rgba(52, 152, 219, 0.1);
}

.day-number {
  font-size: 1.2rem;
  font-weight: 500;
  margin-bottom: 5px;
}

.holiday-marker {
  background-color: rgba(231, 76, 60, 0.1);
  color: #e74c3c;
  padding: 3px 6px;
  border-radius: 4px;
  font-size: 0.8rem;
  display: inline-block;
  margin-top: 5px;
}

/* 周视图样式 */
.week-view {
  margin-top: 10px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  overflow: hidden;
}

.week-header {
  display: flex;
  background-color: #f8f9fa;
  border-bottom: 1px solid #e0e0e0;
}

.day-header {
  flex: 1;
  padding: 15px;
  text-align: center;
  border-right: 1px solid #e0e0e0;
}

.day-header:last-child {
  border-right: none;
}

.day-name {
  font-weight: 500;
  color: #666;
}

.day-date {
  font-size: 1.5rem;
  margin-top: 5px;
}

.day-date.today {
  color: #fff;
  background-color: #3498db;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  line-height: 40px;
  margin: 5px auto 0;
}

.week-body {
  display: flex;
  min-height: 400px;
}

.day-column {
  flex: 1;
  border-right: 1px solid #e0e0e0;
  padding: 15px;
}

.day-column:last-child {
  border-right: none;
}

.day-content {
  min-height: 100%;
  padding: 10px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.day-content.today {
  background-color: rgba(52, 152, 219, 0.05);
}

.day-content:hover {
  background-color: rgba(52, 152, 219, 0.1);
  cursor: pointer;
}

.holiday-item {
  margin-bottom: 8px;
  padding: 10px;
  border-radius: 4px;
  background-color: rgba(231, 76, 60, 0.1);
  color: #e74c3c;
  font-size: 1rem;
  text-align: center;
}

.no-events {
  color: #aaa;
  font-size: 0.9rem;
  text-align: center;
  padding: 30px 0;
}

/* 日视图样式 */
.day-view {
  margin-top: 10px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  overflow: hidden;
}

.day-header {
  background-color: #f8f9fa;
  padding: 20px;
  border-bottom: 1px solid #e0e0e0;
}

.day-header h5 {
  margin: 0;
  font-size: 1.5rem;
}

.day-body {
  padding: 20px;
  min-height: 500px;
}

.holiday-detail {
  padding: 20px;
  background-color: rgba(231, 76, 60, 0.05);
  border-radius: 8px;
  margin-bottom: 20px;
}

.holiday-title {
  font-size: 1.8rem;
  font-weight: 500;
  color: #e74c3c;
  margin-bottom: 15px;
}

.holiday-description {
  font-size: 1.1rem;
  line-height: 1.6;
  color: #333;
}

.no-holiday {
  text-align: center;
  color: #666;
  padding: 20px 0;
}

.day-hours {
  margin-top: 30px;
}

.hour-row {
  display: flex;
  border-bottom: 1px solid #eee;
  min-height: 60px;
}

.hour-row:last-child {
  border-bottom: none;
}

.hour-label {
  width: 80px;
  padding: 10px;
  text-align: right;
  color: #888;
  font-size: 0.9rem;
}

.hour-content {
  flex: 1;
  padding: 10px;
}
/* 添加以下样式 */

.todo-items {
  margin-top: 5px;
}

.todo-item {
  font-size: 0.8rem;
  padding: 3px 6px;
  margin-bottom: 3px;
  background-color: rgba(52, 152, 219, 0.1);
  border-radius: 3px;
  color: #3498db;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.todos-list {
  margin-bottom: 20px;
}

.todos-list h6 {
  font-size: 1.2rem;
  margin-bottom: 15px;
  color: #333;
}

.todos-list .todo-item {
  padding: 10px;
  margin-bottom: 8px;
  background-color: #f8f9fa;
  border-left: 3px solid #3498db;
  font-size: 1rem;
}
/* 添加优先级样式 */
.todo-item.priority-0 {
  background-color: #e0e0e0;
  color: #666;
  border-left-color: #bdbdbd !important;
}

.todo-item.priority-1 {
  background-color: rgba(52, 152, 219, 0.1);
  color: #3498db;
  border-left-color: #3498db !important;
}

.todo-item.priority-2 {
  background-color: rgba(255, 165, 0, 0.1);
  color: #ff8c00;
  border-left-color: #ff8c00 !important;
}

.todo-item.priority-3 {
  background-color: rgba(231, 76, 60, 0.1);
  color: #e74c3c;
  border-left-color: #e74c3c !important;
}

/* 更新日视图中的待办事项样式 */
.todos-list .todo-item.priority-0 {
  background-color: #f8f9fa;
  border-left: 3px solid #bdbdbd;
}

.todos-list .todo-item.priority-1 {
  background-color: #f8f9fa;
  border-left: 3px solid #3498db;
}

.todos-list .todo-item.priority-2 {
  background-color: #f8f9fa;
  border-left: 3px solid #ff8c00;
}

.todos-list .todo-item.priority-3 {
  background-color: #f8f9fa;
  border-left: 3px solid #e74c3c;
}
</style>
