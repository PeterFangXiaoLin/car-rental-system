<template>
  <div class="common-layout">
    <el-container class="h-full w-full">
      <el-aside :width="isCollapse ? '64px' : '200px'" class="aside-container">
        <SideMenu :is-collapse="isCollapse" />
      </el-aside>
      <el-container class="main-container">
        <el-header class="header-container">
          <GlobalHeader :is-collapse="isCollapse" @toggle-collapse="toggleCollapse" />
        </el-header>
        <el-main class="content-container">
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
.common-layout {
  height: 100%;
  position: relative;
}

.aside-container {
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  height: 100vh;
  z-index: 10;
  transition: width 0.3s ease;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12);
  padding: 0;
  box-sizing: border-box;
}

.main-container {
  margin-left: v-bind('isCollapse ? "64px" : "200px"');
  transition: margin-left 0.3s ease;
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.header-container {
  position: fixed;
  top: 0;
  right: 0;
  left: v-bind('isCollapse ? "64px" : "200px"');
  z-index: 9;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  padding: 0 20px;
  height: 70px;
  transition: left 0.3s ease;
}

.content-container {
  margin-top: 70px;
  padding: 20px;
  background-color: #f5f7f9;
  height: calc(100vh - 70px);
  overflow-y: auto;
}
</style>
