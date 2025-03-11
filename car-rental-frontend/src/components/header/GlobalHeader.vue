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
        <el-breadcrumb-item v-for="(item, index) in breadcrumbList" :key="index" :to="item.path">
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
              <el-icon><setting /></el-icon>账号设置
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
import { Fold, Expand, User, Setting, SwitchButton, ArrowDown } from '@element-plus/icons-vue'
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
const breadcrumbList = ref<Array<{ title: string; path: string }>>([{ title: '首页', path: '/' }])

// 根据路径获取面包屑标题
const getBreadcrumbTitle = (path: string): string => {
  const routeMap: Record<string, string> = {
    '/': '首页',
    '/auth': '认证',
    '/auth/login': '登录',
    '/auth/register': '注册',
    '/user': '用户',
    '/user/profile': '个人中心',
    '/user/settings': '账号设置',
    '/car': '车辆管理',
    '/car/list': '车辆列表',
    '/car/category': '车辆分类',
    '/car/status': '车辆状态',
    '/order': '订单管理',
    '/system': '系统设置',
  }

  return routeMap[path] || path.split('/').pop() || ''
}

// 更新面包屑
const updateBreadcrumb = () => {
  breadcrumbList.value = [{ title: '首页', path: '/' }]

  const pathSnippets = route.path.split('/').filter((i) => i)

  pathSnippets.forEach((_, index) => {
    const url = `/${pathSnippets.slice(0, index + 1).join('/')}`
    const title = getBreadcrumbTitle(url)
    if (title) {
      breadcrumbList.value.push({ title, path: url })
    }
  })
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
      router.push('/user/settings')
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
      } catch (error) {
        console.error('退出登录失败:', error)
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
