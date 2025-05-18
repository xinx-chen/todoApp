<template>
  <div class="todo-list">
    <table class="table table-hover">
      <thead>
        <tr>
          <th>状态</th>
          <th>描述</th>
          <th>目标日期</th>
          <th>分类</th>
          <th>优先级</th>
          <th>标签</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="todo in todos" :key="todo.id" :class="{ 'table-success': todo.done }">
          <td>
            <div class="form-check">
              <input
                class="form-check-input"
                type="checkbox"
                :checked="todo.done"
                @change="$emit('toggle-status', todo)"
              />
            </div>
          </td>
          <td>{{ todo.description }}</td>
          <td>{{ todo.targetDate }}</td>
          <td>
            <span
              v-if="todo.category"
              class="badge"
              :style="{ backgroundColor: todo.category.color }"
            >
              {{ todo.category.name }}
            </span>
          </td>
          <td>
            <span class="badge" :class="priorityClass(todo.priority)">
              {{ priorityText(todo.priority) }}
            </span>
          </td>
          <td>
            <div class="d-flex flex-wrap gap-1">
              <span
                v-for="tag in todo.tags"
                :key="tag.id"
                class="badge"
                :style="{ backgroundColor: tag.color }"
              >
                {{ tag.name }}
              </span>
            </div>
          </td>
          <td>
            <div class="btn-group btn-group-sm">
              <button class="btn btn-outline-primary" @click="$emit('edit', todo)">
                <i class="bi bi-pencil"></i>
              </button>
              <button class="btn btn-outline-danger" @click="$emit('delete', todo)">
                <i class="bi bi-trash"></i>
              </button>
            </div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
defineProps({
  todos: Array
})

defineEmits(['edit', 'delete', 'toggle-status'])

function priorityClass(priority) {
  switch (Number(priority)) {
    case 0: return 'bg-secondary'
    case 1: return 'bg-info'
    case 2: return 'bg-warning'
    case 3: return 'bg-danger'
    default: return 'bg-secondary'
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