<template>
  <MainLayout>
    <div class="tags-view">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="page-title"><i class="bi bi-tags me-2"></i>标签管理</h2>
        <button class="btn btn-primary btn-add" @click="showAddModal = true">
          <i class="bi bi-plus-lg me-1"></i>添加标签
        </button>
      </div>

      <div class="card main-card">
        <div class="card-body">
          <div v-if="tagStore.loading" class="text-center py-5">
            <div class="spinner-grow text-primary mb-2" role="status">
              <span class="visually-hidden">加载中...</span>
            </div>
            <p class="text-muted">正在加载标签数据...</p>
          </div>
       
          <div v-else-if="tagStore.error" class="alert alert-danger">
            <i class="bi bi-exclamation-triangle-fill me-2"></i>
            {{ tagStore.error }}
          </div>
          
          <div v-else-if="tagStore.tags.length === 0" class="empty-state">
            <div class="empty-icon">
              <i class="bi bi-tags"></i>
            </div>
            <h4>没有标签</h4>
            <p class="text-muted">点击"添加标签"按钮创建您的第一个标签</p>
          </div>
          
          <div v-else>
            <div class="row tag-grid">
              <div class="col-md-4 mb-3" v-for="tag in tagStore.tags" :key="tag.id">
                <div class="tag-card">
                  <div class="tag-header" :style="{ backgroundColor: tag.color }">
                    <span class="tag-name" :style="{ color: getContrastColor(tag.color) }">
                      {{ tag.name }}
                    </span>
                  </div>
                  <div class="tag-body">
                    <div class="tag-color-info">
                      <div class="color-sample" :style="{ backgroundColor: tag.color }"></div>
                      <span class="color-code">{{ tag.color }}</span>
                    </div>
                    <div class="tag-actions">
                      <button class="btn btn-sm btn-light action-btn" @click="editTag(tag)" title="编辑">
                        <i class="bi bi-pencil"></i>
                      </button>
                      <button class="btn btn-sm btn-light action-btn ms-1" @click="confirmDelete(tag)" title="删除">
                        <i class="bi bi-trash"></i>
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 添加/编辑标签模态框 -->
      <div class="modal fade-in" tabindex="-1" :class="{ 'd-block': showAddModal || editingTag }">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">
                <i class="bi" :class="editingTag ? 'bi-pencil-square' : 'bi-tag-plus'"></i>
                {{ editingTag ? '编辑标签' : '添加标签' }}
              </h5>
              <button type="button" class="btn-close" @click="closeModal"></button>
            </div>
            <div class="modal-body">
              <form @submit.prevent="saveTag">
                <div class="mb-4">
                  <label for="tagName" class="form-label">标签名称</label>
                  <div class="input-group">
                    <span class="input-group-text bg-light">
                      <i class="bi bi-tag"></i>
                    </span>
                    <input
                      type="text"
                      class="form-control"
                      id="tagName"
                      v-model="form.name"
                      required
                      placeholder="请输入标签名称"
                    />
                  </div>
                </div>
                
                <div class="mb-4">
                  <label for="tagColor" class="form-label">标签颜色</label>
                  <div class="color-preview-container mb-3">
                    <div class="tag-preview" :style="{ backgroundColor: form.color }">
                      <span class="tag-preview-text" :style="{ color: getContrastColor(form.color) }">
                        {{ form.name || '预览' }}
                      </span>
                    </div>
                  </div>
                  <div class="input-group">
                    <input
                      type="color"
                      class="form-control form-control-color"
                      id="tagColor"
                      v-model="form.color"
                    />
                    <input
                      type="text"
                      class="form-control"
                      v-model="form.color"
                      pattern="^#[0-9A-Fa-f]{6}$"
                      placeholder="#RRGGBB"
                    />
                  </div>
                  <div class="color-presets mt-2">
                    <button 
                      type="button" 
                      v-for="color in colorPresets" 
                      :key="color"
                      class="color-preset-btn"
                      :style="{ backgroundColor: color }"
                      @click="form.color = color"
                    ></button>
                  </div>
                </div>
                
                <div class="d-flex justify-content-end gap-2">
                  <button type="button" class="btn btn-light" @click="closeModal">
                    <i class="bi bi-x-lg me-1"></i> 取消
                  </button>
                  <button type="submit" class="btn btn-primary" :disabled="tagStore.loading">
                    <span v-if="tagStore.loading" class="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span>
                    <i v-else class="bi" :class="editingTag ? 'bi-check-lg' : 'bi-plus-lg'"></i>
                    {{ editingTag ? '更新' : '创建' }}
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-backdrop fade show" v-if="showAddModal || editingTag"></div>

      <!-- 删除确认模态框 -->
      <div class="modal fade-in" tabindex="-1" :class="{ 'd-block': showDeleteModal }">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header bg-danger text-white">
              <h5 class="modal-title">
                <i class="bi bi-exclamation-triangle me-2"></i>
                确认删除
              </h5>
              <button type="button" class="btn-close btn-close-white" @click="showDeleteModal = false"></button>
            </div>
            <div class="modal-body">
              <div class="text-center mb-3">
                <div class="delete-icon">
                  <i class="bi bi-trash"></i>
                </div>
              </div>
              <p class="lead">确定要删除这个标签吗？</p>
              <div class="alert alert-warning">
                <i class="bi bi-exclamation-circle me-2"></i>
                这可能会影响使用此标签的待办事项。
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-light" @click="showDeleteModal = false">
                <i class="bi bi-x-lg me-1"></i> 取消
              </button>
              <button type="button" class="btn btn-danger" @click="deleteTag">
                <i class="bi bi-trash me-1"></i> 删除
              </button>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-backdrop fade show" v-if="showDeleteModal"></div>
    </div>
  </MainLayout>  
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useTagStore } from '../stores/tag'
import { useAuthStore } from '../stores/auth'
import MainLayout from '../components/layout/MainLayout.vue'

