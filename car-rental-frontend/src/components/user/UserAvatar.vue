<template>
  <div class="user-info-head" @click="editCropper">
    <img :src="options.img" title="点击上传头像" class="img-circle img-lg" />
    <!-- 头像裁剪弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      title="修改头像"
      width="800px"
      append-to-body
      @opened="handleDialogOpened"
      @close="handleDialogClose"
    >
      <el-row>
        <el-col :xs="24" :md="12" :style="{ height: '350px' }">
          <vue-cropper
            ref="cropperRef"
            :img="options.img"
            :info="true"
            :auto-crop="options.autoCrop"
            :auto-crop-width="options.autoCropWidth"
            :auto-crop-height="options.autoCropHeight"
            :fixed-box="options.fixedBox"
            :output-type="options.outputType"
            @real-time="handleRealTime"
            v-if="cropperVisible"
          />
        </el-col>
        <el-col :xs="24" :md="12" :style="{ height: '350px' }">
          <div class="avatar-upload-preview">
            <img :src="options.img" :style="options.previews.img" />
          </div>
        </el-col>
      </el-row>
      <br />
      <el-row>
        <el-col :lg="2" :md="2">
          <el-upload
            action="#"
            :http-request="requestUpload"
            :show-file-list="false"
            :before-upload="handleBeforeUpload"
          >
            <el-button>
              选择
              <el-icon class="el-icon--right"><Upload /></el-icon>
            </el-button>
          </el-upload>
        </el-col>
        <el-col :lg="{ span: 1, offset: 2 }" :md="2">
          <el-button @click="handleScale(1)">
            <el-icon><Plus /></el-icon>
          </el-button>
        </el-col>
        <el-col :lg="{ span: 1, offset: 1 }" :md="2">
          <el-button @click="handleScale(-1)">
            <el-icon><Minus /></el-icon>
          </el-button>
        </el-col>
        <el-col :lg="{ span: 1, offset: 1 }" :md="2">
          <el-button @click="rotateLeft()">
            <el-icon><RefreshLeft /></el-icon>
          </el-button>
        </el-col>
        <el-col :lg="{ span: 1, offset: 1 }" :md="2">
          <el-button @click="rotateRight()">
            <el-icon><RefreshRight /></el-icon>
          </el-button>
        </el-col>
        <el-col :lg="{ span: 2, offset: 6 }" :md="2">
          <el-button type="primary" @click="handleUpload">提 交</el-button>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useLoginUserStore } from '@/stores/useLoginUserStore'
import { Upload, Plus, Minus, RefreshLeft, RefreshRight } from '@element-plus/icons-vue'
import { updateAvatarUsingPost, getLoginUserUsingGet } from '@/api/userController'

const loginUserStore = useLoginUserStore()
const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

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

// 图片裁剪数据
const options = reactive({
  img: loginUserStore.loginUser?.userAvatar || defaultAvatar, // 裁剪图片的地址
  autoCrop: true, // 是否默认生成截图框
  autoCropWidth: 200, // 默认生成截图框宽度
  autoCropHeight: 200, // 默认生成截图框高度
  fixedBox: true, // 固定截图框大小 不允许改变
  outputType: 'png', // 默认生成截图为PNG格式
  filename: 'avatar', // 文件名称
  previews: { img: {} } as { img: Record<string, string> }, //预览数据
})

// 实时预览
const handleRealTime = (data: PreviewData) => {
  if (data && data.img) {
    options.previews.img = data.img as Record<string, string>
  }
}

// 初始化用户头像
const initUserAvatar = () => {
  options.img = loginUserStore.loginUser?.userAvatar || defaultAvatar
}

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
  options.img = loginUserStore.loginUser?.userAvatar || defaultAvatar
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

/** 覆盖默认上传行为 */
function requestUpload() {}

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
    options.img = reader.result as string
    options.filename = file.name
  }
  return false
}

// 提交头像
const handleUpload = () => {
  cropperRef.value?.getCropBlob(async (blob: Blob) => {
    try {
      // 创建File对象
      const file = new File([blob], options.filename, { type: 'image/png' })

      // 上传头像
      const loginUser = loginUserStore.loginUser
      const params = loginUser?.id ? { id: loginUser.id } : {}

      const { data: res } = await updateAvatarUsingPost(params, file)

      if (res && res.code === 0) {
        ElMessage.success('头像修改成功')
        dialogVisible.value = false

        // 重新获取用户信息
        const { data: userRes } = await getLoginUserUsingGet()
        if (userRes && userRes.code === 0 && userRes.data) {
          loginUserStore.setLoginUser(userRes.data)
        }
      } else {
        ElMessage.error('修改失败：' + (res && res.message ? res.message : '未知错误'))
      }
    } catch (error) {
      console.error('上传头像失败:', error)
      ElMessage.error('上传失败，请稍后重试')
    }
  })
}

// 组件挂载时初始化头像
onMounted(() => {
  initUserAvatar()
})
</script>

<style scoped>
.user-info-head {
  position: relative;
  display: inline-block;
  height: 120px;
}

.user-info-head:hover:after {
  content: '+';
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  color: #eee;
  background: rgba(0, 0, 0, 0.5);
  font-size: 24px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  cursor: pointer;
  line-height: 110px;
  border-radius: 50%;
}

/* image */
.img-circle {
  border-radius: 50%;
}

.img-lg {
  width: 120px;
  height: 120px;
}

.avatar-upload-preview {
  position: absolute;
  top: 50%;
  transform: translate(50%, -50%);
  width: 200px;
  height: 200px;
  border-radius: 50%;
  box-shadow: 0 0 4px #ccc;
  overflow: hidden;
}
</style>
