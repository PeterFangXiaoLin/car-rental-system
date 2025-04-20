<template>
  <el-dialog v-model="visible" width="40%" @close="handleClose" destroy-on-close center>
    <template #header>
      <h3 class="title">新增车辆</h3>
    </template>
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      size="large"
      label-width="120px"
      class="mt-8"
    >
      <el-form-item label="车辆名称" prop="name">
        <el-input v-model="formData.name" placeholder="请输入车辆名称" clearable />
      </el-form-item>
      <el-form-item label="车牌号" prop="vehicleNo">
        <el-input v-model="formData.vehicleNo" placeholder="请输入车牌号" clearable />
      </el-form-item>
      <el-form-item label="车辆图片" prop="imageUrl">
        <div class="avatar-uploader">
          <el-upload
            class="avatar-uploader-box"
            action="#"
            :before-upload="beforeAvatarUpload"
            :http-request="uploadRequest"
            :show-file-list="false"
          >
            <img v-if="formData.imageUrl" :src="formData.imageUrl" class="avatar-image" />
            <el-icon v-else class="avatar-uploader-icon">
              <Plus />
            </el-icon>
            <div v-if="!formData.imageUrl" class="upload-tip">点击上传车辆图片</div>
          </el-upload>
        </div>
      </el-form-item>
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
      <el-form-item label="型号" prop="modelId">
        <el-select v-model="formData.modelId" placeholder="请选择型号" clearable>
          <el-option
            v-for="item in modelList"
            :key="item.id"
            :label="item.modelName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="车型" prop="vehicleTypeId">
        <el-select v-model="formData.vehicleTypeId" placeholder="请选择车型" clearable>
          <el-option
            v-for="item in vehicleTypeList"
            :key="item.id"
            :label="item.typeName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="能源类型" prop="energyTypeId">
        <el-select v-model="formData.energyTypeId" placeholder="请选择能源类型" clearable>
          <el-option
            v-for="item in energyTypeList"
            :key="item.id"
            :label="item.typeName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="生产年份" prop="productionYear">
        <el-date-picker
          v-model="formData.productionYear"
          type="year"
          placeholder="请选择生产年份"
          format="YYYY"
          value-format="YYYY"
          clearable
        />
      </el-form-item>
      <el-form-item label="日租金" prop="dailyPrice">
        <el-input-number
          v-model="formData.dailyPrice"
          :step="10"
          :min="0"
          :precision="2"
          placeholder="请输入日租金"
          clearable
        />
      </el-form-item>
      <el-form-item label="座位数" prop="seatCount">
        <el-input-number
          v-model="formData.seatCount"
          :step="1"
          :min="4"
          placeholder="请输入座位数"
          clearable
        />
      </el-form-item>
      <el-form-item label="车辆状态" prop="status">
        <el-select v-model="formData.status" placeholder="请选择车辆状态" clearable>
          <el-option label="空闲中" :value="0" />
          <el-option label="已租出" :value="1" />
          <el-option label="维修中" :value="2" />
          <el-option label="已报废" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="车辆描述" prop="description">
        <el-input
          v-model="formData.description"
          type="textarea"
          rows="4"
          placeholder="请输入车辆描述"
          clearable
        />
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
import { listVehicleTypeDictUsingPost } from '@/api/vehicleTypeDictController.ts'
import { listEnergyTypeDictUsingPost } from '@/api/energyTypeDictController.ts'
import { addVehicleUsingPost } from '@/api/vehicleController.ts'
import {listVehicleModelUsingPost} from "@/api/vehicleModelController.ts";

const emit = defineEmits(['success']) // 定义成功事件, 供父组件调用

const visible = ref(false) //
const formRef = ref<FormInstance>()
const formData = ref<API.VehicleAddRequest>({})
const brandList = ref<API.VehicleBrandVO[]>([])
const modelList = ref<API.VehicleModelVO[]>([])
const vehicleTypeList = ref<API.VehicleTypeDictVO[]>([])
const energyTypeList = ref<API.EnergyTypeDictVO[]>([])

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
      formData.value.imageUrl = res.data.data
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
  name: [
    { required: true, message: '请输入车辆名称', trigger: 'blur' },
  ],
  vehicleNo: [
    { required: true, message: '请输入车牌号', trigger: 'blur' },
    // {
    //   pattern:
    //     /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-Z0-9]{5}$/,
    //   message: '请输入正确的车牌号格式',
    //   trigger: 'blur',
    // },
  ],
  brandId: [{ required: true, message: '请选择品牌', trigger: 'change' }],
  modelId: [{ required: true, message: '请选择型号', trigger: 'change' }],
  vehicleTypeId: [{ required: true, message: '请选择车型', trigger: 'change' }],
  energyTypeId: [{ required: true, message: '请选择能源类型', trigger: 'change' }],
  productionYear: [{ required: true, message: '请选择生产年份', trigger: 'change' }],
  dailyPrice: [
    { required: true, message: '请输入日租金', trigger: 'blur' },
    { type: 'number', min: 0, message: '日租金不能为负数', trigger: 'blur' },
  ],
  seatCount: [
    { required: true, message: '请输入座位数', trigger: 'blur' },
    { type: 'number', min: 2, message: '座位数最少为2个', trigger: 'blur' },
  ],
  status: [{ required: true, message: '请选择车辆状态', trigger: 'change' }],
  description: [{ max: 500, message: '描述不能超过500个字符', trigger: 'blur' }],
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
  fetchVehicleTypeList()
  fetchEnergyTypeList()
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
        const res = await addVehicleUsingPost(formData.value)

        if (res.data?.code === 0) {
          ElMessage.success('新增车辆成功')
          emit('success', '新增成功')
          handleClose()
        } else {
          ElMessage.error('新增失败: ' + (res.data?.message || '未知错误'))
        }
      } catch (error) {
        ElMessage.error('操作失败: ' + (error instanceof Error ? error.message : String(error)))
      } finally {
        loading.close()
      }
    } else {
      ElMessage.warning('请完善表单信息')
    }
  } catch (validationError) {
    ElMessage.error('表单验证失败')
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

// 获取车型
const fetchVehicleTypeList = async () => {
  try {
    const res = await listVehicleTypeDictUsingPost()
    if (res.data?.code === 0 && res.data?.data) {
      vehicleTypeList.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('加载车型列表失败:' + error)
  }
}

// 获取能源类型
const fetchEnergyTypeList = async () => {
  try {
    const res = await listEnergyTypeDictUsingPost()
    if (res.data?.code === 0 && res.data?.data) {
      energyTypeList.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('加载能源类型列表失败:' + error)
  }
}

watch(
  () => formData.value.brandId,
  async (newValue) => {
    if (newValue) {
      try {
        const res = await listVehicleModelUsingPost({ brandId: newValue })
        if (res.data?.code === 0 && res.data?.data) {
          modelList.value = res.data.data
        }
      } catch (error) {
        ElMessage.error('加载型号列表失败:' + error)
      }
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
