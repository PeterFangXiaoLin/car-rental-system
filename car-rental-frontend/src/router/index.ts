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
      {
        path: 'user/profile',
        name: '个人中心',
        component: () => import('@/views/user/ProfilePage.vue'),
        meta: {
          access: ACCESS_ENUM.USER,
          hideInMenu: true,
        },
      },
      {
        path: 'user/order',
        name: '我的订单',
        component: () => import('@/views/user/OrderPage.vue'),
        meta: {
          access: ACCESS_ENUM.USER,
          hideInMenu: true,
        },
      },
      {
        path: 'user/history',
        name: '浏览历史',
        component: () => import('@/views/user/HistoryPage.vue'),
        meta: {
          access: ACCESS_ENUM.USER,
          hideInMenu: true,
        },
      },
      {
        path: 'user/favorite',
        name: '我的收藏',
        component: () => import('@/views/user/FavoritePage.vue'),
        meta: {
          access: ACCESS_ENUM.USER,
          hideInMenu: true,
        },
      },
      {
        path: 'user/manage',
        name: '用户管理',
        component: () => import('@/views/admin/UserManagePage.vue'),
        meta: {
          access: ACCESS_ENUM.ADMIN,
        },
      },
      {
        path: 'driver/manage',
        name: '司机管理',
        component: () => import('@/views/driver/DriverManagePage.vue'),
        meta: {
          access: ACCESS_ENUM.ADMIN,
        },
      },
      {
        path: 'store/manage',
        name: '门店管理',
        component: () => import('@/views/store/StoreManagePage.vue'),
        meta: {
          access: ACCESS_ENUM.ADMIN,
        },
      },
      {
        path: 'order/manage',
        name: '订单管理',
        component: () => import('@/views/order/OrderManagePage.vue'),
        meta: {
          access: ACCESS_ENUM.ADMIN,
        },
      },
      {
        path: 'comment/manage',
        name: '评论管理',
        component: () => import('@/views/comment/CommentManagePage.vue'),
        meta: {
          access: ACCESS_ENUM.ADMIN,
        },
      },
      {
        path: 'vehicle/manage',
        name: '车辆管理',
        component: () => import('@/views/vehicle/VehicleManagePage.vue'),
        meta: {
          access: ACCESS_ENUM.ADMIN,
        },
      },
      {
        path: 'vehicle/brand/manage',
        name: '车辆品牌管理',
        component: () => import('@/views/vehiclebrand/VehicleBrandManagePage.vue'),
        meta: {
          access: ACCESS_ENUM.ADMIN,
        },
      },
      {
        path: 'vehicle/model/manage',
        name: '车辆型号管理',
        component: () => import('@/views/vehiclemodel/VehicleModelManagePage.vue'),
        meta: {
          access: ACCESS_ENUM.ADMIN,
        },
      },
      {
        path: 'vehicle/type/manage',
        name: '车型管理',
        component: () => import('@/views/vehicletypedict/VehicleTypeDictManagePage.vue'),
        meta: {
          access: ACCESS_ENUM.ADMIN,
        },
      },
      {
        path: 'vehicle/energy/manage',
        name: '能源类型管理',
        component: () => import('@/views/energytypedict/EnergyTypeDictManagePage.vue'),
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
