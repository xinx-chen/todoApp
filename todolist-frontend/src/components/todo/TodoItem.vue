<template>
  <div class="todo-item" :class="{ 'todo-done': todo.done }">
    <div class="todo-content">
      <div class="form-check">
        <input
          class="form-check-input"
          type="checkbox"
          :checked="todo.done"
          @change="$emit('toggle-status', todo)"
        />
        <label class="form-check-label" :class="{ 'text-decoration-line-through': todo.done }">
          {{ todo.description }}
        </label>
      </div>
      
      <div class="todo-details">
        <span class="todo-date">
          <i class="bi bi-calendar me-1"></i> {{ todo.targetDate }}
        </span>
        
        <span
          v-if="todo.category"
          class="todo-category"
          :style="{ backgroundColor: todo.category.color }"
        >
          {{ todo.category.name }}
        </span>
        
        <span class="todo-priority" :class="priorityClass(todo.priority)">
          {{ priorityText(todo.priority) }}
        </span>
        
        <div class="todo-tags">
          <span
            v-for="tag in todo.tags"
            :key="tag.id"
            class="todo-tag"
            :style="{ backgroundColor: tag.color }"
          >
            {{ tag.name }}
          </span>
        </div>
      </div>
    </div>
    
    <div class="todo-actions">
      <button class="btn btn-sm btn-outline-primary" @click="$emit('edit', todo)">
        <i class="bi bi-pencil"></i>
      </button>
      <button class="btn btn-sm btn-outline-danger" @click="$emit('delete', todo)">
        <i class="bi bi-trash"></i>
      </button>
    </div>
  </div>
</template>

<script setup>
defineProps({
  todo: Object
})

defineEmits(['edit', 'delete', 'toggle-status'])

function priorityClass(priority) {
  switch (Number(priority)) {
    case 0: return 'priority-low'
    case 1: return 'priority-medium'
    case 2: return 'priority-high'
    case 3: return 'priority-urgent'
    default: return 'priority-low'
  }
}

function priorityText(priority) {
  switch (Number(priority)) {
    case 0: return '低'
    case 1: return '中'
    case 2: return '高'
    case 3: return '紧急'
    default: return '未设置'
  }
}
</script>

<style scoped>
.todo-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  border-radius: 10px;
  background-color: white;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
  margin-bottom: 10px;
  transition: all 0.3s ease;
}

.todo-item:hover {
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.todo-done {
  opacity: 0.7;
  background-color: #f8f9fa;
}

.todo-content {
  flex: 1;
}

.form-check-label {
  font-weight: 500;
  font-size: 1.1rem;
}

.todo-details {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 8px;
  align-items: center;
}

.todo-date {
  color: #6c757d;
  font-size: 0.85rem;
  display: flex;
  align-items: center;
}

.todo-category {
  font-size: 0.8rem;
  padding: 2px 8px;
  border-radius: 12px;
  color: white;
  font-weight: 500;
}

.todo-priority {
  font-size: 0.8rem;
  padding: 2px 8px;
  border-radius: 12px;
  color: white;
  font-weight: 500;
}

.priority-low {
  background-color: #6c757d;
}

.priority-medium {
  background-color: #17a2b8;
}

.priority-high {
  background-color: #ffc107;
  color: #212529;
}

.priority-urgent {
  background-color: #dc3545;
}

.todo-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.todo-tag {
  font-size: 0.75rem;
  padding: 1px 6px;
  border-radius: 10px;
  color: white;
}

.todo-actions {
  display: flex;
  gap: 5px;
}

.todo-actions .btn {
  width: 32px;
  height: 32px;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 6px;
}
</style>
