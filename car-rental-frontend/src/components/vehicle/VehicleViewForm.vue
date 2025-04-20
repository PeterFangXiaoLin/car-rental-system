<template>
  <el-dialog v-model="visible" width="40%" @close="handleClose" destroy-on-close center>
    <template #header>
      <h3 class="title">车辆详情</h3>
    </template>
    <el-form
      ref="formRef"
      :model="formData"
      size="large"
      :disabled="true"
      label-width="120px"
      class="mt-8"
    >
      <el-form-item label="车辆名称" prop="name">
        <el-input v-model="formData.name" clearable />
      </el-form-item>
      <el-form-item label="车牌号" prop="vehicleNo">
        <el-input v-model="formData.vehicleNo" clearable />
      </el-form-item>
      <el-form-item label="车辆图片" prop="imageUrl">
        <el-image :src="formData.imageUrl" style="width: 150px; height: 150px; object-fit: cover" />
      </el-form-item>
      <el-form-item label="品牌" prop="brandId">
        <el-select v-model="formData.brandId" clearable>
          <el-option
            v-for="item in brandList"
            :key="item.id"
            :label="item.brandName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="型号" prop="modelId">
        <el-select v-model="formData.modelId" clearable>
          <el-option
            v-for="item in modelList"
            :key="item.id"
            :label="item.modelName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="车型" prop="vehicleTypeId">
        <el-select v-model="formData.vehicleTypeId" clearable>
          <el-option
            v-for="item in vehicleTypeList"
            :key="item.id"
            :label="item.typeName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="能源类型" prop="energyTypeId">
        <el-select v-model="formData.energyTypeId" clearable>
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
          clearable
        />
      </el-form-item>
      <el-form-item label="日租金" prop="dailyPrice">
        <el-input-number
          v-model="formData.dailyPrice"
          :step="10"
          :min="0"
          :precision="2"
          clearable
        />
      </el-form-item>
      <el-form-item label="座位数" prop="seatCount">
        <el-input-number v-model="formData.seatCount" :step="1" :min="4" clearable />
      </el-form-item>
      <el-form-item label="车辆状态" prop="status">
        <el-select v-model="formData.status" clearable>
          <el-option label="空闲中" :value="0" />
          <el-option label="已租出" :value="1" />
          <el-option label="维修中" :value="2" />
          <el-option label="已报废" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="车辆描述" prop="description">
        <el-input v-model="formData.description" type="textarea" rows="4" clearable />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="flex justify-center gap-[5%]">
        <el-button @click="handleClose" size="large">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import {ref, watch} from 'vue'
import type {FormInstance} from 'element-plus'
import {ElMessage} from 'element-plus'
import {listVehicleBrandUsingPost} from '@/api/vehicleBrandController.ts'
import {listVehicleTypeDictUsingPost} from '@/api/vehicleTypeDictController.ts'
import {listEnergyTypeDictUsingPost} from '@/api/energyTypeDictController.ts'
import {getVehicleByIdUsingGet} from '@/api/vehicleController.ts'

const visible = ref(false) //
const formRef = ref<FormInstance>()
const formData = ref<API.VehicleAddRequest>({})
const brandList = ref<API.VehicleBrandVO[]>([])
const modelList = ref<API.VehicleModelVO[]>([])
const vehicleTypeList = ref<API.VehicleTypeDictVO[]>([])
const energyTypeList = ref<API.EnergyTypeDictVO[]>([])

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
const open = (vehicleId: string) => {
  resetForm()
  visible.value = true
  fetchBrandList()
  fetchVehicleTypeList()
  fetchEnergyTypeList()
  fetchVehicleDetail(vehicleId)
}

// 根据id查询车辆详情
const fetchVehicleDetail = async (id: string) => {
  try {
    const res = await getVehicleByIdUsingGet({id})
    if (res.data?.code === 0 && res.data?.data) {
      formData.value = {
        ...res.data.data,
        productionYear: res.data.data.productionYear?.toString() // 将数字年份转为字符串
      }
    } else {
      ElMessage.error('获取车辆详情失败:' + (res.data?.message || '未知错误'))
    }
  } catch (error) {
    ElMessage.error('加载车辆详情失败:' + error)
  }
}

// 获取品牌
const fetchBrandList = async () => {
  try {
    const res = await listVehicleBrandUsingPost()
    if (res.data?.code === 0 && res.data?.data) {
      brandList.value = res.data.data
    } else {
      ElMessage.error('加载品牌列表失败:' + (res.data?.message || '未知错误'))
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
    } else {
      ElMessage.error('加载车型列表失败:' + (res.data?.message || '未知错误'))
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
    } else {
      ElMessage.error(res.data?.message ?? '系统错误')
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
        const res = await listVehicleBrandUsingPost({brandId: newValue})
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
