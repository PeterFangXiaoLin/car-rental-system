<template>
  <div class="user-info-head" @click="editCropper">
    <el-avatar :size="120" :src="loginUserStore.loginUser?.userAvatar" class="avatar-img" />
    <!-- 头像裁剪弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      title="修改头像"
      width="800px"
      append-to-body
      :z-index="2000"
      destroy-on-close
      @opened="handleDialogOpened"
      @close="handleDialogClose"
    >
      <el-row>
        <el-col :xs="24" :md="12" :style="{ height: '350px' }">
          <vue-cropper
            ref="cropperRef"
            :img="cropperOptions.img"
            :info="true"
            :auto-crop="true"
            :auto-crop-width="200"
            :auto-crop-height="200"
            :fixed-box="true"
            :output-type="'png'"
            @real-time="handleRealTime"
            v-if="cropperVisible"
          />
        </el-col>
        <el-col :xs="24" :md="12" :style="{ height: '350px' }">
          <div class="avatar-upload-preview">
            <img :src="previews.url" :style="previews.img" />
          </div>
        </el-col>
      </el-row>

      <div class="cropper-control mt-4">
        <el-row :gutter="10" justify="center">
          <el-col :span="6">
            <el-upload
              action="#"
              :show-file-list="false"
              :before-upload="handleBeforeUpload"
              :http-request="uploadRequest"
            >
              <el-button>
                <el-icon><Upload /></el-icon>
                选择图片
              </el-button>
            </el-upload>
          </el-col>
          <el-col :span="12">
            <el-button-group>
              <el-button @click="handleScale(1)">
                <el-icon><ZoomIn /></el-icon>
              </el-button>
              <el-button @click="handleScale(-1)">
                <el-icon><ZoomOut /></el-icon>
              </el-button>
              <el-button @click="rotateLeft()">
                <el-icon><RefreshLeft /></el-icon>
              </el-button>
              <el-button @click="rotateRight()">
                <el-icon><RefreshRight /></el-icon>
              </el-button>
            </el-button-group>
          </el-col>
          <el-col :span="6">
            <el-button type="primary" @click="handleUpload">
              <el-icon><Check /></el-icon>
              提交
            </el-button>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useLoginUserStore } from '@/stores/useLoginUserStore'
import { VueCropper } from 'vue-cropper'
import 'vue-cropper/dist/index.css'
import { Upload, ZoomIn, ZoomOut, RefreshLeft, RefreshRight, Check } from '@element-plus/icons-vue'
import { updateAvatarUsingPost, getLoginUserUsingGet } from '@/api/userController'

const loginUserStore = useLoginUserStore()

const dialogVisible = ref(false)
const cropperVisible = ref(false)
const cropperRef = ref()

// 定义预览数据的接口
interface PreviewData {
  url?: string
  img?: {
    width?: string
    height?: string
    transform?: string
  }
}

const previews = ref<PreviewData>({})

const cropperOptions = reactive({
  img: loginUserStore.loginUser?.userAvatar,
  filename: 'avatar.png',
})

// 打开裁剪弹窗
const editCropper = () => {
  dialogVisible.value = true
}

// 弹窗打开回调
const handleDialogOpened = () => {
  cropperVisible.value = true
}

// 弹窗关闭回调
const handleDialogClose = () => {
  cropperVisible.value = false
  cropperOptions.img = loginUserStore.loginUser?.userAvatar
}

// 实时预览
const handleRealTime = (data: PreviewData) => {
  previews.value = data
}

// 缩放
const handleScale = (num: number) => {
  cropperRef.value?.changeScale(num || 1)
}

// 向左旋转
const rotateLeft = () => {
  cropperRef.value?.rotateLeft()
}

// 向右旋转
const rotateRight = () => {
  cropperRef.value?.rotateRight()
}

// 上传前校验
const handleBeforeUpload = (file: File) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }

  const reader = new FileReader()
  reader.readAsDataURL(file)
  reader.onload = () => {
    cropperOptions.img = reader.result as string
    cropperOptions.filename = file.name
  }
  return false
}

// 覆盖默认的上传行为
const uploadRequest = () => {
  // 阻止默认上传
}

// 提交头像
const handleUpload = () => {
  cropperRef.value?.getCropBlob(async (blob: Blob) => {
    try {
      // 创建File对象
      const file = new File([blob], cropperOptions.filename, { type: 'image/png' })

      // 上传头像
      const res = await updateAvatarUsingPost({}, file)

      if (res.data?.code === 0) {
        ElMessage.success('修改成功')
        dialogVisible.value = false
        // 重新获取用户信息
        const userRes = await getLoginUserUsingGet()
        if (userRes.data?.code === 0 && userRes.data.data) {
          loginUserStore.setLoginUser(userRes.data.data)
        }
      } else {
        ElMessage.error('修改失败：' + (res.data?.message ? res.data.message : '未知错误'))
      }
    } catch (error) {
      ElMessage.error('上传失败，请稍后重试')
    }
  })
}

// 组件挂载时初始化头像
onMounted(() => {
  cropperOptions.img = loginUserStore.loginUser?.userAvatar
})
</script>

<style scoped>
.user-info-head {
  position: relative;
  display: inline-block;
  cursor: pointer;
  width: 120px;
  height: 120px;
  border-radius: 50%;
  overflow: hidden;
}

.user-info-head:hover::after {
  content: '+';
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  color: #fff;
  background: rgba(0, 0, 0, 0.5);
  font-size: 24px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
}

.avatar-img {
  width: 100%;
  height: 100%;
  display: block;
}

.avatar-upload-preview {
  position: relative;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 200px;
  height: 200px;
  border-radius: 50%;
  box-shadow: 0 0 4px #ccc;
  overflow: hidden;
}

.cropper-control {
  padding: 0 20px;
}

/* 确保弹窗在最顶层 */
:deep(.el-overlay) {
  z-index: 2000 !important;
}

:deep(.el-image__preview) {
  z-index: 2100 !important;
}

:deep(.el-image-viewer__wrapper) {
  z-index: 2100 !important;
}

:deep(.el-image-viewer__mask) {
  z-index: 2100 !important;
}
</style>
