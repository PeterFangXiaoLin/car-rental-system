<template>
  <el-dialog
    v-model="dialogVisible"
    :title="isAddComment ? '追加评论' : '评价订单'"
    width="50%"
    destroy-on-close
  >
    <el-form
      ref="formRef"
      :model="commentForm"
      :rules="rules"
      label-width="100px"
      class="comment-form"
      size="large"
    >
      <el-form-item label="车辆评分" prop="vehicleRating">
        <el-rate v-model="commentForm.vehicleRating" show-score :colors="colors" />
      </el-form-item>

      <el-form-item label="司机评分" prop="driverRating" v-if="hasDriver">
        <el-rate v-model="commentForm.driverRating" show-score :colors="colors" />
      </el-form-item>

      <el-form-item label="评价内容" prop="content">
        <el-input
          v-model="commentForm.content"
          type="textarea"
          :rows="4"
          placeholder="请输入您的评价内容..."
        />
      </el-form-item>

      <el-form-item label="上传图片">
        <el-upload
          action="#"
          list-type="picture-card"
          :auto-upload="false"
          :limit="3"
          :on-change="handleImageChange"
          :on-remove="handleImageRemove"
          :file-list="fileList"
        >
          <el-icon><plus /></el-icon>
        </el-upload>
        <div v-if="uploading" class="upload-loading">
          <el-progress type="circle" :percentage="uploadProgress" />
        </div>
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitComment" :loading="submitLoading"
          >提交评价</el-button
        >
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getRentalOrderUsingGet } from '@/api/rentalOrderController'
import { addCommentUsingPost, updateCommentUsingPost } from '@/api/commentController'
import { uploadFileUsingPost } from '@/api/fileUploadController'

const dialogVisible = ref(false)
const formRef = ref()
const orderId = ref<string>('')
const isAddComment = ref(false)
const existingCommentId = ref<number | null>(null)
const orderDetail = ref<API.RentalOrderVO | null>(null)
const hasDriver = computed(() => orderDetail.value?.needDriver === 1)
const fileList = ref<{ uid: string; name: string; url?: string }[]>([])
const uploading = ref(false)
const uploadProgress = ref(0)
const submitLoading = ref(false)

// 评分颜色
const colors = ['#99A9BF', '#F7BA2A', '#FF9900']

// 评价表单
const commentForm = reactive({
  vehicleRating: 5,
  driverRating: 5,
  content: '',
  images: [] as string[],
})

// 表单验证规则
const rules = {
  vehicleRating: [{ required: true, message: '请对车辆进行评分', trigger: 'change' }],
  driverRating: [{ required: true, message: '请对司机进行评分', trigger: 'change' }],
  content: [
    { required: true, message: '请输入评价内容', trigger: 'blur' },
    { min: 5, max: 200, message: '评价内容长度在5到200个字符之间', trigger: 'blur' },
  ],
}

// 处理图片变化
const handleImageChange = async (file: { uid: string; raw: File; name: string }) => {
  if (!file.raw) return

  try {
    uploading.value = true
    uploadProgress.value = 30

    // 上传文件到服务器
    const res = await uploadFileUsingPost({}, file.raw)

    uploadProgress.value = 100

    if (res.data.code === 0 && res.data.data) {
      // 存储返回的URL
      commentForm.images.push(res.data.data)
      ElMessage.success('图片上传成功')
    } else {
      ElMessage.error((res && res.data && res.data.message) || '图片上传失败')
      // 从文件列表中移除上传失败的文件
      const index = fileList.value.findIndex((item) => item.uid === file.uid)
      if (index !== -1) {
        fileList.value.splice(index, 1)
      }
    }
  } catch (error) {
    ElMessage.error('图片上传失败: ' + error)
    // 从文件列表中移除上传失败的文件
    const index = fileList.value.findIndex((item) => item.uid === file.uid)
    if (index !== -1) {
      fileList.value.splice(index, 1)
    }
  } finally {
    uploading.value = false
    uploadProgress.value = 0
  }
}

