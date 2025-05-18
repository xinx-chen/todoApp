import api from './api'

const tagService = {
  getAllTags(username) {
    return api.get(`/tags/users/${username}`)
  },
  
  getTagById(id) {
    return api.get(`/tags/${id}`)
  },
  
  createTag(tagData) {
    return api.post('/tags', tagData)
  },
  
  updateTag(id, tagData) {
    return api.put(`/tags/${id}`, tagData)
  },
  
  deleteTag(id) {
    return api.delete(`/tags/${id}`)
  }
}

export default tagService