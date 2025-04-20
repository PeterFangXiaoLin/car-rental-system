<template>
  <el-dialog v-model="visible" width="40%" @close="handleClose" destroy-on-close center>
    <template #header>
      <h3 class="title">新增车型</h3>
    </template>
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      size="large"
      label-width="120px"
      class="mt-8"
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
        <div class="avatar-uploader">
          <el-upload
            class="avatar-uploader-box"
            action="#"
            :before-upload="beforeAvatarUpload"
            :http-request="uploadRequest"
            :show-file-list="false"
          >
            <img v-if="formData.modelLogo" :src="formData.modelLogo" class="avatar-image" />
            <el-icon v-else class="avatar-uploader-icon">
              <Plus />
            </el-icon>
            <div v-if="!formData.modelLogo" class="upload-tip">点击上传车型图片</div>
          </el-upload>
        </div>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="flex justify-center gap-[5%]">
        <el-button type="primary" @click="handleSubmit" size="large">确定</el-button>
        <el-button @click="handleClose" size="large">取消</el-button>
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
import { listVehicleBrandUsingPost } from '@/api/vehicleBrandController.ts'
import { addVehicleModelUsingPost } from '@/api/vehicleModelController.ts'

const emit = defineEmits(['success']) // 定义成功事件, 供父组件调用

const visible = ref(false) //
const formRef = ref<FormInstance>()
const formData = ref<API.VehicleModelAddRequest>({})
const brandList = ref<API.VehicleBrandVO[]>([])

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

// 自定义上传请求 - 车辆图片
const uploadRequest = async (options: { file: File }) => {
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
    if (res.data?.code === 0 && res.data?.data) {
      // 上传成功，设置头像URL
      formData.value.modelLogo = res.data.data
      ElMessage.success('头像上传成功')
    } else {
      // 上传失败
      ElMessage.error('头像上传失败: ' + (res.data?.message || '未知错误'))
    }
  } catch (error) {
    ElMessage.error('头像上传失败: ' + (error?.message || '未知错误'))
  }
}

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
const open = () => {
  resetForm()
  visible.value = true
  fetchBrandList()
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

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  try {
    // 先验证表单
    const valid = await formRef.value.validate().catch((err) => {
      ElMessage.error('表单验证错误:' + err)
      return false
    })

    if (valid) {
      // 显示加载状态
      const loading = ElLoading.service({
        lock: true,
        text: '提交中...',
        background: 'rgba(0, 0, 0, 0.7)',
      })

      try {
        // 新增
        const res = await addVehicleModelUsingPost(formData.value)

        if (res.data?.code === 0) {
          ElMessage.success('新增车型成功')
          emit('success', '新增成功')
          handleClose()
        } else {
          ElMessage.error('新增失败: ' + (res.data?.message || '未知错误'))
        }
      } catch (error) {
        console.error('提交请求错误:', error)
        ElMessage.error('操作失败: ' + (error instanceof Error ? error.message : String(error)))
      } finally {
        loading.close()
      }
    } else {
      ElMessage.warning('请完善表单信息')
    }
  } catch (validationError) {
    console.error('表单验证发生异常:', validationError)
    ElMessage.error('表单验证失败')
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