// 处理图片移除
const handleImageRemove = (file: { uid: string }) => {
  const index = commentForm.images.findIndex(
    (url, idx) => idx === fileList.value.findIndex((item) => item.uid === file.uid),
  )
  if (index !== -1) {
    commentForm.images.splice(index, 1)
  }
}

// 加载订单详情
const loadOrderDetail = async (id: string) => {
  try {
    const res = await getRentalOrderUsingGet({ orderId: id })
    if (res.data?.code === 0 && res.data.data) {
      orderDetail.value = res.data.data
    } else {
      ElMessage.error('获取订单详情失败')
    }
  } catch (error) {
    ElMessage.error('获取订单详情失败' + error)
  }
}

// 提交评价
const submitComment = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid: boolean) => {
    if (!valid) return

    submitLoading.value = true

    try {
      const vehicleId = orderDetail.value?.vehicleId
      const driverId = orderDetail.value?.driverId

      if (!vehicleId) {
        ElMessage.error('车辆信息缺失')
        return
      }

      if (isAddComment.value && existingCommentId.value) {
        // 追加评论，更新现有评论
        const res = await updateCommentUsingPost({
          id: existingCommentId.value,
          content: commentForm.content,
          vehicleRating: commentForm.vehicleRating,
          driverRating: hasDriver.value ? commentForm.driverRating : undefined,
          images: commentForm.images.length > 0 ? commentForm.images.join(',') : undefined,
        })

        if (res.data?.code === 0 && res.data.data) {
          ElMessage.success('评论更新成功')
          dialogVisible.value = false
          // 触发刷新事件
          emit('refresh')
        } else {
          ElMessage.error(res.data?.message || '评论更新失败')
        }
      } else {
        // 新增评论
        const res = await addCommentUsingPost({
          orderId: orderId.value,
          vehicleId,
          driverId: hasDriver.value ? driverId : undefined,
          content: commentForm.content,
          vehicleRating: commentForm.vehicleRating,
          driverRating: hasDriver.value ? commentForm.driverRating : undefined,
          images: commentForm.images.length > 0 ? commentForm.images.join(',') : undefined,
        })

        if (res.data?.code === 0 && res.data.data) {
          ElMessage.success('评价提交成功')
          dialogVisible.value = false
          // 触发刷新事件
          emit('refresh')
        } else {
          ElMessage.error(res.data?.message || '评价提交失败')
        }
      }
    } catch (error) {
      console.error('提交评价失败', error)
      ElMessage.error('提交评价失败')
    } finally {
      submitLoading.value = false
    }
  })
}

// 打开评价对话框
const open = async (id: string, commentId?: number) => {
  orderId.value = id
  isAddComment.value = !!commentId
  existingCommentId.value = commentId || null

  // 重置表单
  commentForm.vehicleRating = 5
  commentForm.driverRating = 5
  commentForm.content = ''
  commentForm.images = []
  fileList.value = []

  // 加载订单详情
  await loadOrderDetail(id)

  dialogVisible.value = true
}

// 定义对外事件
const emit = defineEmits(['refresh'])

// 对外暴露方法
defineExpose({
  open,
})
</script>

<style scoped>
.comment-form {
  margin: 20px 0;
}

:deep(.el-rate) {
  margin-top: 0;
  display: inline-flex;
  align-items: center;
  height: 32px;
}

:deep(.el-form-item__label) {
  line-height: 32px;
  height: 32px;
  display: flex;
  align-items: center;
}

:deep(.el-form-item__content) {
  line-height: 32px;
  display: flex;
  align-items: center;
}

:deep(.el-textarea__inner) {
  font-family: inherit;
}

:deep(.el-upload--picture-card) {
  width: 100px;
  height: 100px;
  line-height: 100px;
}

.upload-loading {
  display: flex;
  justify-content: center;
  margin-top: 10px;
}
</style>
