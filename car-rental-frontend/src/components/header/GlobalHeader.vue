<template>
  <div id="global-header">
    <div class="left-section">
      <div class="toggle-button" @click="toggleCollapse">
        <el-icon size="24">
          <fold v-if="!isCollapse" />
          <expand v-else />
        </el-icon>
      </div>
      <!-- 使用面包屑替代标题 -->
      <el-breadcrumb separator="/">
        <el-breadcrumb-item v-for="(item, index) in breadcrumbList" :key="index">
          {{ item.title }}
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="right-section">
      <!-- 未登录状态 -->
      <template v-if="!isLoggedIn">
        <el-button type="primary" link @click="handleLogin">登录</el-button>
        <el-button link class="register-btn" @click="handleRegister">注册</el-button>
      </template>

      <!-- 已登录状态 -->
      <el-dropdown v-else trigger="click" @command="handleCommand">
        <div class="user-info">
          <el-avatar :size="32" :src="userAvatar">{{ userInitials }}</el-avatar>
          <span class="username">{{ userName }}</span>
          <el-icon><arrow-down /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">
              <el-icon><user /></el-icon>个人中心
            </el-dropdown-item>
            <el-dropdown-item command="settings">
              <el-icon><setting /></el-icon>个人设置
            </el-dropdown-item>
            <el-dropdown-item command="favorite">
              <el-icon><star /></el-icon>我的收藏
            </el-dropdown-item>
            <el-dropdown-item command="history">
              <el-icon><clock /></el-icon>浏览记录
            </el-dropdown-item>
            <el-dropdown-item divided command="logout">
              <el-icon><switch-button /></el-icon>退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  Fold,
  Expand,
  User,
  Setting,
  SwitchButton,
  ArrowDown,
  Clock,
  Star,
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useLoginUserStore } from '@/stores/useLoginUserStore'
import { userLogoutUsingPost } from '@/api/userController'

const router = useRouter()
const route = useRoute()
const loginUserStore = useLoginUserStore()

// 接收父组件传递的props
defineProps({
  isCollapse: {
    type: Boolean,
    default: false,
  },
})

// 定义事件
const emit = defineEmits(['toggle-collapse'])

// 切换菜单折叠状态
const toggleCollapse = () => {
  emit('toggle-collapse')
}

// 从store获取登录状态和用户信息
const isLoggedIn = computed(() => !!loginUserStore.loginUser?.id)
const userName = computed(() => loginUserStore.loginUser?.userName || '未登录')
const userAvatar = computed(() => loginUserStore.loginUser?.userAvatar || '')

// 如果没有头像，显示用户名首字母
const userInitials = computed(() => {
  return userName.value ? userName.value.charAt(0).toUpperCase() : '?'
})

// 面包屑数据
const breadcrumbList = ref<Array<{ title: string }>>([{ title: '首页' }])

// 根据路径获取面包屑标题
const getBreadcrumbTitle = (path: string): string => {
  const routeMap: Record<string, string> = {
    '/': '首页',
    '/auth': '认证',
    '/auth/login': '用户登录',
    '/auth/register': '用户注册',
    '/user': '用户',
    '/user/profile': '个人中心',
    '/user/manage': '用户管理',
    '/user/history': '浏览历史',
    '/user/favorite': '我的收藏',
    '/driver': '司机',
    '/driver/manage': '司机管理',
    '/store': '门店',
    '/store/manage': '门店管理',
    '/vehicle': '车辆',
    '/vehicle/manage': '车辆管理',
    '/vehicle/brand': '车辆品牌',
    '/vehicle/brand/manage': '车辆品牌管理',
    '/vehicle/model': '车辆型号',
    '/vehicle/model/manage': '车辆型号管理',
    '/vehicle/type': '车型',
    '/vehicle/type/manage': '车型管理',
    '/vehicle/energy': '能源类型',
    '/vehicle/energy/manage': '能源类型管理',
    '/order/create': '创建订单',
    '/401': '无权限',
    '/404': '页面不存在',
  }

  return routeMap[path] || '未知页面'
}

// 更新面包屑
const updateBreadcrumb = () => {
  breadcrumbList.value = []

  // 如果是首页，只显示"首页"
  if (route.path === '/') {
    breadcrumbList.value.push({ title: '首页' })
    return
  }

  // 如果是错误页面，显示对应标题
  if (route.path === '/401' || route.path === '/404') {
    breadcrumbList.value.push({ title: '首页' })
    breadcrumbList.value.push({ title: getBreadcrumbTitle(route.path) })
    return
  }

  // 处理车辆详情页（格式为 /vehicle/数字）
  const vehicleDetailRegex = /^\/vehicle\/\d+$/
  if (vehicleDetailRegex.test(route.path)) {
    breadcrumbList.value.push({ title: '首页' })
    breadcrumbList.value.push({ title: '车辆详情' })
    return
  }

  // 添加首页作为第一个面包屑项
  breadcrumbList.value.push({ title: '首页' })

  // 直接添加当前页面的面包屑，跳过中间路径
  // 这样可以避免显示中间未定义的路径
  breadcrumbList.value.push({ title: getBreadcrumbTitle(route.path) })
}

// 监听路由变化，更新面包屑
watch(
  () => route.path,
  () => {
    updateBreadcrumb()
  },
  { immediate: true },
)

// 处理登录按钮点击
const handleLogin = () => {
  router.push('/auth/login')
}

// 处理注册按钮点击
const handleRegister = () => {
  router.push('/auth/register')
}

// 处理下拉菜单命令
const handleCommand = async (command: string) => {
  switch (command) {
    case 'profile':
      router.push('/user/profile')
      break
    case 'settings':
      router.push('/user/profile')
      break
    case 'favorite':
      router.push('/user/favorite')
      break
    case 'history':
      router.push('/user/history')
      break
    case 'logout':
      try {
        const res = await userLogoutUsingPost()
        if (res.data?.code === 0) {
          ElMessage.success('退出登录成功')
          // 清除用户信息
          loginUserStore.setLoginUser({ userName: '未登录' })
          // 退出登录后跳转到首页
          router.push('/')
        }
      } catch {
        ElMessage.error('退出登录失败')
      }
      break
    default:
      break
  }
}
</script>

<style scoped>
#global-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  height: 100%;
}

.left-section {
  display: flex;
  align-items: center;
}

.toggle-button {
  cursor: pointer;
  padding: 10px;
  border-radius: 4px;
  transition: background-color 0.3s;
  margin-right: 16px;
}

.toggle-button:hover {
  background-color: #f5f5f5;
}

.right-section {
  display: flex;
  align-items: center;
}

.register-btn {
  margin-left: 12px;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: background-color 0.3s;
  height: 100%;
}

.user-info:hover {
  background-color: #f5f5f5;
}

.username {
  margin: 0 8px;
  font-size: 14px;
  color: #333;
}

:deep(.el-breadcrumb__item) {
  display: flex;
  align-items: center;
}

:deep(.el-breadcrumb__inner) {
  font-size: 16px;
  color: #606266;
}

:deep(.el-breadcrumb__inner.is-link) {
  color: #409eff;
  font-weight: normal;
}

:deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: #303133;
  font-weight: 600;
}
</style>
