import { defineStore } from 'pinia'
import { ref } from 'vue'
import todoService from '../services/todoService'

export const useTodoStore = defineStore('todo', () => {
  const todos = ref([])
  const currentTodo = ref(null)
  const statistics = ref(null)
  const reminders = ref([])
  const loading = ref(false)
  const error = ref(null)

  async function fetchTodos(username, filters = {}) {
    try {
      loading.value = true
      error.value = null
      const response = await todoService.getAllTodos(username, filters)
      todos.value = response.data
    } catch (err) {
      error.value = '获取待办事项失败'
      console.error(err)
    } finally {
      loading.value = false
    }
  }

  async function fetchTodoById(id) {
    try {
      loading.value = true
      error.value = null
      const response = await todoService.getTodoById(id)
      currentTodo.value = response.data
    } catch (err) {
      error.value = '获取待办事项详情失败'
      console.error(err)
    } finally {
      loading.value = false
    }
  }

  async function createTodo(todoData) {
    try {
      loading.value = true
      error.value = null
      const response = await todoService.createTodo(todoData)
      todos.value.push(response.data)
      return response.data
    } catch (err) {
      error.value = '创建待办事项失败'
      console.error(err)
      throw err
    } finally {
      loading.value = false
    }
  }

  async function updateTodo(id, todoData) {
    try {
      loading.value = true
      error.value = null
      const response = await todoService.updateTodo(id, todoData)
      const index = todos.value.findIndex(t => t.id === id)
      if (index !== -1) {
        todos.value[index] = response.data
      }
      return response.data
    } catch (err) {
      error.value = '更新待办事项失败'
      console.error(err)
      throw err
    } finally {
      loading.value = false
    }
  }

  async function deleteTodo(id) {
    try {
      loading.value = true
      error.value = null
      await todoService.deleteTodo(id)
      todos.value = todos.value.filter(t => t.id !== id)
    } catch (err) {
      error.value = '删除待办事项失败'
      console.error(err)
    } finally {
      loading.value = false
    }
  }

  async function fetchStatistics(username) {
    try {
      loading.value = true
      const response = await todoService.getStatistics(username)
      statistics.value = response.data
    } catch (err) {
      console.error('获取统计信息失败', err)
    } finally {
      loading.value = false
    }
  }

  async function fetchReminders(username) {
    try {
      loading.value = true
      const response = await todoService.getReminders(username)
      reminders.value = response.data
    } catch (err) {
      console.error('获取提醒失败', err)
    } finally {
      loading.value = false
    }
  }

  return {
    todos,
    currentTodo,
    statistics,
    reminders,
    loading,
    error,
    fetchTodos,
    fetchTodoById,
    createTodo,
    updateTodo,
    deleteTodo,
    fetchStatistics,
    fetchReminders
  }
})