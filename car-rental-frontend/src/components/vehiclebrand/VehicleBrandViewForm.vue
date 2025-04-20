<template>
  <el-dialog v-model="visible" width="40%" @close="handleClose" destroy-on-close center>
    <template #header>
      <h3 class="title">品牌详情</h3>
    </template>
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      size="large"
      label-width="120px"
      class="mt-8"
      :disabled="true"
    >
      <el-form-item label="品牌名称" prop="brandName">
        <el-input v-model="formData.brandName" clearable />
      </el-form-item>
      <el-form-item label="品牌Logo" prop="brandLogo">
        <el-image :src="formData.brandLogo" style="width: 150px; height: 150px; object-fit: cover" />
      </el-form-item>
      <el-form-item label="首字母" prop="firstLetter">
        <el-input v-model="formData.firstLetter" clearable :disabled="true" />
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
import { reactive, ref, watch } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElLoading, ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { uploadFileUsingPost } from '@/api/fileUploadController'
import {
  addVehicleBrandUsingPost,
  getVehicleBrandByIdUsingGet
} from '@/api/vehicleBrandController.ts'

const visible = ref(false) //
const formRef = ref<FormInstance>()
const formData = ref<API.VehicleBrandVO>({})

const rules = reactive<FormRules>({
  brandName: [{ required: true, message: '请输入品牌名称', trigger: 'blur' }],
  brandLogo: [{ required: true, message: '请上传品牌Logo', trigger: 'change' }],
  firstLetter: [{ required: true, message: '请输入首字母', trigger: 'blur' }],
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
const open = (brandId: string) => {
  resetForm()
  visible.value = true
  fetchBrandDetail(brandId)
}

// 获取品牌信息
const fetchBrandDetail = async (brandId: string) => {
  try {
    const res = await getVehicleBrandByIdUsingGet({id: brandId})
    if (res.data?.code === 0 && res.data?.data) {
      formData.value = res.data.data
    } else {
      ElMessage.error('获取品牌信息失败:'+ (res.data?.message || '未知错误'))
    }
  } catch (error) {
    ElMessage.error('获取品牌信息失败:'+ error)
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
