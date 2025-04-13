import { createRouter, createWebHistory } from 'vue-router'
import ACCESS_ENUM from '@/access/accessEnum'

export const routes = [
  {
    path: '/auth',
    name: '认证',
    component: () => import('@/layouts/AuthLayout.vue'),
    meta: {
      access: ACCESS_ENUM.NOT_LOGIN,
      hideInMenu: true,
    },
    children: [
      {
        path: 'login',
        name: '用户登录',
        component: () => import('@/views/user/LoginPage.vue'),
        meta: {
          access: ACCESS_ENUM.NOT_LOGIN,
          hideInMenu: true,
        },
      },
      {
        path: 'register',
        name: '用户注册',
        component: () => import('@/views/user/RegisterPage.vue'),
        meta: {
          access: ACCESS_ENUM.NOT_LOGIN,
          hideInMenu: true,
        },
      },
    ],
  },
  {
    path: '/',
    component: () => import('@/layouts/BasicLayout.vue'),
    children: [
      {
        path: '',
        name: '首页',
        component: () => import('@/views/HomeView.vue'),
        meta: {
          access: ACCESS_ENUM.NOT_LOGIN,
        },
      },
      {
        path: 'vehicle/:id',
        name: '车辆详情',
        props: true,
        component: () => import('@/views/vehicle/VehicleDetailPage.vue'),
        meta: {
          hideInMenu: true,
        },
      },
      {
        path: 'order/create',
        name: '确认订单',
        component: () => import('@/views/order/OrderSubmitPage.vue'),
        meta: {
          access: ACCESS_ENUM.USER,
          hideInMenu: true,
        },
      },
    ],
  },
  {
    path: '/user',
    name: '用户',
    component: () => import('@/layouts/BasicLayout.vue'),
    children: [
      {
        path: 'profile',
        name: '个人中心',
        component: () => import('@/views/user/ProfilePage.vue'),
        meta: {
          access: ACCESS_ENUM.USER,
          hideInMenu: true,
        },
      },
      {
        path: 'settings',
        name: '账号设置',
        component: () => import('@/views/user/SettingsPage.vue'),
        meta: {
          access: ACCESS_ENUM.USER,
          hideInMenu: true,
        },
      },
    ],
  },
  {
    path: '/admin',
    name: '管理',
    component: () => import('@/layouts/BasicLayout.vue'),
    children: [
      {
        path: 'userManage',
        name: '用户管理',
        component: () => import('@/views/admin/UserManagePage.vue'),
        meta: {
          access: ACCESS_ENUM.ADMIN,
        },
      },
      {
        path: 'storeManage',
        name: '店铺管理',
        component: () => import('@/views/admin/StoreManagePage.vue'),
        meta: {
          access: ACCESS_ENUM.ADMIN,
        },
      },
    ],
  },
  {
    path: '/401',
    name: '无权限',
    component: () => import('@/components/error/401.vue'),
    meta: {
      access: ACCESS_ENUM.NOT_LOGIN,
      hideInMenu: true,
    },
  },
  {
    path: '/404',
    name: '404',
    component: () => import('@/components/error/404.vue'),
    meta: {
      access: ACCESS_ENUM.NOT_LOGIN,
      hideInMenu: true,
    },
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    redirect: '/404',
    meta: {
      access: ACCESS_ENUM.NOT_LOGIN,
      hideInMenu: true,
    },
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: routes,
})

export default router
