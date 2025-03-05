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
        <el-menu-item index="/">
          <el-icon><house /></el-icon>
          <template #title>首页</template>
        </el-menu-item>
        
        <el-sub-menu index="1">
          <template #title>
            <el-icon><location /></el-icon>
            <span>车辆管理</span>
          </template>
          <el-menu-item index="1-1">车辆列表</el-menu-item>
          <el-menu-item index="1-2">车辆分类</el-menu-item>
          <el-menu-item index="1-3">车辆状态</el-menu-item>
        </el-sub-menu>
        
        <el-menu-item index="2">
          <el-icon><icon-menu /></el-icon>
          <template #title>订单管理</template>
        </el-menu-item>
        
        <el-menu-item index="3">
          <el-icon><document /></el-icon>
          <template #title>用户管理</template>
        </el-menu-item>
        
        <el-menu-item index="4">
          <el-icon><setting /></el-icon>
          <template #title>系统设置</template>
        </el-menu-item>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Location, Document, Menu as IconMenu, Setting, Van, House } from '@element-plus/icons-vue'

// 定义props
defineProps({
  isCollapse: {
    type: Boolean,
    default: false
  }
})

// 当前激活的菜单项
const activeIndex = ref('/')
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