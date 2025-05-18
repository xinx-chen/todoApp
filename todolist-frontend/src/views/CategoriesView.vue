<template>
  <MainLayout>
    <div class="categories-view">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>分类管理</h2>
        <button class="btn btn-primary" @click="showAddModal = true">
          添加分类
        </button>
      </div>

      <div class="card">
        <div class="card-body">
          <div v-if="categoryStore.loading" class="text-center py-3">
            <div class="spinner-border text-primary" role="status">
              <span class="visually-hidden">加载中...</span>
            </div>
          </div>
          
          <div v-else-if="categoryStore.error" class="alert alert-danger">
            {{ categoryStore.error }}
          </div>
          
          <div v-else-if="categoryStore.categories.length === 0" class="text-center py-3">
            没有分类
          </div>
          
          <div v-else>
            <table class="table table-hover">
              <thead>
                <tr>
                  <th width="50">#</th>
                  <th>名称</th>
                  <th>颜色</th>
                  <th width="150">操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(category, index) in categoryStore.categories" :key="category.id">
                  <td>{{ index + 1 }}</td>
                  <td>{{ category.name }}</td>
                  <td>
                    <span class="badge me-2" :style="{ backgroundColor: category.color }">
                      &nbsp;&nbsp;&nbsp;&nbsp;
                    </span>
                    <span class="text-muted">{{ category.color }}</span>
                  </td>
                  <td>
                    <div class="btn-group btn-group-sm">
                      <button class="btn btn-outline-primary" @click="editCategory(category)">
                        <i class="bi bi-pencil"></i> 编辑
                      </button>
                      <button class="btn btn-outline-danger" @click="confirmDelete(category)">
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
      <div class="modal" tabindex="-1" :class="{ 'd-block': showAddModal || editingCategory }">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">{{ editingCategory ? '编辑分类' : '添加分类' }}</h5>
              <button type="button" class="btn-close" @click="closeModal"></button>
            </div>
            <div class="modal-body">
              <form @submit.prevent="saveCategory">
                <div class="mb-3">
                  <label for="categoryName" class="form-label">名称</label>
                  <input
                    type="text"
                    class="form-control"
                    id="categoryName"
                    v-model="form.name"
                    required
                  />
                </div>
                
                <div class="mb-3">
                  <label for="categoryColor" class="form-label">颜色</label>
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
                </div>
                
                <div class="d-flex justify-content-end gap-2">
                  <button type="button" class="btn btn-secondary" @click="closeModal">取消</button>
                  <button type="submit" class="btn btn-primary" :disabled="categoryStore.loading">
                    <span v-if="categoryStore.loading" class="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span>
                    保存
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-backdrop fade show" v-if="showAddModal || editingCategory"></div>

      <!-- 删除确认模态框 -->
      <div class="modal" tabindex="-1" :class="{ 'd-block': showDeleteModal }">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">确认删除</h5>
              <button type="button" class="btn-close" @click="showDeleteModal = false"></button>
            </div>
            <div class="modal-body">
              <p>确定要删除这个分类吗？这可能会影响使用此分类的待办事项。</p>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" @click="showDeleteModal = false">取消</button>
              <button type="button" class="btn btn-danger" @click="deleteCategory">删除</button>
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
