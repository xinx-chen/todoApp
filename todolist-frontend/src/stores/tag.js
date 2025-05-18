import { defineStore } from 'pinia'
import { ref } from 'vue'
import tagService from '../services/tagService'

export const useTagStore = defineStore('tag', () => {
  const tags = ref([])
  const loading = ref(false)
  const error = ref(null)
  
  async function fetchTags(username) {
    try {
      loading.value = true
      error.value = null
      const response = await tagService.getAllTags(username)
      tags.value = response.data
    } catch (err) {
      error.value = '获取标签失败'
      console.error(err)
    } finally {
      loading.value = false
    }
  }
  
  async function createTag(tagData) {
    try {
      loading.value = true
      error.value = null
      const response = await tagService.createTag(tagData)
      tags.value.push(response.data)
      return response.data
    } catch (err) {
      error.value = '创建标签失败'
      console.error(err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  async function updateTag(id, tagData) {
    try {
      loading.value = true
      error.value = null
      const response = await tagService.updateTag(id, tagData)
      const index = tags.value.findIndex(t => t.id === id)
      if (index !== -1) {
        tags.value[index] = response.data
      }
      return response.data
    } catch (err) {
      error.value = '更新标签失败'
      console.error(err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  async function deleteTag(id) {
    try {
      loading.value = true
      error.value = null
      await tagService.deleteTag(id)
      tags.value = tags.value.filter(t => t.id !== id)
    } catch (err) {
      error.value = '删除标签失败'
      console.error(err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  return {
    tags,
    loading,
    error,
    fetchTags,
    createTag,
    updateTag,
    deleteTag
  }
})