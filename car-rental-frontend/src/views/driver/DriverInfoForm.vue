<template>
  <el-dialog v-model="visible" width="40%" @close="handleClose" destroy-on-close center>
    <template #header>
      <h3 class="title">{{ title }}</h3>
    </template>
    <el-form
      ref="formRef"
      :disabled="isView"
      :model="formData"
      :rules="rules"
      size="large"
      label-width="100px"
      label-position="right"
      class="mt-8"
    >
      <el-form-item label="司机姓名" prop="driverName" v-if="formType !== 1">
        <el-input
          v-model="formData.driverName"
          :placeholder="isView ? '' : '请输入司机姓名'"
          clearable
        />
      </el-form-item>
      <el-form-item label="司机头像" prop="driverAvatar">
        <div class="avatar-uploader">
          <el-upload
            class="avatar-uploader-box"
            action="#"
            :before-upload="beforeAvatarUpload"
            :http-request="uploadRequest"
            :show-file-list="false"
          >
            <img v-if="formData.driverAvatar" :src="formData.driverAvatar" class="avatar-image" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            <div class="upload-tip">点击上传头像</div>
          </el-upload>
        </div>
      </el-form-item>
      <el-form-item label="用户头像">
        <el-input v-model="formData.userAvatar" :placeholder="isView ? '' : '请输入用户头像地址'" />
      </el-form-item>
      <el-form-item label="用户简介" prop="userProfile">
        <el-input
          v-model="formData.userProfile"
          :placeholder="isView ? '' : '请输入用户简介'"
          clearable
        />
      </el-form-item>
      <el-form-item label="用户角色" prop="userRole">
        <el-select
          v-model="formData.userRole"
          :placeholder="isView ? '' : '请选择用户角色'"
          clearable
        >
          <el-option label="普通用户" value="user" />
          <el-option label="管理员" value="admin" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer v-if="!isView">
      <div class="flex justify-center gap-[5%]">
        <el-button type="primary" @click="handleSubmit" size="large">确定</el-button>
        <el-button @click="handleClose" size="large">取消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage, ElLoading } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import {
  addUserUsingPost,
  getUserByIdUsingGet,
  adminUpdateUserUsingPost,
} from '@/api/userController'
import { useLoginUserStore } from '@/stores/useLoginUserStore'
import { uploadFileUsingPost } from '@/api/fileUploadController'

const title = ref('新增用户') // 弹窗标题
const isView = ref(false) // 是否为查看模式

const emit = defineEmits(['success']) // 定义成功事件, 供父组件调用

const visible = ref(false)
const formType = ref(0) // 0: 新增, 1: 修改, 2: 查看
const formRef = ref<FormInstance>()
const formData = ref<API.DriverVO>({})

// 登录用户信息，用于获取token
const loginUserStore = useLoginUserStore()

// 上传前验证
const beforeAvatarUpload = (file: File) => {
  // 验证文件类型
  const isImage = ['image/jpeg', 'image/png', 'image/jpg', 'image/gif'].includes(file.type)
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }

  // 验证文件大小（2MB）
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过2MB!')
    return false
  }

  return true
}

// 自定义上传请求
const uploadRequest = async (options: any) => {
  try {
    // 显示上传中
    const loading = ElLoading.service({
      lock: true,
      text: '上传中...',
      background: 'rgba(0, 0, 0, 0.7)',
    })

    // 调用API上传文件
    const res = await uploadFileUsingPost({}, options.file)

    // 关闭加载
    loading.close()

    // 处理响应
    if (res.data?.code === 0 && res.data.data) {
      // 上传成功，设置头像URL
      formData.value.driverAvatar = res.data.data
      ElMessage.success('头像上传成功')
    } else {
      // 上传失败
      ElMessage.error('头像上传失败: ' + (res.data?.message || '未知错误'))
    }
  } catch (error: any) {
    ElMessage.error('头像上传失败: ' + (error.message || '未知错误'))
  }
}

const rules = reactive<FormRules>({
  userAccount: [
    { required: true, message: '请输入用户账号', trigger: 'blur' },
    { min: 4, message: '账号长度不能小于4位', trigger: 'blur' },
  ],
  userRole: [{ required: true, message: '请选择用户角色', trigger: 'change' }],
})

// 重置表单
const resetForm = () => {
  formData.value = {} as API.DriverVO
  formRef.value?.resetFields()
}

// 关闭弹窗
const handleClose = () => {
  formRef.value?.resetFields()
  resetForm()
  visible.value = false
}

// 供父组件调用的打开方法
const open = async (type: number, userId?: string) => {
  // 默认打开初始化
  isView.value = false
  formType.value = 0
  title.value = '新增用户'
  resetForm()
  visible.value = true
  if (type === 0) return
  const res = await getUserByIdUsingGet({ id: userId })
  if (res.data?.code === 0 && res.data.data) {
    formData.value = res.data.data
  }
  // 编辑
  if (type === 1) {
    title.value = '编辑用户'
    formType.value = 1
  } else {
    // 设置查看状态，禁用表单
    isView.value = true
    formType.value = 2
    title.value = '用户信息'
  }
}

// 提交表单
const handleSubmit = async () => {
  // 查看状态不允许提交
  if (isView.value) {
    visible.value = false
    return
  }
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (formType.value === 1) {
          // 更新
          await adminUpdateUserUsingPost({
            id: formData.value.id,
            userName: formData.value.userName,
            userAvatar: formData.value.userAvatar,
            userProfile: formData.value.userProfile,
            userRole: formData.value.userRole,
          })
          emit('success', '修改成功')
        } else {
          // 新增
          await addUserUsingPost({
            userAccount: formData.value.userAccount,
            userName: formData.value.userName,
            userAvatar: formData.value.userAvatar,
            userProfile: formData.value.userProfile,
            userRole: formData.value.userRole,
          })
          emit('success', '新增成功')
        }
        handleClose()
      } catch (error) {
        ElMessage.error('操作失败, ' + error)
      } finally {
        resetForm()
      }
    }
  })
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
</style>
