
import api from './api'

const todoService = {
  getAllTodos(username, filters = {}) {
    let url = `/todos/users/${username}`
    const params = new URLSearchParams()
    
    if (filters.done !== undefined) params.append('done', filters.done)
    if (filters.categoryId) params.append('categoryId', filters.categoryId)
    if (filters.priority) params.append('priority', filters.priority)
    if (filters.tagId) params.append('tagId', filters.tagId)
    if (filters.search) params.append('search', filters.search)
    
    if (params.toString()) {
      url += `?${params.toString()}`
    }
    
    return api.get(url)
  },
  
  getTodoById(id) {
    return api.get(`/todos/${id}`)
  },
  
  createTodo(todoData) {
    return api.post('/todos', todoData)
  },
  
  updateTodo(id, todoData) {
    return api.put(`/todos/${id}`, todoData)
  },
  
  deleteTodo(id) {
    return api.delete(`/todos/${id}`)
  },
  
  getStatistics(username) {
    return api.get(`/todos/users/${username}/statistics`)
  },
  
  getReminders(username) {
    return api.get(`/todos/users/${username}/reminders`)
  }
}

export default todoService