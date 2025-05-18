import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    port: 8081, // 与后端CORS配置匹配
    proxy: {
      // 如果需要在开发环境中代理API请求
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})