<template>
  <div id="side-menu">
    <el-scrollbar>
      <!-- 系统名称和Logo -->
      <div class="logo-container">
        <div class="logo-icon" v-if="!isCollapse">
          <el-icon :size="36" color="#409EFF"><van /></el-icon>
        </div>
        <div class="logo-icon-small" v-else>
          <el-icon :size="28" color="#409EFF"><van /></el-icon>
        </div>
        <h1 class="system-name" v-if="!isCollapse">租车系统</h1>
      </div>

      <el-menu
        :collapse="isCollapse"
        :default-active="activeIndex"
        @select="handleSelect"
        class="el-menu-vertical"
        background-color="#001529"
        text-color="#fff"
        active-text-color="#ffd04b"
        router
      >
        <!-- 动态生成菜单项 -->
        <template v-for="item in menuItems" :key="item.index">
          <!-- 带子菜单的项 -->
          <el-sub-menu v-if="item.children && item.children.length > 0" :index="item.index">
            <template #title>
              <el-icon v-if="item.icon"><component :is="item.icon" /></el-icon>
              <span>{{ item.title }}</span>
            </template>
            <el-menu-item v-for="child in item.children" :key="child.index" :index="child.index">
              {{ child.title }}
            </el-menu-item>
          </el-sub-menu>

          <!-- 没有子菜单的项 -->
          <el-menu-item v-else :index="item.index">
            <el-icon v-if="item.icon"><component :is="item.icon" /></el-icon>
            <template #title>{{ item.title }}</template>
          </el-menu-item>
        </template>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { Location, Document, Menu as IconMenu, Setting, Van, House } from '@element-plus/icons-vue'
import { useRoute, useRouter } from 'vue-router'
import { routes } from '@/router'
import { useLoginUserStore } from '@/stores/useLoginUserStore'
import { checkAccess } from '@/access/checkAccess'
import * as Icons from '@element-plus/icons-vue'

// 定义props
defineProps({
  isCollapse: {
    type: Boolean,
    default: false,
  },
})

const router = useRouter()
const route = useRoute()
const loginUserStore = useLoginUserStore()

// 当前激活的菜单项
const activeIndex = computed(() => route.path)

// 处理菜单选择
const handleSelect = (index: string) => {
  router.push(index)
}

// 把路由转换成菜单项
const routeToMenuItem = (route: any) => {
  // 根据路由路径获取对应的图标
  const getIconForRoute = (path: string) => {
    const iconMap: Record<string, any> = {
      '/': Icons.HomeFilled,
      '/user/manage': Icons.User,
      '/driver/manage': Icons.Van,
      '/vehicle/manage': Icons.CarFilled,
      '/vehicle/brand/manage': Icons.Shop,
      '/vehicle/model/manage': Icons.Operation,
      '/vehicle/type/manage': Icons.List,
      '/vehicle/energy/manage': Icons.Lightning
    }
    
    return iconMap[path] || Icons.Document
  }
  
  return {
    index: route.path,
    title: route.name,
    icon: getIconForRoute(route.path),
    children: route.children
      ? route.children
          .filter((child: any) => {
            if (child.meta?.hideInMenu) {
              return false
            }
            return checkAccess(loginUserStore.loginUser, child.meta?.access)
          })
          .map((child: any) => ({
            index: route.path === '/' ? child.path : `${route.path}/${child.path}`,
            title: child.name,
          }))
      : []
  }
}

// 生成菜单项
const menuItems = computed(() => {
  // 找到主布局下的路由
  const mainLayoutRoute = routes.find(route => route.path === '/')
  if (!mainLayoutRoute || !mainLayoutRoute.children) {
    return []
  }
  
  // 过滤出符合条件的路由，并转换为菜单项
  return mainLayoutRoute.children
    .filter((route: any) => {
      // 过滤掉隐藏的菜单项
      if (route.meta?.hideInMenu) {
        return false
      }
      // 根据用户权限过滤
      return checkAccess(loginUserStore.loginUser, route.meta?.access)
    })
    .map(routeToMenuItem)
})
</script>

<style scoped>
.el-menu-vertical:not(.el-menu--collapse) {
  width: 200px;
  min-height: calc(100vh - 70px);
  border-right: none;
}

.el-menu-vertical.el-menu--collapse {
  border-right: none;
}

#side-menu {
  height: 100%;
  background-color: #001529;
  width: 100%;
  box-sizing: border-box;
}

.logo-container {
  height: 70px;
  display: flex;
  align-items: center;
  padding: 0 20px 0 16px;
  background-color: #002140;
  overflow: hidden;
  box-sizing: border-box;
  width: 100%;
}

.logo-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
}

.logo-icon-small {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
}

.system-name {
  color: white;
  font-size: 20px;
  font-weight: 600;
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
