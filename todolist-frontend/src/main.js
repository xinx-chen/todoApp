import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'

// 导入Bootstrap
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap/dist/js/bootstrap.bundle.js'
import { BootstrapVue3 } from 'bootstrap-vue-3'
import 'bootstrap-vue-3/dist/bootstrap-vue-3.css'
import 'v-calendar/dist/style.css'  // 添加 v-calendar 样式
import VCalendar from 'v-calendar'  // 导入 v-calendar

// 创建应用
const app = createApp(App)

// 使用插件
app.use(createPinia())
app.use(router)
app.use(BootstrapVue3)
app.use(VCalendar)  // 注册 v-calendar

app.mount('#app')