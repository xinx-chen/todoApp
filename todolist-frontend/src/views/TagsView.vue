<template>
  <MainLayout>
    <div>
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>标签管理</h2>
        <button class="btn btn-primary" @click="showAddModal = true">
          添加标签
        </button>
      </div>

      <div class="card">
        <div class="card-body">
          <div v-if="tagStore.loading" class="text-center py-3">
            <div class="spinner-border text-primary" role="status">
              <span class="visually-hidden">加载中...</span>
            </div>
          </div>
       
          <div v-else-if="tagStore.error" class="alert alert-danger">
            {{ tagStore.error }}
          </div>
          <div v-else-if="tagStore.tags.length === 0" class="text-center py-3">
            没有标签
          </div>
          <div v-else>
            <div class="row">
              <div class="col-md-4 mb-3" v-for="tag in tagStore.tags" :key="tag.id">
                <div class="card h-100">
                  <div class="card-body d-flex justify-content-between align-items-center">
                    <div>
                      <span class="badge me-2" :style="{ backgroundColor: tag.color }">
                        &nbsp;
                      </span>
                      <span class="fw-bold">{{ tag.name }}</span>
                    </div>
                    <div class="btn-group btn-group-sm">
                      <button class="btn btn-outline-primary" @click="editTag(tag)">
                        <i class="bi bi-pencil"></i>
                      </button>
                      <button class="btn btn-outline-danger" @click="confirmDelete(tag)">
                        <i class="bi bi-trash"></i>
                      </button>
                    </div>
                  </div>
                  <div class="card-footer text-muted">
                    <small>{{ tag.color }}</small>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 添加/编辑标签模态框 -->
      <div class="modal" tabindex="-1" :class="{ 'd-block': showAddModal || editingTag }">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">{{ editingTag ? '编辑标签' : '添加标签' }}</h5>
              <button type="button" class="btn-close" @click="closeModal"></button>
            </div>
            <div class="modal-body">
              <form @submit.prevent="saveTag">
                <div class="mb-3">
                  <label for="tagName" class="form-label">名称</label>
                  <input
                    type="text"
                    class="form-control"
                    id="tagName"
                    v-model="form.name"
                    required
                  />
                </div>
                
                <div class="mb-3">
                  <label for="tagColor" class="form-label">颜色</label>
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
                </div>
                
                <div class="d-flex justify-content-end gap-2">
                  <button type="button" class="btn btn-secondary" @click="closeModal">取消</button>
                  <button type="submit" class="btn btn-primary" :disabled="tagStore.loading">
                    <span v-if="tagStore.loading" class="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span>
                    保存
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-backdrop fade show" v-if="showAddModal || editingTag"></div>

      <!-- 删除确认模态框 -->
      <div class="modal" tabindex="-1" :class="{ 'd-block': showDeleteModal }">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">确认删除</h5>
              <button type="button" class="btn-close" @click="showDeleteModal = false"></button>
            </div>
            <div class="modal-body">
              <p>确定要删除这个标签吗？这可能会影响使用此标签的待办事项。</p>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" @click="showDeleteModal = false">取消</button>
              <button type="button" class="btn btn-danger" @click="deleteTag">删除</button>
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
import MainLayout from '../components/layout/MainLayout.vue'  // 添加这一行

const tagStore = useTagStore()
const authStore = useAuthStore()

const showAddModal = ref(false)
const showDeleteModal = ref(false)
const editingTag = ref(null)
const tagToDelete = ref(null)

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
