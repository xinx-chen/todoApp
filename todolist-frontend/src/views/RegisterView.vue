<template>
  <div class="auth-page">
    <div class="auth-container">
      <div class="auth-card">
        <div class="auth-header">
          <h2>创建账号</h2>
          <p>填写以下信息完成注册</p>
        </div>
        
        <div v-if="authStore.error" class="auth-alert error">
          <i class="bi bi-exclamation-circle"></i> {{ authStore.error }}
        </div>
        
        <form @submit.prevent="handleRegister" class="auth-form">
          <div class="form-group">
            <label for="username">
              <i class="bi bi-person"></i> 用户名
            </label>
            <input
              type="text"
              id="username"
              v-model="username"
              placeholder="请输入用户名"
              required
            />
          </div>
          
          <div class="form-group">
            <label for="email">
              <i class="bi bi-envelope"></i> 邮箱
            </label>
            <input
              type="email"
              id="email"
              v-model="email"
              placeholder="请输入邮箱"
              required
            />
          </div>
          
          <div class="form-group">
            <label for="password">
              <i class="bi bi-lock"></i> 密码
            </label>
            <div class="password-input">
              <input
                :type="showPassword ? 'text' : 'password'"
                id="password"
                v-model="password"
                placeholder="请输入密码"
                required
              />
              <button type="button" class="toggle-password" @click="showPassword = !showPassword">
                <i :class="showPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
              </button>
            </div>
          </div>
          
          <div class="form-group">
            <label for="confirmPassword">
              <i class="bi bi-shield-lock"></i> 确认密码
            </label>
            <div class="password-input">
              <input
                :type="showConfirmPassword ? 'text' : 'password'"
                id="confirmPassword"
                v-model="confirmPassword"
                placeholder="请再次输入密码"
                required
              />
              <button type="button" class="toggle-password" @click="showConfirmPassword = !showConfirmPassword">
                <i :class="showConfirmPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
              </button>
            </div>
          </div>
          
          <div v-if="passwordError" class="auth-alert error">
            <i class="bi bi-exclamation-circle"></i> {{ passwordError }}
          </div>
          
          <button
            type="submit"
            class="auth-button"
            :disabled="authStore.loading || !isFormValid"
          >
            <span v-if="authStore.loading" class="spinner"></span>
            <span v-else>注册</span>
          </button>
        </form>
        
        <div class="auth-footer">
          <p>已有账号？ <router-link to="/login">立即登录</router-link></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth';

const router = useRouter();
const authStore = useAuthStore();

const username = ref('');
const email = ref('');
const password = ref('');
const confirmPassword = ref('');
const passwordError = ref('');
const showPassword = ref(false);
const showConfirmPassword = ref(false);

const isFormValid = computed(() => {
  if (password.value !== confirmPassword.value) {
    passwordError.value = '两次输入的密码不一致';
    return false;
  }
  passwordError.value = '';
  return true;
});

const handleRegister = async () => {
  if (!isFormValid.value) return;
  
  try {
    // 调用注册方法
    const success = await authStore.register({
      username: username.value,
      email: email.value,
      password: password.value
    });

    // 注册成功后跳转到登录页
    if (success) {
      router.push('/login');
    }
  } catch (error) {
    // 处理错误信息
    if (error instanceof Error) {
      
      // 使用UI框架的通知组件
      message.error(error.message);

    } else {
      // 处理非Error对象的异常
      errorMessage.value = '注册失败，请稍后再试';
    }
  }
};
</script>

<style scoped>
/* 使用与登录页面相同的样式 */
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 20px;
}

.auth-container {
  width: 100%;
  max-width: 420px;
}

.auth-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.08);
  padding: 40px;
}

.auth-header {
  text-align: center;
  margin-bottom: 30px;
}

.auth-header h2 {
  font-size: 28px;
  font-weight: 700;
  color: #333;
  margin-bottom: 10px;
}

.auth-header p {
  color: #777;
  font-size: 15px;
}

.auth-alert {
  padding: 12px 15px;
  border-radius: 8px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  font-size: 14px;
}

.auth-alert.error {
  background-color: #fff2f2;
  color: #e74c3c;
  border-left: 4px solid #e74c3c;
}

.auth-alert i {
  margin-right: 8px;
  font-size: 16px;
}

.auth-form {
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  font-weight: 600;
  color: #444;
}

.form-group input {
  width: 100%;
  padding: 12px 15px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 15px;
  transition: border 0.3s;
}

.form-group input:focus {
  border-color: #3498db;
  outline: none;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
}

.password-input {
  position: relative;
}

.toggle-password {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #777;
  cursor: pointer;
  padding: 0;
}

.auth-button {
  width: 100%;
  padding: 14px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.3s;
}

.auth-button:hover {
  background-color: #2980b9;
}

.auth-button:disabled {
  background-color: #95a5a6;
  cursor: not-allowed;
}

.spinner {
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 3px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: #fff;
  animation: spin 1s ease-in-out infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.auth-footer {
  text-align: center;
  margin-top: 25px;
  color: #777;
  font-size: 14px;
}

.auth-footer a {
  color: #3498db;
  font-weight: 600;
  text-decoration: none;
}

.auth-footer a:hover {
  text-decoration: underline;
}
</style>
