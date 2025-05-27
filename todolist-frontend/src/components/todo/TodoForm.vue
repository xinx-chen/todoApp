<template>
  <form @submit.prevent="handleSubmit">
    <div class="mb-3">
      <label for="description" class="form-label">描述</label>
      <input
        type="text"
        class="form-control"
        id="description"
        v-model="form.description"
        required
      />
    </div>
    
    <div class="mb-3">
      <label for="targetDate" class="form-label">目标日期</label>
      <input
        type="date"
        class="form-control"
        id="targetDate"
        v-model="form.targetDate"
        required
      />
    </div>
    
    <div class="mb-3">
      <label for="category" class="form-label">分类</label>
      <select class="form-select" id="category" v-model="form.categoryId">
        <option value="">无分类</option>
        <option v-for="category in categories" :key="category.id" :value="category.id">
          {{ category.name }}
        </option>
      </select>
    </div>
          <div class="mb-3">
      <label for="priority" class="form-label">优先级</label>
      <select class="form-select" id="priority" v-model="form.priority">
        <option value="0">低</option>
        <option value="1">中</option>
        <option value="2">高</option>
        <option value="3">紧急</option>
      </select>
    </div>
    
    <div class="mb-3">
      <label for="reminderTime" class="form-label">提醒时间</label>
      <input
        type="datetime-local"
        class="form-control"
        id="reminderTime"
        v-model="form.reminderTime"
      />
    </div>
    
    <div class="mb-3">
      <label class="form-label">标签</label>
      <div class="d-flex flex-wrap gap-2">
        <div v-for="tag in tags" :key="tag.id" class="form-check">
          <input
            class="form-check-input"
            type="checkbox"
            :id="`tag-${tag.id}`"
            :value="tag.id"
            v-model="form.tagIds"
          />
          <label class="form-check-label" :for="`tag-${tag.id}`" :style="{ color: tag.color }">
            {{ tag.name }}
          </label>
        </div>
      </div>
    </div>
    
    <div class="mb-3">
      <label for="notes" class="form-label">备注</label>
      <textarea
        class="form-control"
        id="notes"
        rows="3"
        v-model="form.notes"
      ></textarea>
    </div>
    
    <div class="mb-3 form-check">
      <input type="checkbox" class="form-check-input" id="done" v-model="form.done" />
      <label class="form-check-label" for="done">已完成</label>
    </div>
    
    <div class="d-flex justify-content-end gap-2">
      <button type="button" class="btn btn-secondary" @click="handleCancel">取消</button>
      <button type="submit" class="btn btn-primary" :disabled="loading">
        <span v-if="loading" class="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span>
        保存
      </button>
    </div>
  </form>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { format } from 'date-fns'

const props = defineProps({
  todo: Object,
  categories: Array,
  tags: Array,
  username: String
})

const emit = defineEmits(['submit', 'cancel'])

const loading = ref(false)
const form = ref({
  description: '',
  done: false,
  targetDate: format(new Date(), 'yyyy-MM-dd'),
  username: props.username, // 确保username字段被正确设置
  categoryId: '',
  priority: 0,
  reminderTime: '',
  notes: '',
  tagIds: []
})

// 在 script setup 部分添加验证逻辑
const validateForm = () => {
  if (!form.value.description.trim()) {
    return { valid: false, message: '描述不能为空' }
  }
  if (!form.value.targetDate) {
    return { valid: false, message: '目标日期不能为空' }
  }
  return { valid: true }
}

// 当编辑现有待办事项时，填充表单
onMounted(() => {
  // 确保设置username
  form.value.username = props.username
  
  if (props.todo) {
    form.value = {
      description: props.todo.description,
      done: props.todo.done,
      targetDate: props.todo.targetDate,
      username: props.username, // 确保username被设置
      categoryId: props.todo.categoryId || '',
      priority: props.todo.priority || 0,
      reminderTime: props.todo.reminderTime ? formatDateTimeForInput(props.todo.reminderTime) : '',
      notes: props.todo.notes || '',
      tagIds: props.todo.tags ? props.todo.tags.map(tag => tag.id) : []
    }
  }
})

// 监听username属性变化
watch(() => props.username, (newUsername) => {
  if (newUsername) {
    form.value.username = newUsername
  }
}, { immediate: true })

// 监听todo属性变化
watch(() => props.todo, (newTodo) => {
  if (newTodo) {
    form.value = {
      description: newTodo.description,
      done: newTodo.done,
      targetDate: newTodo.targetDate,
      username: props.username, // 确保username被设置
      categoryId: newTodo.categoryId || '',
      priority: newTodo.priority || 0,
      reminderTime: newTodo.reminderTime ? formatDateTimeForInput(newTodo.reminderTime) : '',
      notes: newTodo.notes || '',
      tagIds: newTodo.tags ? newTodo.tags.map(tag => tag.id) : []
    }
  }
}, { deep: true })

const handleSubmit = async () => {
  // 验证表单
  const validation = validateForm()
  if (!validation.valid) {
    alert(validation.message)
    return
  }
  
  loading.value = true
  try {
    // 格式化数据以符合后端API要求
    const todoData = {
      description: form.value.description,
      done: form.value.done,
      targetDate: form.value.targetDate,
      username: props.username,
      categoryId: form.value.categoryId || null,
      priority: form.value.priority,
      reminderTime: form.value.reminderTime || null,
      notes: form.value.notes,
      tagIds: form.value.tagIds
    }
    
    emit('submit', todoData)
    
    // 如果不是编辑模式，则重置表单
    if (!props.todo) {
      resetForm()
    }
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  form.value = {
    description: '',
    done: false,
    targetDate: format(new Date(), 'yyyy-MM-dd'),
    username: props.username,
    categoryId: '',
    priority: 0,
    reminderTime: '',
    notes: '',
    tagIds: []
  }
}

const handleCancel = () => {
  if (!props.todo) {
    resetForm()
  }
  emit('cancel')
}

// 将日期时间格式化为HTML datetime-local输入所需的格式
function formatDateTimeForInput(dateTimeStr) {
  const date = new Date(dateTimeStr)
  return format(date, "yyyy-MM-dd'T'HH:mm")
}
</script>
