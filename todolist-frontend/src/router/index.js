// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import TodosView from '../views/TodosView.vue'
import CategoriesView from '../views/CategoriesView.vue'
import TagsView from '../views/TagsView.vue'
import CalendarView from '../views/CalendarView.vue'  // 导入日历视图




const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView
    },
    {
      path: '/home',
      name: 'home',
      component: HomeView,
      meta: { requiresAuth: true }
    },
    {
      path: '/todos',
      name: 'todos',
      component: TodosView,
      meta: { requiresAuth: true }
    },
    {
      path: '/categories',
      name: 'categories',
      component: CategoriesView,
      meta: { requiresAuth: true }
    },
    {
      path: '/tags',
      name: 'tags',
      component: TagsView,
      meta: { requiresAuth: true }
    },
    {
      path: '/calendar',  // 添加日历路由
      name: 'calendar',
      component: CalendarView,
      meta: { requiresAuth: true }
    }
  ]
})

// 导航守卫

router.beforeEach((to, from, next) => {
  let user = null;

  try {
    const userStr = localStorage.getItem('user');

    // 检查是否为有效的用户对象
    if (userStr) {
      user = JSON.parse(userStr);
      // 验证用户对象是否有效
      if (!user || typeof user !== 'object' || !user.username) {
        localStorage.removeItem('user');
        user = null;
      }
    } else if (userStr === "登录成功") {
      // 清除无效的用户数据
      localStorage.removeItem('user');
      user = null;
    }
  } catch (e) {
    localStorage.removeItem('user');
    user = null;
  }

  console.log('Parsed user:', user);
  console.log('Target path:', to.path);

  if (to.matched.some(record => record.meta.requiresAuth) && !user) {
    console.log('Redirecting to login: protected route and no user');
    next('/login');
  } else if ((to.path === '/login' || to.path === '/register') && user) {
    console.log('Redirecting to home: auth route and user exists');
    next('/home');
  } else {
    console.log('Proceeding normally');
    next();
  }
});



export default router