const tagStore = useTagStore()
const authStore = useAuthStore()

const showAddModal = ref(false)
const showDeleteModal = ref(false)
const editingTag = ref(null)
const tagToDelete = ref(null)

const colorPresets = [
  '#95a5a6', '#3498db', '#2ecc71', '#e74c3c', '#f39c12',
  '#9b59b6', '#1abc9c', '#d35400', '#34495e', '#16a085'
]

const form = ref({
  name: '',
  color: '#95a5a6',
  username: ''
})

onMounted(async () => {
  if (authStore.user) {
    await tagStore.fetchTags(authStore.user.username)
    form.value.username = authStore.user.username
  }
})

// 获取对比色以确保文本可读性
const getContrastColor = (hexColor) => {
  // 移除#号
  const hex = hexColor.replace('#', '')
  
  // 转换为RGB
  const r = parseInt(hex.substring(0, 2), 16)
  const g = parseInt(hex.substring(2, 4), 16)
  const b = parseInt(hex.substring(4, 6), 16)
  
  // 计算亮度
  const brightness = (r * 299 + g * 587 + b * 114) / 1000
  
  // 亮度大于125返回黑色，否则返回白色
  return brightness > 125 ? '#000000' : '#ffffff'
}

const editTag = (tag) => {
  editingTag.value = tag
  form.value = {
    name: tag.name,
    color: tag.color,
    username: tag.username
  }
}

const confirmDelete = (tag) => {
  tagToDelete.value = tag
  showDeleteModal.value = true
}

const deleteTag = async () => {
  if (tagToDelete.value) {
    await tagStore.deleteTag(tagToDelete.value.id)
    showDeleteModal.value = false
    tagToDelete.value = null
  }
}

const saveTag = async () => {
  try {
    if (editingTag.value) {
      await tagStore.updateTag(editingTag.value.id, form.value)
    } else {
      await tagStore.createTag(form.value)
    }
    closeModal()
  } catch (error) {
    console.error('保存标签失败', error)
  }
}

const closeModal = () => {
  showAddModal.value = false
  editingTag.value = null
  form.value = {
    name: '',
    color: '#95a5a6',
    username: authStore.user?.username || ''
  }
}
</script>

<style scoped>
.page-title {
  font-weight: 600;
  color: #2c3e50;
}

.btn-add {
  border-radius: 8px;
  padding: 0.5rem 1.25rem;
  font-weight: 500;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.btn-add:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.main-card {
  border: none;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.tag-grid {
  padding: 0.5rem;
}

.tag-card {
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  height: 100%;
}

.tag-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
}

.tag-header {
  padding: 1rem;
  text-align: center;
}

.tag-name {
  font-weight: 600;
  font-size: 1.1rem;
}

.tag-body {
  padding: 1rem;
  background-color: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.tag-color-info {
  display: flex;
  align-items: center;
}

.color-sample {
  width: 20px;
  height: 20px;
  border-radius: 4px;
  margin-right: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.color-code {
  font-family: monospace;
  color: #6c757d;
  font-size: 0.9rem;
}

.tag-actions {
  opacity: 0.7;
  transition: opacity 0.2s ease;
}

.tag-card:hover .tag-actions {
  opacity: 1;
}

.action-btn {
  border-radius: 6px;
  width: 32px;
  height: 32px;
  padding: 0;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
}

.action-btn:hover {
  transform: translateY(-2px);
}

.empty-state {
  text-align: center;
  padding: 3rem 1rem;
}

.empty-icon {
  font-size: 3rem;
  color: #dee2e6;
  margin-bottom: 1rem;
}

.fade-in {
  animation: fadeIn 0.3s ease;
}

.color-preview-container {
  display: flex;
  justify-content: center;
}

.tag-preview {
  width: 100%;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.tag-preview-text {
  font-size: 1.25rem;
  font-weight: 500;
}

.color-presets {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.color-preset-btn {
  width: 24px;
  height: 24px;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  transition: transform 0.2s;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.color-preset-btn:hover {
  transform: scale(1.2);
}

.delete-icon {
  font-size: 3rem;
  color: #e74c3c;
  margin-bottom: 1rem;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-20px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
