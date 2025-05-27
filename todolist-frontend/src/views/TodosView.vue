<template>
  <MainLayout>
    <div class="todos-view">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>待办事项</h1>
        <button class="btn btn-primary" @click="showAddModal = true">
          <i class="bi bi-plus-lg me-1"></i> 创建待办事项
        </button>
      </div>
      
      <TodoFilter 
        :categories="categoryStore.categories" 
        :tags="tagStore.tags"
        @filter-change="handleFilterChange" 
      />
      
      <div v-if="todoStore.loading" class="text-center py-5">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">加载中...</span>
        </div>
      </div>
      
      <div v-else-if="todoStore.error" class="alert alert-danger">
        {{ todoStore.error }}
      </div>
      
      <div v-else-if="todoStore.todos.length === 0" class="text-center py-5">
        <div class="empty-state">
          <i class="bi bi-clipboard-check display-1 text-muted"></i>
          <h4 class="mt-3">没有待办事项</h4>
          <p class="text-muted">点击"创建待办事项"按钮开始添加</p>
          <button class="btn btn-primary mt-2" @click="showAddModal = true">
            <i class="bi bi-plus-lg me-1"></i> 创建待办事项
          </button>
        </div>
      </div>
      
      <div v-else>
        <TodoList 
          :todos="todoStore.todos" 
          @edit="editTodo" 
          @delete="confirmDelete" 
          @toggle-status="toggleTodoStatus"
        />
      </div>
      
      <!-- 添加/编辑待办事项模态框 -->
      <div class="modal" tabindex="-1" :class="{ 'd-block': showAddModal || editingTodo }">
        <div class="modal-dialog modal-lg">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">{{ editingTodo ? '编辑待办事项' : '创建待办事项' }}</h5>
              <button type="button" class="btn-close" @click="closeModal"></button>
            </div>
            <div class="modal-body">
              <TodoForm
                :todo="editingTodo"
                :categories="categoryStore.categories"
                :tags="tagStore.tags"
                :username="authStore.user?.username"
                @submit="saveTodo"
                @cancel="closeModal"
              />
            </div>
          </div>
        </div>
      </div>
      <div class="modal-backdrop fade show" v-if="showAddModal || editingTodo"></div>

      <!-- 删除确认模态框 -->
      <div class="modal" tabindex="-1" :class="{ 'd-block': showDeleteModal }">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">确认删除</h5>
              <button type="button" class="btn-close" @click="showDeleteModal = false"></button>
            </div>
            <div class="modal-body">
              <p>确定要删除这个待办事项吗？此操作无法撤销。</p>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" @click="showDeleteModal = false">取消</button>
              <button type="button" class="btn btn-danger" @click="deleteTodo">删除</button>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-backdrop fade show" v-if="showDeleteModal"></div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import MainLayout from '../components/layout/MainLayout.vue';
import TodoFilter from '../components/todo/TodoFilter.vue';
import TodoList from '../components/todo/TodoList.vue';
import TodoForm from '../components/todo/TodoForm.vue';
import { useTodoStore } from '../stores/todo';
import { useCategoryStore } from '../stores/category';
import { useTagStore } from '../stores/tag';
import { useAuthStore } from '../stores/auth';

const todoStore = useTodoStore();
const categoryStore = useCategoryStore();
const tagStore = useTagStore();
const authStore = useAuthStore();

const showAddModal = ref(false);
const showDeleteModal = ref(false);
const editingTodo = ref(null);
const todoToDelete = ref(null);
const activeFilters = ref({});

onMounted(async () => {
  if (authStore.user) {
    await Promise.all([
      todoStore.fetchTodos(authStore.user.username, activeFilters.value),
      categoryStore.fetchCategories(authStore.user.username),
      tagStore.fetchTags(authStore.user.username)
    ]);
  }
});

const handleFilterChange = (filters) => {
  activeFilters.value = filters;
  if (authStore.user) {
    todoStore.fetchTodos(authStore.user.username, filters);
  }
};

const editTodo = (todo) => {
  editingTodo.value = todo;
};

const confirmDelete = (todo) => {
  todoToDelete.value = todo;
  showDeleteModal.value = true;
};

const deleteTodo = async () => {
  if (todoToDelete.value) {
    await todoStore.deleteTodo(todoToDelete.value.id);
    showDeleteModal.value = false;
    todoToDelete.value = null;
  }
};

const saveTodo = async (todoData) => {
  try {
    if (editingTodo.value) {
      await todoStore.updateTodo(editingTodo.value.id, todoData)
    } else {
      await todoStore.createTodo(todoData)
    }
    closeModal()
  } catch (error) {
    console.error('保存待办事项失败', error)
  }
};

const toggleTodoStatus = async (todo) => {
  const updatedTodo = { ...todo, done: !todo.done };
  await todoStore.updateTodo(todo.id, updatedTodo);
};

const closeModal = () => {
  showAddModal.value = false;
  editingTodo.value = null;
};
</script>
<style scoped>
.todos-view {
  padding-bottom: 30px;
}

/* 操作按钮样式 */
.todo-actions .btn {
  min-width: 70px;
  padding: 8px 16px;
  font-size: 14px;
  border-radius: 6px;
}

.todo-actions .btn i {
  font-size: 14px;
}

/* 更大的按钮 */
.todo-actions .btn-lg-custom {
  min-width: 80px;
  padding: 10px 20px;
  font-size: 15px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 0;
  color: #6c757d;
}

.modal-content {
  border-radius: 12px;
  border: none;
}

.modal-header {
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  background-color: #f8f9fa;
}

.modal-footer {
  border-top: 1px solid rgba(0, 0, 0, 0.05);
}
</style>

