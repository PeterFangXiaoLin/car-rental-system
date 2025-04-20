<template>
  <el-dialog v-model="visible" width="40%" @close="handleClose" destroy-on-close center>
    <template #header>
      <h3 class="title">修改品牌</h3>
    </template>
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      size="large"
      label-width="120px"
      class="mt-8"
    >
      <el-form-item label="品牌名称" prop="brandName">
        <el-input v-model="formData.brandName" placeholder="请输入品牌名称" clearable />
      </el-form-item>
      <el-form-item label="品牌Logo" prop="brandLogo">
        <div class="avatar-uploader">
          <el-upload
            class="avatar-uploader-box"
            action="#"
            :before-upload="beforeAvatarUpload"
            :http-request="uploadRequest"
            :show-file-list="false"
          >
            <img v-if="formData.brandLogo" :src="formData.brandLogo" class="avatar-image" />
            <el-icon v-else class="avatar-uploader-icon">
              <Plus />
            </el-icon>
            <div v-if="!formData.brandLogo" class="upload-tip">点击上传车辆图片</div>
          </el-upload>
        </div>
      </el-form-item>
      <el-form-item label="首字母" prop="firstLetter">
        <el-input v-model="formData.firstLetter" placeholder="请选择首字母" clearable :disabled="true" />
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
import {
  addVehicleBrandUsingPost,
  getVehicleBrandByIdUsingGet
} from '@/api/vehicleBrandController.ts'

const emit = defineEmits(['success']) // 定义成功事件, 供父组件调用

const visible = ref(false) //
const formRef = ref<FormInstance>()
const formData = ref<API.VehicleBrandUpdateRequest>({})

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
      formData.value.brandLogo = res.data.data
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
        const res = await addVehicleBrandUsingPost(formData.value)

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

// 获取中文拼音首字母
const getChineseFirstLetter = (str: string) => {
  if (!str) return ''

  // 简单的中文汉字区间与拼音首字母映射
  const pinyinMap = [
    { reg: /^[\u4e00-\u4eff]/i, letter: 'A' },
    { reg: /^[\u4f00-\u52ff]/i, letter: 'B' },
    { reg: /^[\u5300-\u53ff]/i, letter: 'C' },
    { reg: /^[\u5400-\u57ff]/i, letter: 'D' },
    { reg: /^[\u5800-\u58ff]/i, letter: 'E' },
    { reg: /^[\u5900-\u5eff]/i, letter: 'F' },
    { reg: /^[\u5f00-\u61ff]/i, letter: 'G' },
    { reg: /^[\u6200-\u64ff]/i, letter: 'H' },
    { reg: /^[\u6500-\u66ff]/i, letter: 'J' },
    { reg: /^[\u6700-\u6bff]/i, letter: 'K' },
    { reg: /^[\u6c00-\u70ff]/i, letter: 'L' },
    { reg: /^[\u7100-\u75ff]/i, letter: 'M' },
    { reg: /^[\u7600-\u7aff]/i, letter: 'N' },
    { reg: /^[\u7b00-\u7fff]/i, letter: 'O' },
    { reg: /^[\u8000-\u84ff]/i, letter: 'P' },
    { reg: /^[\u8500-\u8aff]/i, letter: 'Q' },
    { reg: /^[\u8b00-\u8cff]/i, letter: 'R' },
    { reg: /^[\u8d00-\u92ff]/i, letter: 'S' },
    { reg: /^[\u9300-\u93ff]/i, letter: 'T' },
    { reg: /^[\u9400-\u94ff]/i, letter: 'W' },
    { reg: /^[\u9500-\u97ff]/i, letter: 'X' },
    { reg: /^[\u9800-\u9bff]/i, letter: 'Y' },
    { reg: /^[\u9c00-\u9fff]/i, letter: 'Z' },
  ]

  const firstChar = str.charAt(0)

  // 检查是否为英文字母
  if (/^[a-zA-Z]/.test(firstChar)) {
    return firstChar.toUpperCase()
  }

  // 检查是否为中文字符
  for (const item of pinyinMap) {
    if (item.reg.test(firstChar)) {
      return item.letter
    }
  }

  // 默认返回A
  return '#'
}

watch(
  () => formData.value.brandName,
  (newVal) => {
    if (newVal) {
      formData.value.firstLetter = getChineseFirstLetter(newVal)
    }
  },
)

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
