<template>
  <el-form :model="form" ref="formRef" :rules="rules" label-width="80px">
    <el-form-item label="用户昵称" prop="userName">
      <el-input v-model="form.userName" maxlength="30" placeholder="请输入用户昵称" />
    </el-form-item>
    <el-form-item label="用户简介" prop="userProfile">
      <el-input
        v-model="form.userProfile"
        type="textarea"
        :rows="4"
        maxlength="200"
        show-word-limit
        placeholder="请输入用户简介"
      />
    </el-form-item>
    <el-form-item label="手机号码" prop="phoneNumber">
      <el-input v-model="form.phoneNumber" maxlength="11" placeholder="请输入手机号码" />
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-input v-model="form.email" maxlength="50" placeholder="请输入邮箱" />
    </el-form-item>
    <el-form-item label="性别">
      <el-radio-group v-model="form.gender">
        <el-radio value="0">男</el-radio>
        <el-radio value="1">女</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="handleSubmit">保存</el-button>
      <el-button @click="resetForm">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import { updateUserUsingPost } from '@/api/userController'
import { ref, type PropType } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { useLoginUserStore } from '@/stores/useLoginUserStore'

const props = defineProps({
  user: {
    type: Object as PropType<API.LoginUserVO>,
    required: true,
  },
})

const form = ref<API.UserUpdateRequest>({
  userName: '',
  userProfile: '',
  phoneNumber: '',
  email: '',
})

const loginUserStore = useLoginUserStore()
const formRef = ref<FormInstance>()

// 表单校验规则
const rules = ref<FormRules>({
  userName: [
    { required: true, message: '用户昵称不能为空', trigger: 'blur' },
    { min: 2, max: 30, message: '用户昵称长度必须在 2 到 30 个字符之间', trigger: 'blur' },
  ],
  userProfile: [{ max: 200, message: '用户简介不能超过 200 个字符', trigger: 'blur' }],
  phoneNumber: [{ pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }],
  email: [{ type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }],
})

// 提交基本信息
const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const { data: res } = await updateUserUsingPost(form.value)
        if (res?.code === 0 && res?.data) {
          ElMessage.success('修改成功')
          // 更新用户信息
          loginUserStore.setLoginUser(res.data)
        } else {
          ElMessage.error(res?.message || '修改失败')
        }
      } catch (error) {
        console.error('修改用户信息失败:', error)
        ElMessage.error('修改失败，请稍后重试')
      }
    }
  })
}

// 重置基本信息表单
const resetForm = () => {
  formRef.value?.resetFields()
  initForm()
}

// 初始化表单数据
const initForm = () => {
  if (props.user) {
    form.value = {
      userName: props.user.userName || '',
      userProfile: props.user.userProfile || '',
      phoneNumber: props.user.phoneNumber || '',
      email: props.user.email || '',
    }
  }
}

// 初始化表单数据
initForm()
</script>
