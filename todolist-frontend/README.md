# 待办事项应用前端

## 简介
待办事项应用是一个帮助用户管理和跟踪待办事项的工具。用户可以通过这个应用轻松地添加、删除、标记待办事项为已完成，并设置提醒。

## 功能
- 添加新待办事项
- 删除待办事项
- 标记待办事项为已完成
- 设置提醒时间
- 查看所有待办事项列表

## 安装
1. 确保系统安装了nodejs和npm

2.安装项目依赖：
    ```npm install
    ```




```
todolist-frontend
├─ index.html
├─ package-lock.json
├─ package.json
├─ public
│  └─ vite.svg
├─ README.md
├─ src
│  ├─ App.vue
│  ├─ assets
│  │  └─ vue.svg
│  ├─ components
│  │  ├─ calendar
│  │  │  └─ Calendar.vue
│  │  ├─ layout
│  │  │  ├─ AppFooter.vue
│  │  │  ├─ AppHeader.vue
│  │  │  ├─ AuthLayout.vue
│  │  │  ├─ MainLayout.vue
│  │  │  └─ Sidebar.vue
│  │  └─ todo
│  │     ├─ TodoFilter.vue
│  │     ├─ TodoForm.vue
│  │     ├─ TodoItem.vue
│  │     └─ TodoList.vue
│  ├─ main.js
│  ├─ router
│  │  └─ index.js
│  ├─ services
│  │  ├─ api.js
│  │  ├─ authService.js
│  │  ├─ categoryService.js
│  │  ├─ tagService.js
│  │  └─ todoService.js
│  ├─ stores
│  │  ├─ auth.js
│  │  ├─ category.js
│  │  ├─ tag.js
│  │  └─ todo.js
│  ├─ style.css
│  └─ views
│     ├─ CalendarView.vue
│     ├─ CategoriesView.vue
│     ├─ HomeView.vue
│     ├─ LoginView.vue
│     ├─ RegisterView.vue
│     ├─ TagsView.vue
│     └─ TodosView.vue
└─ vite.config.js

```