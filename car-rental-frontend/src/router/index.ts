import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/pages/HomePage.vue'
import UserLoginPage from '@/pages/user/UserLoginPage.vue'
import UserRegisterPage from '@/pages/user/UserRegisterPage.vue'
import UserManagePage from '@/pages/admin/UserManagePage.vue'
import UserInfoPage from '@/pages/user/UserInfoPage.vue'
import ACCESS_ENUM from '@/access/accessEnum'
import NoAuthPage from '@/error/NoAuthPage.vue'
import NoFoundPage from '@/error/NoFoundPage.vue'
import AddPicturePage from '@/pages/picture/AddPicturePage.vue'
import { useLoginUserStore } from '@/stores/useLoginUserStore'
import PictureManagePage from '@/pages/admin/PictureManagePage.vue'

export const routes = [
  {
    path: '/',
    name: '首页',
    component: () => import('@/views/HomeView.vue'),
    meta: {
      access: ACCESS_ENUM.NOT_LOGIN,
    },
  },
  {
    path: '/user/login',
    name: '用户登录',
    component: UserLoginPage,
    meta: {
      access: ACCESS_ENUM.NOT_LOGIN,
      hideInMenu: true,
    },
  },
  {
    path: '/user/register',
    name: '用户注册',
    component: UserRegisterPage,
    meta: {
      access: ACCESS_ENUM.NOT_LOGIN,
      hideInMenu: true,
    },
  },
  {
    path: '/user/info',
    name: '用户信息',
    component: UserInfoPage,
    meta: {
      access: ACCESS_ENUM.USER,
      hideInMenu: true,
    },
  },
  {
    path: '/admin/userManage',
    name: '用户管理',
    component: UserManagePage,
    meta: {
      access: ACCESS_ENUM.ADMIN,
    },
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
