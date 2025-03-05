<template>
  <el-form ref="pwdFormRef" :model="pwdForm" :rules="pwdRules" label-width="100px" class="mt-4">
    <el-form-item label="旧密码" prop="oldPassword">
      <el-input
        v-model="pwdForm.oldPassword"
        type="password"
        show-password
        placeholder="请输入旧密码"
      />
    </el-form-item>
    <el-form-item label="新密码" prop="newPassword">
      <el-input
        v-model="pwdForm.newPassword"
        type="password"
        show-password
        placeholder="请输入新密码"
      />
    </el-form-item>
    <el-form-item label="确认密码" prop="confirmPassword">
      <el-input
        v-model="pwdForm.confirmPassword"
        type="password"
        show-password
        placeholder="请确认新密码"
      />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="handleUpdatePwd">保存</el-button>
      <el-button @click="resetPwdForm">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useLoginUserStore } from '@/stores/useLoginUserStore'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { updateUserPasswordUsingPost } from '@/api/userController'
import { useRouter } from 'vue-router'

const router = useRouter()
const pwdFormRef = ref<FormInstance>()
const loginUserStore = useLoginUserStore()
// 密码表单
const pwdForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
})

// 密码校验规则
const pwdRules = reactive<FormRules>({
  oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 8, max: 20, message: '长度在 8 到 20 个字符', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== pwdForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur',
    },
  ],
})

// 更新密码
const handleUpdatePwd = async () => {
  if (!pwdFormRef.value) return
  await pwdFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      try {
        const res = await updateUserPasswordUsingPost({
          oldPassword: pwdForm.oldPassword,
          newPassword: pwdForm.newPassword,
          checkPassword: pwdForm.newPassword,
        })

        if (res?.code === 0 && res?.data) {
          ElMessage.success('密码修改成功')
          resetPwdForm()
          // 自动退出登录
          loginUserStore.setLoginUser({
            userName: '未登录',
          })
          await router.push('/auth/login')
        } else {
          ElMessage.error(res?.message || '密码修改失败')
        }
      } catch (error) {
        ElMessage.error('密码修改失败' + error)
      }
    }
  })
}

// 重置密码表单
const resetPwdForm = () => {
  pwdFormRef.value?.resetFields()
}
</script>

<style scoped></style>
