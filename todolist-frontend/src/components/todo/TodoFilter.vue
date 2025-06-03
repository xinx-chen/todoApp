<template>
  <div class="card filter-card mb-4">
    <div class="card-body">
      <h5 class="card-title d-flex align-items-center mb-3">
        <i class="bi bi-funnel me-2"></i>
        筛选待办事项
      </h5>
      <!-- 显示今日待办 -->
      <div class="mb-3 form-check">
        <input 
          type="checkbox" 
          class="form-check-input" 
          id="showTodayOnly" 
          v-model="filters.showTodayOnly"
        >
        <label class="form-check-label">
          <i class="bi bi-calendar-check me-1"></i>
          仅显示今日待办
          <span v-if="filters.showTodayOnly" class="badge bg-success ms-2">
            {{ format(new Date(), 'yyyy-MM-dd') }}
          </span>
        </label>
      </div>
      <div class="row">
        <div class="col-md-3 mb-3">
          <label class="form-label">状态</label>
          <select class="form-select" v-model="filters.done">
            <option value="">全部</option>
            <option :value="false">未完成</option>
            <option :value="true">已完成</option>
          </select>
        </div>
        
        <div class="col-md-3 mb-3">
          <label class="form-label">分类</label>
          <select class="form-select" v-model="filters.categoryId">
            <option value="">全部</option>
            <option v-for="category in categories" :key="category.id" :value="category.id">
              {{ category.name }}
            </option>
          </select>
        </div>
        
        <div class="col-md-3 mb-3">
          <label class="form-label">优先级</label>
          <select class="form-select" v-model="filters.priority">
            <option value="">全部</option>
            <option value="0">低</option>
            <option value="1">中</option>
            <option value="2">高</option>
            <option value="3">紧急</option>
          </select>
        </div>
        
        <div class="col-md-3 mb-3">
          <label class="form-label">标签</label>
          <select class="form-select" v-model="filters.tagId">
            <option value="">全部</option>
            <option v-for="tag in tags" :key="tag.id" :value="tag.id">
              {{ tag.name }}
            </option>
          </select>
        </div>
      </div>
      
      <div class="row mt-2">
        <div class="col-md-6 mb-3">
          <label class="form-label">搜索</label>
          <div class="input-group">
            <span class="input-group-text">
              <i class="bi bi-search"></i>
            </span>
            <input 
              type="text" 
              class="form-control" 
              placeholder="搜索待办事项..." 
              v-model="filters.search"
            />
          </div>
        </div>
        
        <div class="col-md-6 d-flex align-items-end justify-content-end mb-3">
          <button class="btn btn-primary me-2" @click="applyFilters">
            <i class="bi bi-funnel-fill me-1"></i> 应用筛选
          </button>
          <button class="btn btn-outline-secondary" @click="resetFilters">
            <i class="bi bi-arrow-counterclockwise me-1"></i> 重置
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { format } from 'date-fns'

const props = defineProps({
  categories: Array,
  tags: Array
})

const emit = defineEmits(['filter-change'])

const filters = ref({
  done: '',
  categoryId: '',
  priority: '',
  tagId: '',
  search: '',
  showTodayOnly: false
})

const applyFilters = () => {
  const activeFilters = {
    ...(filters.value.done !== '' && { done: filters.value.done }),
    ...(filters.value.categoryId && { categoryId: filters.value.categoryId }),
    ...(filters.value.priority && { priority: filters.value.priority }),
    ...(filters.value.tagId && { tagId: filters.value.tagId }),
    ...(filters.value.search && { search: filters.value.search }),
    ...(filters.value.showTodayOnly && { showTodayOnly: true })
  }
  emit('filter-change', activeFilters)
}

const resetFilters = () => {
  filters.value = {
    done: '',
    categoryId: '',
    priority: '',
    tagId: '',
    search: '',
    showTodayOnly: false
  }
  emit('filter-change', {})
}
</script>

<style scoped>
.filter-card {
  background-color: #f8f9fa;
  border: none;
}
.form-check-label[for="showTodayOnly"] {
  font-weight: 500;
  color: #2c3e50;
}
.form-check-input:checked[type="checkbox"]#showTodayOnly {
  background-color: #3498db;
  border-color: #3498db;
}
/* 选中状态更明显 */
.form-check-input:checked[type="checkbox"] {
  background-color: var(--bs-success);
  border-color: var(--bs-success);
}

/* 日期标记样式 */
.badge {
  font-size: 0.75em;
  font-weight: normal;
}
</style>