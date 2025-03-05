<template>
  <div class="common-layout">
    <el-container class="h-full w-full">
      <el-aside :width="isCollapse ? '64px' : '200px'" class="aside-container">
        <SideMenu :is-collapse="isCollapse" />
      </el-aside>
      <el-container>
        <el-header class="header-container">
          <GlobalHeader :is-collapse="isCollapse" @toggle-collapse="toggleCollapse" />
        </el-header>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import SideMenu from '@/components/aside/SideMenu.vue'
import GlobalHeader from '@/components/header/GlobalHeader.vue'
import { ref } from 'vue'

const isCollapse = ref(false)

// 切换菜单折叠状态
const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}
</script>

<style scoped>
body {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

.common-layout {
  height: 100vh;
}

.aside-container {
  transition: width 0.3s ease;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12);
  padding: 0;
  box-sizing: border-box;
}

.header-container {
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  padding: 0 20px;
  height: 70px;
}

.el-main {
  padding: 20px;
  background-color: #f5f7f9;
}
</style>
