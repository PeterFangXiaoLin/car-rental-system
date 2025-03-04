<template>
  <div class="common-layout">
    <el-container class="h-full w-full">
      <el-aside width="200px" class="bg-red-500">Aside</el-aside>
      <el-container>
        <el-header class="bg-blue-500">Header</el-header>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts"></script>

<style scoped>
body {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

.common-layout {
  height: 100vh;
}
</style>
