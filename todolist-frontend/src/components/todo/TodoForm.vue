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
    
    <!-- 在目标日期输入框下方添加提示 -->
    <div class="mb-3">
      <label for="targetDate" class="form-label">目标日期</label>
      <input
        type="date"
        class="form-control"
        id="targetDate"
        v-model="form.targetDate"
        :min="format(new Date(), 'yyyy-MM-dd')" 
        required
      />
      <div v-if="form.targetDate && new Date(form.targetDate) < new Date().setHours(0,0,0,0)" 
          class="text-danger small mt-1">
        目标日期不能早于今天
      </div>
    </div>

    <!-- 在提醒时间输入框下方添加提示 -->
    <div class="mb-3">
      <label for="reminderTime" class="form-label">提醒时间</label>
      <input
        type="datetime-local"
        class="form-control"
        id="reminderTime"
        v-model="form.reminderTime"
        :min="new Date().toISOString().slice(0, 16)" 
      />
      <div v-if="form.reminderTime && new Date(form.reminderTime) > new Date(form.targetDate + 'T23:59:59')" 
          class="text-danger small mt-1">
        提醒时间不能晚于目标日期
      </div>
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

    <!-- 图片上传区域 -->
    <div class="mb-3">
      <label class="form-label">图片</label>
      <div class="d-flex align-items-center gap-2 mb-2">
        <input
          type="file"
          class="form-control"
          id="image"
          accept="image/*"
          ref="fileInput"
          @change="handleImageSelect"
          style="flex: 1;"
        />
      </div>
      
      <!-- 图片预览 -->
      <div v-if="form.imagePreview || form.imageUrl" class="mt-2">
        <!-- 优先显示新上传的预览 -->
        <img 
          v-if="form.imagePreview" 
          :src="form.imagePreview" 
          class="img-thumbnail" 
          style="max-height: 200px;" 
        />
        <!-- 如果没有新预览，但有原图URL -->
        <img 
          v-else-if="form.imageUrl" 
          :src="form.imageUrl" 
          class="img-thumbnail" 
          style="max-height: 200px;" 
        />
        
        <button 
          type="button" 
          class="btn btn-sm btn-danger ms-2"
          @click="removeImage"
        >
          移除图片
        </button>
      </div>
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
const fileInput = ref(null)

const form = ref({
  description: '',
  done: false,
  targetDate: format(new Date(), 'yyyy-MM-dd'),
  username: props.username,
  categoryId: '',
  priority: 0,
  reminderTime: '',
  notes: '',
  tagIds: [],
  imageUrl: null,
  imageRemoved: false
})

//验证逻辑
const validateForm = () => {
  // 基础验证
  if (!form.value.description.trim()) {
    return { valid: false, message: '描述不能为空' }
  }
  
  // 目标日期验证
  const today = new Date()
  today.setHours(0, 0, 0, 0) // 清除时间部分
  
  const targetDate = new Date(form.value.targetDate)
  targetDate.setHours(0, 0, 0, 0)
  
  // 目标日期不能早于今天
  if (targetDate < today) {
    return { valid: false, message: '目标日期不能早于今天' }
  }
  
  // 如果设置了提醒时间
  if (form.value.reminderTime) {
    const reminderDate = new Date(form.value.reminderTime)
    const maxReminderDate = new Date(targetDate)
    maxReminderDate.setHours(23, 59, 59, 999) // 目标日期的最后一刻
    
    // 提醒时间不能晚于目标日期
    if (reminderDate > maxReminderDate) {
      return { valid: false, message: '提醒时间不能晚于目标日期' }
    }
    
    // 提醒时间不能早于当前时间（可选）
    const now = new Date()
    if (reminderDate < now) {
      return { valid: false, message: '提醒时间不能早于当前时间' }
    }
  }
  
  return { valid: true }
}
// 处理图片选择
// 图片处理相关代码
const handleImageSelect = (event) => {
  const file = event.target.files[0]
  if (!file) return

  // 验证逻辑...
  
  const reader = new FileReader()
  reader.onload = (e) => {
    // 设置新图片预览和文件对象
    form.value.imagePreview = e.target.result
    form.value.imageFile = file
    // 清除原有的图片URL（如果有）
    form.value.imageUrl = null
  }
  reader.readAsDataURL(file)
}

const removeImage = () => {
  // 清除所有图片相关状态
  form.value.imageFile = null
  form.value.imagePreview = null
  form.value.imageUrl = null // 关键点：显式设置为null
  form.value.imageRemoved = true
  
  // 重置文件输入
  if (fileInput.value) {
    fileInput.value.value = ''
  }
}

// 当编辑现有待办事项时，填充表单
onMounted(() => {
  form.value.username = props.username
  if (props.todo) {
    form.value = {
      ...form.value,
      description: props.todo.description,
      done: props.todo.done,
      targetDate: props.todo.targetDate,
      categoryId: props.todo.categoryId || '',
      priority: props.todo.priority || 0,
      reminderTime: props.todo.reminderTime ? formatDateTimeForInput(props.todo.reminderTime) : '',
      notes: props.todo.notes || '',
      tagIds: props.todo.tags ? props.todo.tags.map(tag => tag.id) : [],
      // 保留原有图片URL
      imageUrl: props.todo.imageUrl || null,
      // 重置上传状态
      imagePreview: null,
      imageFile: null,
      imageRemoved: false
    }
  }
})
// 监听todo属性变化
watch(() => props.todo, (newTodo) => {
  if (newTodo) {
    form.value = {
      ...form.value,
      description: newTodo.description,
      done: newTodo.done,
      targetDate: newTodo.targetDate,
      categoryId: newTodo.categoryId || '',
      priority: newTodo.priority || 0,
      reminderTime: newTodo.reminderTime ? formatDateTimeForInput(newTodo.reminderTime) : '',
      notes: newTodo.notes || '',
      tagIds: newTodo.tags ? newTodo.tags.map(tag => tag.id) : [],
      // 保留原有图片URL
      imageUrl: newTodo.imageUrl || null,
      // 重置上传状态
      imagePreview: null,
      imageFile: null
    }
  }
}, { deep: true })

// 监听username属性变化
watch(() => props.username, (newUsername) => {
  if (newUsername) {
    form.value.username = newUsername
  }
}, { immediate: true })

const handleSubmit = async () => {
  const validation = validateForm()
  if (!validation.valid) {
    showToast(validation.message, 'error')
    return
  }
  
  loading.value = true
  try {
    const formData = new FormData()

    const imageUrl = form.value.imageRemoved 
      ? null // 情况1：用户主动移除图片
      : (form.value.imageFile 
        ? null // 情况2：上传了新图片（原图应被覆盖）
        : form.value.imageUrl) // 情况3：保持原图不变
    
    // 构造要提交的JSON数据
    const todoData = {
      description: form.value.description,
      done: form.value.done,
      targetDate: form.value.targetDate,
      username: form.value.username,
      categoryId: form.value.categoryId,
      priority: form.value.priority,
      reminderTime: form.value.reminderTime || null,
      notes: form.value.notes || '',
      tagIds: form.value.tagIds,
      // 明确标记是否要删除图片
      imageUrl: imageUrl
    }

    // 添加JSON部分
    formData.append('todoRequest', new Blob([JSON.stringify(todoData)], {
      type: 'application/json'
    }))

    // 如果有新上传的图片
    if (form.value.imageFile) {
      formData.append('image', form.value.imageFile)
    }

    emit('submit', formData)
    
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
    tagIds: [],
    imageUrl: null,
    imagePreview: null,
    imageFile: null
  }
  if (fileInput.value) {
    fileInput.value.value = ''
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
