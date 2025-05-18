import api from './api'

const categoryService = {
  getAllCategories(username) {
    return api.get(`/categories/users/${username}`)
  },
  
  getCategoryById(id) {
    return api.get(`/categories/${id}`)
  },
  
  createCategory(categoryData) {
    return api.post('/categories', categoryData)
  },
  
  updateCategory(id, categoryData) {
    return api.put(`/categories/${id}`, categoryData)
  },
  
  deleteCategory(id) {
    return api.delete(`/categories/${id}`)
  },
  
  batchDeleteCategories(ids) {
    return api.delete('/categories/batch', { data: ids })
  }
}

export default categoryService