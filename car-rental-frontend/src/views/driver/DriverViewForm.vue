<template>
  <el-dialog v-model="visible" width="40%" @close="handleClose" destroy-on-close center>
    <template #header>
      <h3 class="title">司机详情</h3>
    </template>
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      size="large"
      label-width="100px"
      class="mt-8"
      disabled
    >
      <el-form-item label="司机姓名" prop="driverName">
        <el-input v-model="formData.driverName" placeholder="请输入司机姓名" clearable />
      </el-form-item>
      <el-form-item label="司机头像" prop="driverAvatar">
        <el-image
          v-if="formData.driverAvatar"
          :src="formData.driverAvatar"
          style="width: 150px; height: 150px; object-fit: cover"
        />
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
        <el-image
          v-if="formData.driverLicenseImg"
          :src="formData.driverLicenseImg"
          style="width: 150px; height: 150px; object-fit: cover"
        />
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
        <el-button @click="handleClose" size="large">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { getDriverVoByIdUsingGet } from '@/api/driverController.ts'

const visible = ref(false)
const formRef = ref<FormInstance>()
const formData = ref<API.DriverUpdateRequest>({})

const rules = reactive<FormRules>({
  userAccount: [],
  userRole: [{ required: true, message: '请选择用户角色', trigger: 'change' }],
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
const open = async (driverId: string) => {
  resetForm()
  visible.value = true
  const res = await getDriverVoByIdUsingGet({
    id: driverId,
  })
  if (res.data?.code === 0 && res.data?.data) {
    formData.value = res.data.data
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
