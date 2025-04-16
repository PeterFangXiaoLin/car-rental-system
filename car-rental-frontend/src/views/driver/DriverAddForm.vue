<template>
  <el-dialog v-model="visible" width="40%" @close="handleClose" destroy-on-close center>
    <template #header>
      <h3 class="title">新增司机</h3>
    </template>
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      size="large"
      label-width="120px"
      class="mt-8"
    >
      <el-form-item label="司机姓名" prop="driverName">
        <el-input v-model="formData.driverName" placeholder="请输入司机姓名" clearable />
      </el-form-item>
      <el-form-item label="司机头像" prop="driverAvatar">
        <div class="avatar-uploader">
          <el-upload
            class="avatar-uploader-box"
            action="#"
            :before-upload="beforeAvatarUpload"
            :http-request="uploadRequest"
            :show-file-list="false"
          >
            <img v-if="formData.driverAvatar" :src="formData.driverAvatar" class="avatar-image" />
            <el-icon v-else class="avatar-uploader-icon">
              <Plus />
            </el-icon>
            <div v-if="!formData.driverAvatar" class="upload-tip">点击上传头像</div>
          </el-upload>
        </div>
      </el-form-item>
      <el-form-item label="性别" prop="gender">
        <el-select v-model="formData.gender" placeholder="请选择性别" clearable>
          <el-option label="男" :value="0" />
          <el-option label="女" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="年龄" prop="age">
        <el-input v-model="formData.age" type="number" placeholder="请输入年龄" clearable />
      </el-form-item>
      <el-form-item label="联系电话" prop="phoneNumber">
        <el-input v-model="formData.phoneNumber" placeholder="请输入联系电话" clearable />
      </el-form-item>
      <el-form-item label="驾驶证号码" prop="driverLicenseNo">
        <el-input v-model="formData.driverLicenseNo" placeholder="请输入驾驶证号码" clearable />
      </el-form-item>
      <el-form-item label="驾照类型" prop="driverLicenseType">
        <el-select v-model="formData.driverLicenseType" placeholder="请选择驾照类型" clearable>
          <el-option label="A1" value="A1" />
          <el-option label="A2" value="A2" />
          <el-option label="B1" value="B1" />
          <el-option label="B2" value="B2" />
          <el-option label="C1" value="C1" />
          <el-option label="C2" value="C2" />
        </el-select>
      </el-form-item>
      <el-form-item label="驾驶证照片" prop="driverLicenseImg">
        <div class="avatar-uploader">
          <el-upload
            class="avatar-uploader-box"
            action="#"
            :before-upload="beforeAvatarUpload"
            :http-request="uploadLicenseRequest"
            :show-file-list="false"
          >
            <img
              v-if="formData.driverLicenseImg"
              :src="formData.driverLicenseImg"
              class="avatar-image"
            />
            <el-icon v-else class="avatar-uploader-icon">
              <Plus />
            </el-icon>
            <div v-if="!formData.driverLicenseImg" class="upload-tip">点击上传驾驶证照片</div>
          </el-upload>
        </div>
      </el-form-item>
      <el-form-item label="驾照发证日期" prop="driverLicenseIssueDate">
        <el-date-picker
          v-model="formData.driverLicenseIssueDate"
          type="date"
          placeholder="请选择驾照发证日期"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          clearable
        />
      </el-form-item>
      <el-form-item label="驾照到期日期" prop="driverLicenseExpireDate">
        <el-date-picker
          v-model="formData.driverLicenseExpireDate"
          type="date"
          placeholder="请选择驾照到期日期"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          clearable
        />
      </el-form-item>
      <el-form-item label="驾龄" prop="drivingYears">
        <el-input
          v-model="formData.drivingYears"
          type="number"
          placeholder="请输入驾龄"
          clearable
        />
      </el-form-item>
      <el-form-item label="日薪" prop="dailyPrice">
        <el-input-number
          v-model="formData.dailyPrice"
          :step="10"
          :min="0"
          :precision="2"
          placeholder="请输入日薪"
          clearable
        />
      </el-form-item>
      <el-form-item label="工作状态" prop="workStatus">
        <el-select v-model="formData.workStatus" placeholder="请选择工作状态" clearable>
          <el-option label="休息中" :value="0" />
          <el-option label="可接单" :value="1" />
          <el-option label="已接单" :value="2" />
        </el-select>
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
import { Plus } from '@element-plus/icons-vue'
import { uploadFileUsingPost } from '@/api/fileUploadController'
import { addDriverUsingPost } from '@/api/driverController.ts'

const emit = defineEmits(['success']) // 定义成功事件, 供父组件调用

const visible = ref(false) //
const formRef = ref<FormInstance>()
const formData = ref<API.DriverAddRequest>({})

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

// 自定义上传请求 - 头像
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
      formData.value.driverAvatar = res.data.data
      ElMessage.success('头像上传成功')
    } else {
      // 上传失败
      ElMessage.error('头像上传失败: ' + (res.data?.message || '未知错误'))
    }
  } catch (error: Error) {
    ElMessage.error('头像上传失败: ' + (error.message || '未知错误'))
  }
}

// 自定义上传请求 - 驾驶证
const uploadLicenseRequest = async (options: { file: File }) => {
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
      // 上传成功，设置驾驶证照片URL
      formData.value.driverLicenseImg = res.data.data
      ElMessage.success('驾驶证照片上传成功')
    } else {
      // 上传失败
      ElMessage.error('驾驶证照片上传失败: ' + (res.data?.message || '未知错误'))
    }
  } catch (error) {
    ElMessage.error('驾驶证照片上传失败: ' + error)
  }
}

const rules = reactive<FormRules>({
  driverName: [{ required: true, message: '请输入司机姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  age: [{ required: true, message: '请输入年龄', trigger: 'blur' }],
  phoneNumber: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
  driverLicenseNo: [{ required: true, message: '请输入驾驶证号码', trigger: 'blur' }],
  driverLicenseType: [{ required: true, message: '请选择驾照类型', trigger: 'change' }],
  driverLicenseIssueDate: [{ required: true, message: '请选择驾照发证日期', trigger: 'change' }],
  driverLicenseExpireDate: [{ required: true, message: '请选择驾照到期日期', trigger: 'change' }],
  workStatus: [{ required: true, message: '请选择工作状态', trigger: 'change' }],
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
const open = async () => {
  resetForm()
  visible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  try {
    // 先验证表单
    const valid = await formRef.value.validate().catch((err) => {
      console.error('表单验证错误:', err)
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
        const res = await addDriverUsingPost(formData.value)

        if (res.data?.code === 0) {
          ElMessage.success('新增司机成功')
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
