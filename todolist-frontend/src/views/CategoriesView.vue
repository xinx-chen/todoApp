<template>
  <MainLayout>
    <div class="categories-view">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="page-title"><i class="bi bi-folder2 me-2"></i>分类管理</h2>
        <button class="btn btn-primary btn-add" @click="showAddModal = true">
          <i class="bi bi-plus-lg me-1"></i>添加分类
        </button>
      </div>

      <div class="card main-card">
        <div class="card-body">
          <div v-if="categoryStore.loading" class="text-center py-5">
            <div class="spinner-grow text-primary mb-2" role="status">
              <span class="visually-hidden">加载中...</span>
            </div>
            <p class="text-muted">正在加载分类数据...</p>
          </div>
          
          <div v-else-if="categoryStore.error" class="alert alert-danger">
            <i class="bi bi-exclamation-triangle-fill me-2"></i>
            {{ categoryStore.error }}
          </div>
          
          <div v-else-if="categoryStore.categories.length === 0" class="empty-state">
            <div class="empty-icon">
              <i class="bi bi-folder-x"></i>
            </div>
            <h4>没有分类</h4>
            <p class="text-muted">点击"添加分类"按钮创建您的第一个分类</p>
          </div>
          
          <div v-else>
            <table class="table table-hover custom-table">
              <thead>
                <tr>
                  <th width="50" class="text-center">#</th>
                  <th>名称</th>
                  <th>颜色</th>
                  <th width="150" class="text-center">操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(category, index) in categoryStore.categories" :key="category.id" class="category-row">
                  <td class="text-center index-cell">{{ index + 1 }}</td>
                  <td class="name-cell">{{ category.name }}</td>
                  <td class="color-cell">
                    <div class="color-display">
                      <span class="color-badge" :style="{ backgroundColor: category.color }"></span>
                      <span class="color-code">{{ category.color }}</span>
                    </div>
                  </td>
                  <td class="text-center action-cell">
                    <div class="btn-group btn-group-sm">
                      <button class="btn btn-outline-primary action-btn" @click="editCategory(category)">
                        <i class="bi bi-pencil"></i> 编辑
                      </button>
                      <button class="btn btn-outline-danger action-btn" @click="confirmDelete(category)">
                        <i class="bi bi-trash"></i> 删除
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- 添加/编辑分类模态框 -->
      <div class="modal fade-in" tabindex="-1" :class="{ 'd-block': showAddModal || editingCategory }">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">
                <i class="bi" :class="editingCategory ? 'bi-pencil-square' : 'bi-folder-plus'"></i>
                {{ editingCategory ? '编辑分类' : '添加分类' }}
              </h5>
              <button type="button" class="btn-close" @click="closeModal"></button>
            </div>
            <div class="modal-body">
              <form @submit.prevent="saveCategory">
                <div class="mb-4">
                  <label for="categoryName" class="form-label">名称</label>
                  <div class="input-group">
                    <span class="input-group-text bg-light">
                      <i class="bi bi-tag"></i>
                    </span>
                    <input
                      type="text"
                      class="form-control"
                      id="categoryName"
                      v-model="form.name"
                      required
                      placeholder="请输入分类名称"
                    />
                  </div>
                </div>
                
                <div class="mb-4">
                  <label for="categoryColor" class="form-label">颜色</label>
                  <div class="color-preview-container mb-3">
                    <div class="color-preview" :style="{ backgroundColor: form.color }">
                      <span class="color-preview-text" :style="{ color: getContrastColor(form.color) }">
                        {{ form.name || '预览' }}
                      </span>
                    </div>
                  </div>
                  <div class="input-group">
                    <input
                      type="color"
                      class="form-control form-control-color"
                      id="categoryColor"
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
                  <button type="submit" class="btn btn-primary" :disabled="categoryStore.loading">
                    <span v-if="categoryStore.loading" class="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span>
                    <i v-else class="bi" :class="editingCategory ? 'bi-check-lg' : 'bi-plus-lg'"></i>
                    {{ editingCategory ? '更新' : '创建' }}
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-backdrop fade show" v-if="showAddModal || editingCategory"></div>

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
              <p class="lead">确定要删除这个分类吗？</p>
              <div class="alert alert-warning">
                <i class="bi bi-exclamation-circle me-2"></i>
                这可能会影响使用此分类的待办事项。
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-light" @click="showDeleteModal = false">
                <i class="bi bi-x-lg me-1"></i> 取消
              </button>
              <button type="button" class="btn btn-danger" @click="deleteCategory">
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
import { useCategoryStore } from '../stores/category'
import { useAuthStore } from '../stores/auth'
import MainLayout from '../components/layout/MainLayout.vue'

const categoryStore = useCategoryStore()
const authStore = useAuthStore()

const showAddModal = ref(false)
const showDeleteModal = ref(false)
const editingCategory = ref(null)
const categoryToDelete = ref(null)

const colorPresets = [
  '#3498db', '#2ecc71', '#e74c3c', '#f39c12', '#9b59b6',
  '#1abc9c', '#d35400', '#34495e', '#16a085', '#c0392b'
]

const form = ref({
  name: '',
  color: '#3498db',
  username: ''
})

onMounted(async () => {
  if (authStore.user) {
    await categoryStore.fetchCategories(authStore.user.username)
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

const editCategory = (category) => {
  editingCategory.value = category
  form.value = {
    name: category.name,
    color: category.color,
    username: category.username
  }
}

const confirmDelete = (category) => {
  categoryToDelete.value = category
  showDeleteModal.value = true
}

const deleteCategory = async () => {
  if (categoryToDelete.value) {
    await categoryStore.deleteCategory(categoryToDelete.value.id)
    showDeleteModal.value = false
    categoryToDelete.value = null
  }
}

const saveCategory = async () => {
  try {
    if (editingCategory.value) {
      await categoryStore.updateCategory(editingCategory.value.id, form.value)
    } else {
      await categoryStore.createCategory(form.value)
    }
    closeModal()
  } catch (error) {
    console.error('保存分类失败', error)
  }
}

const closeModal = () => {
  showAddModal.value = false
  editingCategory.value = null
  form.value = {
    name: '',
    color: '#3498db',
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

.custom-table {
  margin-bottom: 0;
}

.custom-table thead th {
  background-color: #f8f9fa;
  border-bottom: 2px solid #e9ecef;
  font-weight: 600;
  color: #495057;
  padding: 1rem;
}

.category-row {
  transition: all 0.2s ease;
}

.category-row:hover {
  background-color: #f8f9fa;
  transform: translateY(-2px);
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
}

.index-cell {
  font-weight: 600;
  color: #6c757d;
}

.name-cell {
  font-weight: 500;
}

.color-cell {
  min-width: 150px;
}

.color-display {
  display: flex;
  align-items: center;
}

.color-badge {
  display: inline-block;
  width: 24px;
  height: 24px;
  border-radius: 4px;
  margin-right: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.color-code {
  color: #6c757d;
  font-family: monospace;
  font-size: 0.9rem;
}

.action-cell {
  opacity: 0.7;
  transition: opacity 0.2s ease;
}

.category-row:hover .action-cell {
  opacity: 1;
}

.action-btn {
  border-radius: 6px;
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

.color-preview {
  width: 100%;
  height: 80px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.color-preview-text {
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
