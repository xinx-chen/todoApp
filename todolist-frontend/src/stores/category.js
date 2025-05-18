import { defineStore } from 'pinia'
import { ref } from 'vue'
import categoryService from '../services/categoryService'

export const useCategoryStore = defineStore('category', () => {
  const categories = ref([])
  const loading = ref(false)
  const error = ref(null)
  
  async function fetchCategories(username) {
    try {
      loading.value = true
      error.value = null
      const response = await categoryService.getAllCategories(username)
      categories.value = response.data
    } catch (err) {
      error.value = '获取分类失败'
      console.error(err)
    } finally {
      loading.value = false
    }
  }
  
  async function createCategory(categoryData) {
    try {
      loading.value = true
      error.value = null
      const response = await categoryService.createCategory(categoryData)
      categories.value.push(response.data)
      return response.data
    } catch (err) {
      error.value = '创建分类失败'
      console.error(err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  async function updateCategory(id, categoryData) {
    try {
      loading.value = true
      error.value = null
      const response = await categoryService.updateCategory(id, categoryData)
      const index = categories.value.findIndex(c => c.id === id)
      if (index !== -1) {
        categories.value[index] = response.data
      }
      return response.data
    } catch (err) {
      error.value = '更新分类失败'
      console.error(err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  async function deleteCategory(id) {
    try {
      loading.value = true
      error.value = null
      await categoryService.deleteCategory(id)
      categories.value = categories.value.filter(c => c.id !== id)
    } catch (err) {
      error.value = '删除分类失败'
      console.error(err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  async function batchDeleteCategories(ids) {
    try {
      loading.value = true
      error.value = null
      await categoryService.batchDeleteCategories(ids)
      categories.value = categories.value.filter(c => !ids.includes(c.id))
    } catch (err) {
      error.value = '批量删除分类失败'
      console.error(err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  return {
    categories,
    loading,
    error,
    fetchCategories,
    createCategory,
    updateCategory,
    deleteCategory,
    batchDeleteCategories
  }
})