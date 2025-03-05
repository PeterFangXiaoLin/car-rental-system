<template>
  <div class="auth-layout">
    <router-view></router-view>
  </div>
</template>

<style scoped>
.auth-layout {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-image: url('@/assets/login-background.jpg');
  background-size: cover;
  background-position: center;
  overflow: hidden;
}
</style>
