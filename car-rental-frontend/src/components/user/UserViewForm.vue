<template>
  <el-dialog v-model="visible" width="40%" @close="handleClose" destroy-on-close center>
    <template #header>
      <h3 class="title">查看用户</h3>
    </template>
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      size="large"
      label-width="100px"
      class="mt-8"
      disabled
    >
      <el-form-item label="用户昵称" prop="userName">
        <el-input v-model="formData.userName" placeholder="请输入用户昵称" clearable />
      </el-form-item>
      <el-form-item label="用户头像" prop="userAvatar">
        <el-image
          :src="formData.userAvatar"
          :preview-src-list="[formData.userAvatar]"
          style="width: 100px; height: 100px; object-fit: cover"
        />
      </el-form-item>
      <el-form-item label="用户简介" prop="userProfile">
        <el-input v-model="formData.userProfile" placeholder="请输入用户简介" clearable />
      </el-form-item>
      <el-form-item label="性别" prop="gender">
        <el-select v-model="formData.gender" placeholder="请选择性别" clearable>
          <el-option label="男" :value="0" />
          <el-option label="女" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="手机号码" prop="phoneNumber">
        <el-input v-model="formData.phoneNumber" placeholder="请输入手机号码" clearable />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="formData.email" placeholder="请输入邮箱" clearable />
      </el-form-item>
      <el-form-item label="会员状态" prop="memberLevel">
        <el-select v-model="formData.memberLevel" placeholder="请选择会员状态" clearable>
          <el-option label="vip" :value="1" />
          <el-option label="普通用户" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item label="用户角色" prop="userRole">
        <el-select v-model="formData.userRole" placeholder="请选择用户角色" clearable>
          <el-option label="普通用户" value="user" />
          <el-option label="管理员" value="admin" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="flex justify-center gap-[5%]">
        <el-button @click="handleClose" size="large">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElLoading, ElMessage, type FormInstance, type FormRules } from 'element-plus'
import {
  adminUpdateUserUsingPost,
  getUserByIdUsingGet,
  updateAvatarUsingPost,
} from '@/api/userController'
import { Plus } from '@element-plus/icons-vue'

const visible = ref(false)
const formRef = ref<FormInstance>()
const formData = ref<API.UserVO>({})

const rules = reactive<FormRules>({
  userName: [
    { required: true, message: '请输入用户昵称', trigger: 'blur' },
    { min: 2, max: 20, message: '昵称长度在2-20个字符之间', trigger: 'blur' },
  ],
  userAvatar: [{ required: false, message: '请上传用户头像', trigger: 'change' }],
  userProfile: [{ max: 200, message: '简介不能超过200个字符', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  phoneNumber: [{ required: false, message: '请输入手机号码', trigger: 'blur' }],
  email: [{ required: false, message: '请输入邮箱', trigger: 'blur' }],
  memberLevel: [{ required: true, message: '请选择会员状态', trigger: 'change' }],
  userRole: [{ required: true, message: '请选择用户角色', trigger: 'change' }],
})

// 重置表单
const resetForm = () => {
  formData.value = {}
  formRef.value?.resetFields()
}

// 关闭弹窗
const handleClose = () => {
  formRef.value?.resetFields()
  resetForm()
  visible.value = false
}

// 供父组件调用的打开方法
const open = async (userId: string) => {
  try {
    resetForm()
    visible.value = true
    const res = await getUserByIdUsingGet({
      id: userId,
    })
    if (res.data?.code === 0 && res.data?.data) {
      formData.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('获取用户信息失败: ' + error)
  }
}

defineExpose({
  open,
})
</script>

<style scoped>
.title {
  font-size: 18px;
  font-weight: bold;
  color: #000;
  font-family:
    'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana,
    sans-serif;
  margin: 0 auto;
}

.avatar-uploader {
  width: 150px;
  margin-bottom: 10px;
}

.avatar-uploader-box {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
  text-align: center;
  width: 150px;
  height: 150px;
}

.avatar-uploader-box:hover {
  border-color: var(--el-color-primary);
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 150px;
  height: 120px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.avatar-image {
  width: 150px;
  height: 150px;
  display: block;
  object-fit: cover;
}

.upload-tip {
  font-size: 12px;
  color: #8c939d;
  position: absolute;
  bottom: 10px;
  left: 0;
  right: 0;
  text-align: center;
}

.el-overlay-dialog {
  display: flex;
  justify-content: center;
  align-items: center;
}

.el-dialog {
  margin: 0 !important;
}

.el-dialog__header {
  height: 54px;
  padding: 0;
  margin-right: 0 !important;
  border-bottom: 1px solid var(--el-border-color);
}

.el-dialog__body {
  padding: 15px !important;
}

.el-dialog__footer {
  border-top: 1px solid var(--el-border-color);
}

.el-dialog__headerbtn {
  top: 0;
}
</style>
