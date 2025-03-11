<template>
  <el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" class="register-form">
    <h3 class="title">汽车租赁系统</h3>
    <el-form-item prop="userAccount">
      <el-input
        v-model="registerForm.userAccount"
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
        v-model="registerForm.userPassword"
        type="password"
        size="large"
        auto-complete="off"
        placeholder="密码"
        @keyup.enter="handleRegister"
      >
        <template #prefix>
          <el-icon><Lock /></el-icon>
        </template>
      </el-input>
    </el-form-item>
    <el-form-item prop="checkPassword">
      <el-input
        v-model="registerForm.checkPassword"
        type="password"
        size="large"
        auto-complete="off"
        placeholder="确认密码"
        @keyup.enter="handleRegister"
      >
        <template #prefix>
          <el-icon><Lock /></el-icon>
        </template>
      </el-input>
    </el-form-item>
    <el-form-item prop="captchaCode">
      <el-input
        v-model="registerForm.captchaCode"
        size="large"
        auto-complete="off"
        placeholder="验证码"
        style="width: 63%"
        @keyup.enter="handleRegister"
      >
        <template #prefix>
          <el-icon><Key /></el-icon>
        </template>
      </el-input>
      <div class="register-code">
        <el-button type="primary" link @click="refreshCaptcha">
          <el-icon><Refresh /></el-icon>
        </el-button>
        <img :src="captchaImg" @click="refreshCaptcha" class="register-code-img" alt="验证码" />
      </div>
    </el-form-item>
    <el-form-item style="width: 100%">
      <el-button
        :loading="loading"
        size="large"
        type="primary"
        style="width: 100%"
        @click.prevent="handleRegister"
      >
        <span v-if="!loading">注 册</span>
        <span v-else>注 册 中...</span>
      </el-button>
      <div style="float: right; margin-top: 10px">
        <router-link class="link-type" to="/auth/login">使用已有账户登录</router-link>
      </div>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import { onMounted, onBeforeMount, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Key, Refresh } from '@element-plus/icons-vue'
import { userRegisterUsingPost, getLoginUserUsingGet } from '@/api/userController'
import { getCaptchaUsingGet, refreshCaptchaUsingGet } from '@/api/smsAuthController'
import type { FormInstance, FormItemRule } from 'element-plus'

const router = useRouter()

// 检查登录状态
const checkLoginStatus = async () => {
  try {
    const res = await getLoginUserUsingGet()
    if (res.data?.code === 0 && res.data?.data) {
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

const registerForm = ref<API.UserRegisterRequest>({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
  captchaCode: '',
  captchaKey: '',
})

const captchaImg = ref('')

const validatePass = (rule: FormItemRule, value: string, callback: (error?: Error) => void) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else {
    if (registerForm.value.checkPassword !== '') {
      registerFormRef.value?.validateField('checkPassword')
    }
    callback()
  }
}

const validateCheckPass = (
  rule: FormItemRule,
  value: string,
  callback: (error?: Error) => void,
) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.value.userPassword) {
    callback(new Error('两次输入密码不一致!'))
  } else {
    callback()
  }
}

const registerRules = {
  userAccount: [
    { required: true, trigger: 'blur', message: '请输入账号' },
    { min: 2, max: 20, message: '账号长度必须介于 2 和 20 之间', trigger: 'blur' },
  ],
  userPassword: [
    { required: true, validator: validatePass, trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度必须介于 6 和 20 之间', trigger: 'blur' },
  ],
  checkPassword: [{ required: true, validator: validateCheckPass, trigger: 'blur' }],
  captchaCode: [{ required: true, trigger: 'blur', message: '请输入验证码' }],
}

const loading = ref(false)
const registerFormRef = ref<FormInstance>()

const getCaptcha = async () => {
  try {
    const res = await getCaptchaUsingGet()
    if (res.data?.code === 0 && res.data?.data) {
      captchaImg.value = res.data.data.image
      registerForm.value.captchaKey = res.data.data.captchaKey
    }
  } catch (error) {
    console.error('获取验证码失败:', error)
  }
}

const refreshCaptcha = async () => {
  try {
    if (!registerForm.value.captchaKey) {
      await getCaptcha()
      return
    }
    const res = await refreshCaptchaUsingGet({ captchaKey: registerForm.value.captchaKey })
    if (res.data?.code === 0 && res.data?.data) {
      captchaImg.value = res.data.data.image
      registerForm.value.captchaKey = res.data.data.captchaKey
    }
  } catch (error) {
    console.error('刷新验证码失败:', error)
    // 如果刷新失败，尝试重新获取验证码
    getCaptcha()
  }
}

const handleRegister = async () => {
  if (!registerFormRef.value) return

  await registerFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      loading.value = true
      try {
        const res = await userRegisterUsingPost(registerForm.value)

        if (res.data?.code === 0 && res.data.data) {
          ElMessage.success('注册成功')
          router.push('/auth/login')
        } else {
          ElMessage.error(res.data.message || '注册失败，请稍后重试')
        }
      } catch (error) {
        if (error instanceof Error) {
          ElMessage.error(error.message || '注册失败，请稍后重试')
        } else {
          ElMessage.error('注册失败，请稍后重试')
        }
        // 注册失败时刷新验证码
        getCaptcha()
      } finally {
        loading.value = false
      }
    }
  })
}

onMounted(() => {
  getCaptcha()
})
</script>

<style scoped>
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #409eff;
  font-size: 24px;
}

.register-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.register-form :deep(.el-input) {
  height: 40px;
}

.register-form :deep(.el-input input) {
  height: 40px;
}

.register-code {
  width: 33%;
  height: 40px;
  float: right;
  display: flex;
  align-items: center;
  gap: 4px;
}

.register-code-img {
  height: 40px;
  cursor: pointer;
  vertical-align: middle;
}

.link-type {
  color: #409eff;
  text-decoration: none;
}

.link-type:hover {
  color: #79bbff;
}
</style>
