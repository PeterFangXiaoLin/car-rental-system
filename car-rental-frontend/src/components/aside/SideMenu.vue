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
import { ref, watch } from 'vue'
import { Location, Document, Menu as IconMenu, Setting, Van, House } from '@element-plus/icons-vue'
import { useRoute } from 'vue-router'

// 定义props
defineProps({
  isCollapse: {
    type: Boolean,
    default: false,
  },
})

const route = useRoute()

// 当前激活的菜单项
const activeIndex = ref('/')

// 监听路由变化，更新当前激活的菜单项
watch(
  () => route.path,
  (newPath) => {
    activeIndex.value = newPath
  },
  { immediate: true },
)

// 手动定义菜单项
const menuItems = [
  {
    index: '/',
    title: '首页',
    icon: House,
  },
  {
    index: '/car',
    title: '车辆管理',
    icon: Location,
    children: [
      { index: '/car/list', title: '车辆列表' },
      { index: '/car/category', title: '车辆分类' },
      { index: '/car/status', title: '车辆状态' },
    ],
  },
  {
    index: '/order',
    title: '订单管理',
    icon: IconMenu,
  },
  {
    index: '/admin/userManage',
    title: '用户管理',
    icon: Document,
  },
  {
    index: '/system',
    title: '系统设置',
    icon: Setting,
  },
]
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
