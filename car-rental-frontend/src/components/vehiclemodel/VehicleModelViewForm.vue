<template>
  <el-dialog v-model="visible" width="40%" @close="handleClose" destroy-on-close center>
    <template #header>
      <h3 class="title">车型详情</h3>
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
      <el-form-item label="品牌" prop="brandId">
        <el-select v-model="formData.brandId" placeholder="请选择品牌" clearable>
          <el-option
            v-for="item in brandList"
            :key="item.id"
            :label="item.brandName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="车型名称" prop="modelName">
        <el-input v-model="formData.modelName" placeholder="请输入车型名称" clearable />
      </el-form-item>
      <el-form-item label="车型Logo" prop="modelLogo">
        <el-image
          :src="formData.modelLogo"
          style="width: 150px; height: 150px; object-fit: cover"
        />
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
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { getVehicleModelByIdUsingGet } from '@/api/vehicleModelController.ts'
import { listVehicleBrandUsingPost } from '@/api/vehicleBrandController.ts'

const visible = ref(false) //
const formRef = ref<FormInstance>()
const formData = ref<API.VehicleModelVO>({})
const brandList = ref<API.VehicleBrandVO[]>([])

const rules = reactive<FormRules>({
  brandId: [{ required: true, message: '请选择品牌', trigger: 'change' }],
  modelName: [{ required: true, message: '请输入车型名称', trigger: 'blur' }],
  modelLogo: [{ required: true, message: '请上传车型Logo', trigger: 'change' }],
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
const open = (modelId: string) => {
  resetForm()
  visible.value = true
  fetchBrandList()
  fetchModelDetail(modelId)
}

// 获取品牌信息
const fetchModelDetail = async (modelId: string) => {
  try {
    const res = await getVehicleModelByIdUsingGet({ id: modelId })
    if (res.data?.code === 0 && res.data?.data) {
      formData.value = res.data.data
    } else {
      ElMessage.error('获取车型信息失败:' + (res.data?.message || '未知错误'))
    }
  } catch (error) {
    ElMessage.error('获取车型信息失败:' + error)
  }
}

// 获取品牌
const fetchBrandList = async () => {
  try {
    const res = await listVehicleBrandUsingPost()
    if (res.data?.code === 0 && res.data?.data) {
      brandList.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('加载品牌列表失败:' + error)
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
