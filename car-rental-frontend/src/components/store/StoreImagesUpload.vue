<template>
  <div class="upload-container">
    <el-upload
      v-model:file-list="fileList"
      :http-request="customUpload"
      list-type="picture-card"
      :on-preview="handlePictureCardPreview"
      :on-remove="handleRemove"
      :before-upload="beforeUpload"
      multiple
    >
      <el-icon><Plus /></el-icon>
      <template #tip>
        <div class="el-upload__tip">只能上传jpg/png文件，且不超过5MB</div>
      </template>
    </el-upload>

    <el-dialog v-model="dialogVisible" title="预览">
      <img w-full :src="dialogImageUrl" alt="Preview Image" style="max-width: 100%" />
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import type { UploadProps, UploadUserFile, UploadRequestOptions } from 'element-plus'
import { ElMessage } from 'element-plus'
import { uploadStoreImageUsingPost } from '@/api/storeController'

const props = defineProps({
  storeId: {
    type: Number,
    default: undefined,
  },
  initialImages: {
    type: Array as () => string[],
    default: () => [],
  },
})

const emit = defineEmits(['update:images'])

// 初始化文件列表
const fileList = ref<UploadUserFile[]>(
  props.initialImages.map((url, index) => ({
    name: `image-${index + 1}`,
    url,
  })),
)

const dialogImageUrl = ref('')
const dialogVisible = ref(false)

// 自定义上传方法
const customUpload = async (options: UploadRequestOptions) => {
  const { file, onSuccess, onError } = options

  // 创建FormData对象
  const formData = new FormData()
  formData.append('files', file as File)

  // 如果有storeId，添加到请求中
  if (props.storeId) {
    formData.append('storeId', props.storeId.toString())
  }

  try {
    // 调用后端接口上传图片
    const response = await uploadStoreImageUsingPost({ files: [file] })

    // 成功处理
    if (response.data?.code === 0) {
      const urls = response.data?.data || []
      onSuccess(response)

      // 更新URL
      updateImageList(options.file as File, urls[0])
      ElMessage.success('上传成功')
    } else {
      // 使用类型断言处理错误
      onError(new Error(response.data?.message || '上传失败') as never)
      ElMessage.error(response.data?.message || '上传失败')
    }
  } catch (error) {
    // 使用类型断言处理错误
    onError(error as never)
    ElMessage.error('上传失败')
  }
}

// 更新图片列表
const updateImageList = (file: File, url: string) => {
  // 找到当前上传的文件并更新URL
  const uploadFile = fileList.value.find((item) => item.name === file.name && !item.url)

  if (uploadFile) {
    uploadFile.url = url
  }

  // 更新父组件的图片列表
  const currentImages = fileList.value.map((file) => file.url || '').filter((url) => url)
  emit('update:images', currentImages)
}

// 移除图片
const handleRemove: UploadProps['onRemove'] = (uploadFile, uploadFiles) => {
  const currentImages = uploadFiles.map((file) => file.url || '').filter((url) => url)
  emit('update:images', currentImages)
}

// 预览图片
const handlePictureCardPreview: UploadProps['onPreview'] = (uploadFile) => {
  dialogImageUrl.value = uploadFile.url!
  dialogVisible.value = true
}

// 上传前检查
const beforeUpload: UploadProps['beforeUpload'] = (file) => {
  // 检查文件类型
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }

  // 检查文件大小
  const isLt5M = file.size / 1024 / 1024 < 5
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过5MB!')
    return false
  }

  return true
}
</script>

<style scoped>
.upload-container {
  margin: 20px 0;
}

:deep(.el-upload--picture-card) {
  --el-upload-picture-card-size: 100px;
}

:deep(.el-upload-list--picture-card .el-upload-list__item) {
  --el-upload-list-picture-card-size: 100px;
}

:deep(.el-upload__tip) {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
}
</style>
