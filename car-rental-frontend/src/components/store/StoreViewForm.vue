<template>
  <el-dialog v-model="visible" width="40%" @close="handleClose" destroy-on-close center>
    <template #header>
      <h3 class="title">查看门店</h3>
    </template>
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      size="large"
      label-width="120px"
      class="mt-8"
      disabled
    >
      <el-form-item label="门店名称" prop="storeName">
        <el-input v-model="formData.storeName" placeholder="请输入门店名称" clearable />
      </el-form-item>
      <el-form-item label="门店地址" prop="address">
        <SelectPosition v-model:model-value="formData.address" @position-selected="updateAddress" />
      </el-form-item>
      <el-form-item label="门店电话" prop="mobile">
        <el-input v-model="formData.mobile" placeholder="请输入门店电话" clearable />
      </el-form-item>
      <el-form-item label="门店照片" prop="images">
        <div class="image-list-container">
          <el-upload
            v-model:file-list="fileList"
            class="image-uploader"
            action="#"
            list-type="picture-card"
            :before-upload="beforeImageUpload"
            :http-request="uploadImageRequest"
            :on-remove="handleImageRemove"
            multiple
          >
            <el-icon>
              <Plus />
            </el-icon>
          </el-upload>
        </div>
      </el-form-item>
      <el-form-item label="开始营业时间">
        <el-time-picker
          v-model="formData.openTime"
          placeholder="请选择开始营业时间"
          value-format="HH:mm"
        />
      </el-form-item>
      <el-form-item label="结束营业时间">
        <el-time-picker
          v-model="formData.closeTime"
          placeholder="请选择结束营业时间"
          value-format="HH:mm"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="flex justify-center gap-[5%]">
        <el-button @click="handleClose" size="large">返回</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { reactive, ref, watch } from 'vue'
import type { FormInstance, FormRules, UploadUserFile } from 'element-plus'
import { ElLoading, ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { uploadFileUsingPost } from '@/api/fileUploadController'
import { getStoreUsingGet, updateStoreUsingPost } from '@/api/storeController.ts'
import SelectPosition from '@/components/SelectPosition.vue'

interface UploadResponse {
  data?: {
    code?: number
    data?: string
    message?: string
  }
}

interface Position {
  lng: number
  lat: number
  cityName: string
}

const emit = defineEmits(['success']) // 定义成功事件, 供父组件调用

const visible = ref(false)
const formRef = ref<FormInstance>()
const formData = ref<API.StoreUpdateRequest>({
  images: '',
})

// 文件列表
const fileList = ref<UploadUserFile[]>([])
// 上传的图片URL列表
const imageUrls = ref<string[]>([])

// 处理图片删除
const handleImageRemove = (file: UploadUserFile) => {
  // 如果删除的是已有的图片（有URL的图片）
  if (file.url) {
    // 从imageUrls中移除该URL
    const index = imageUrls.value.indexOf(file.url)
    if (index !== -1) {
      imageUrls.value.splice(index, 1)
      // 更新表单数据中的images字段
      formData.value.images = imageUrls.value.join(',')
    }
  }
}

// 上传前验证
const beforeImageUpload = (file: File) => {
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

// 自定义上传请求 - 门店照片
const uploadImageRequest = async (options: { file: File }) => {
  try {
    // 显示上传中
    const loading = ElLoading.service({
      lock: true,
      text: '上传中...',
      background: 'rgba(0, 0, 0, 0.7)',
    })

    // 调用API上传文件
    const res = (await uploadFileUsingPost({}, options.file)) as UploadResponse

    // 关闭加载
    loading.close()

    // 处理响应
    if (res.data?.code === 0 && res.data?.data) {
      // 上传成功，添加到图片URL列表
      imageUrls.value.push(res.data.data)
      // 更新表单数据中的images字段，使用逗号拼接多个URL
      formData.value.images = imageUrls.value.join(',')
      ElMessage.success('图片上传成功')
    } else {
      // 上传失败
      ElMessage.error('图片上传失败: ' + (res.data?.message || '未知错误'))
    }
  } catch (error: unknown) {
    const err = error as Error
    ElMessage.error('图片上传失败: ' + (err?.message || '未知错误'))
  }
}

const rules = reactive<FormRules>({
  storeName: [{ required: true, message: '请输入门店名称', trigger: 'blur' }],
  address: [{ required: true, message: '请选择门店地址', trigger: 'change' }],
  mobile: [{ required: true, message: '请输入门店电话', trigger: 'blur' }],
  images: [{ required: true, message: '请上传门店照片', trigger: 'change' }],
})

// 修改经纬度
const updateAddress = (position: Position) => {
  formData.value.longitude = position.lng
  formData.value.latitude = position.lat
  let cityName = position.cityName
  if (cityName.indexOf('市') !== -1) {
    cityName = cityName.substring(0, cityName.indexOf('市'))
  }
  formData.value.cityName = cityName
}

// 重置表单
const resetForm = () => {
  formData.value = { images: '' }
  fileList.value = []
  imageUrls.value = []
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
  // 获取车辆详情
  fetchData(id)
}

// 获取数据
const fetchData = async (id: string) => {
  try {
    // @ts-expect-error API定义与实际接口不匹配，后端接收字符串类型的id
    const res = await getStoreUsingGet({ id })
    if (res.data?.code === 0 && res.data?.data) {
      formData.value = res.data.data

      // 处理已有的图片数据
      if (formData.value.images) {
        // 将图片URL字符串分割为数组
        imageUrls.value = formData.value.images.split(',')

        // 为每个图片URL创建一个文件对象，添加到fileList
        fileList.value = imageUrls.value.map((url, index) => {
          return {
            name: `已有图片${index + 1}`,
            url: url,
            // 转换为数字类型的uid
            uid: -(index + 1),
          }
        })
      }
    } else {
      ElMessage.error('获取门店详情失败:' + (res.data?.message || '未知错误'))
    }
  } catch (error) {
    ElMessage.error('加载门店详情失败:' + error)
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

.image-list-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.image-uploader :deep(.el-upload--picture-card) {
  width: 150px;
  height: 150px;
  line-height: 150px;
}

.image-uploader :deep(.el-upload-list--picture-card .el-upload-list__item) {
  width: 150px;
  height: 150px;
}
</style>
