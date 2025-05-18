<template>
  <div class="card filter-card mb-4">
    <div class="card-body">
      <h5 class="card-title d-flex align-items-center mb-3">
        <i class="bi bi-funnel me-2"></i>
        筛选待办事项
      </h5>
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
import { ref, watch } from 'vue'

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
  search: ''
})

const applyFilters = () => {
  // 创建一个新对象，只包含有值的过滤条件
  const activeFilters = {}
  
  if (filters.value.done !== '') activeFilters.done = filters.value.done
  if (filters.value.categoryId) activeFilters.categoryId = filters.value.categoryId
  if (filters.value.priority) activeFilters.priority = filters.value.priority
  if (filters.value.tagId) activeFilters.tagId = filters.value.tagId
  if (filters.value.search) activeFilters.search = filters.value.search
  
  emit('filter-change', activeFilters)
}

const resetFilters = () => {
  filters.value = {
    done: '',
    categoryId: '',
    priority: '',
    tagId: '',
    search: ''
  }
  emit('filter-change', {})
}
</script>

<style scoped>
.filter-card {
  background-color: #f8f9fa;
  border: none;
}
</style>