<template>
  <div class="login">
    <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title">汽车租赁系统</h3>
      <el-form-item prop="userAccount">
        <el-input
          v-model="loginForm.userAccount"
          type="text"
          size="large"
          auto-complete="off"
          placeholder="账号"
        >
          <template #prefix>
            <el-icon><User /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="userPassword">
        <el-input
          v-model="loginForm.userPassword"
          type="password"
          size="large"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter="handleLogin"
        >
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item style="width: 100%">
        <el-button
          :loading="loading"
          size="large"
          type="primary"
          style="width: 100%"
          @click.prevent="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <div style="float: right; margin-top: 10px">
          <router-link class="link-type" to="/auth/register">立即注册</router-link>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref, onBeforeMount } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { userLoginUsingPost, getLoginUserUsingGet } from '@/api/userController'
import { useLoginUserStore } from '@/stores/useLoginUserStore'

const router = useRouter()
const route = useRoute()
const loginUserStore = useLoginUserStore()

// 检查登录状态
const checkLoginStatus = async () => {
  try {
    const res = await getLoginUserUsingGet()
    if (res.data?.code === 0) {
      // 用户已登录，重定向到首页
      router.replace('/')
    }
  } catch (error) {
    console.error('检查登录状态失败:', error)
  }
}

onBeforeMount(() => {
  checkLoginStatus()
})

const loginForm = ref<API.UserLoginRequest>({
  userAccount: '',
  userPassword: '',
})

const loginRules = {
  userAccount: [{ required: true, trigger: 'blur', message: '请输入账号' }],
  userPassword: [{ required: true, trigger: 'blur', message: '请输入密码' }],
}

const loading = ref(false)
const loginFormRef = ref()

const handleLogin = async () => {
  if (!loginFormRef.value) return

  await loginFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      loading.value = true
      try {
        const res = await userLoginUsingPost(loginForm.value)

        if (res.data?.code === 0 && res.data?.data) {
          await loginUserStore.fetchLoginUser()
          ElMessage.success('登录成功')
          const redirect = route.query.redirect as string
          router.replace(redirect || '/')
        } else {
          ElMessage.error(res.data?.message || '登录失败')
        }
      } catch (error) {
        ElMessage.error('登录失败，请稍后重试')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #409eff;
  font-size: 24px;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.login-form :deep(.el-input) {
  height: 40px;
}

.login-form :deep(.el-input input) {
  height: 40px;
}

.link-type {
  color: #409eff;
  text-decoration: none;
}

.link-type:hover {
  color: #79bbff;
}
</style>
