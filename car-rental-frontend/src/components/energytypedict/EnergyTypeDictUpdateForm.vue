<template>
  <el-dialog v-model="visible" width="40%" @close="handleClose" destroy-on-close center>
    <template #header>
      <h3 class="title">修改能源类型</h3>
    </template>
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      size="large"
      label-width="120px"
      class="mt-8"
    >
      <el-form-item label="能源类型名称" prop="modelName">
        <el-input v-model="formData.typeName" placeholder="请输入能源类型名称" clearable />
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
import { reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElLoading, ElMessage } from 'element-plus'
import { updateVehicleModelUsingPost } from '@/api/vehicleModelController.ts'
import { getEnergyTypeDictByIdUsingGet } from '@/api/energyTypeDictController.ts'

const emit = defineEmits(['success']) // 定义成功事件, 供父组件调用

const visible = ref(false) //
const formRef = ref<FormInstance>()
const formData = ref<API.EnergyTypeDictUpdateRequest>({})

const rules = reactive<FormRules>({
  typeName: [{ required: true, message: '请输入能源类型名称', trigger: 'blur' }],
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
const open = (id: string) => {
  resetForm()
  visible.value = true
  fetchDetail(id)
}

// 获取品牌信息
const fetchDetail = async (id: string) => {
  try {
    const res = await getEnergyTypeDictByIdUsingGet({ id: id })
    if (res.data?.code === 0 && res.data?.data) {
      formData.value = res.data.data
    } else {
      ElMessage.error('获取车型信息失败:' + (res.data?.message || '未知错误'))
    }
  } catch (error) {
    ElMessage.error('获取车型信息失败:' + error)
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
        const res = await updateVehicleModelUsingPost(formData.value)

        if (res.data?.code === 0) {
          ElMessage.success('新增品牌成功')
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
