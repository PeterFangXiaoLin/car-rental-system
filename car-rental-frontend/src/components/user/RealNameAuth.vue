<template>
  <div class="real-name-auth">
    <!-- 未认证状态 -->
    <div v-if="authStatus === 0" class="auth-form">
      <el-alert
        title="您尚未进行实名认证，请填写以下信息进行认证"
        type="info"
        :closable="false"
        class="mb-4"
      />
      <el-form :model="form" ref="formRef" :rules="rules" label-width="100px">
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" maxlength="30" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCardNumber">
          <el-input v-model="form.idCardNumber" maxlength="18" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="驾驶证号" prop="drivingLicenseNo">
          <el-input v-model="form.drivingLicenseNo" maxlength="18" placeholder="请输入驾驶证号" />
        </el-form-item>
        <el-form-item label="驾龄(年)" prop="drivingYears">
          <el-input-number v-model="form.drivingYears" :min="0" :max="50" />
        </el-form-item>
        <el-form-item label="手机号码" prop="phoneNumber">
          <el-input v-model="form.phoneNumber" maxlength="11" placeholder="请输入手机号码" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" maxlength="50" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">提交认证</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 审核中状态 -->
    <div v-else-if="authStatus === 1" class="auth-pending">
      <el-result
        icon="info"
        title="实名认证审核中"
        sub-title="您的实名认证信息已提交，正在等待管理员审核，请耐心等待"
      >
        <template #extra>
          <el-descriptions title="认证信息" :column="1" border>
            <el-descriptions-item label="真实姓名">{{ authInfo.realName }}</el-descriptions-item>
            <el-descriptions-item label="身份证号">{{ maskIdCard(authInfo.idCardNumber) }}</el-descriptions-item>
            <el-descriptions-item label="驾驶证号">{{ maskLicenseNo(authInfo.drivingLicenseNo) }}</el-descriptions-item>
            <el-descriptions-item label="驾龄">{{ authInfo.drivingYears }}年</el-descriptions-item>
            <el-descriptions-item label="手机号码">{{ maskPhone(authInfo.phoneNumber) }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ maskEmail(authInfo.email) }}</el-descriptions-item>
            <el-descriptions-item label="提交时间">{{ formatTime(authInfo.submitTime) }}</el-descriptions-item>
          </el-descriptions>
        </template>
      </el-result>
    </div>

    <!-- 已认证状态 -->
    <div v-else-if="authStatus === 2" class="auth-approved">
      <el-result
        icon="success"
        title="实名认证已通过"
        sub-title="您的实名认证已通过审核，可以使用所有功能"
      >
        <template #extra>
          <el-descriptions title="认证信息" :column="1" border>
            <el-descriptions-item label="真实姓名">{{ authInfo.realName }}</el-descriptions-item>
            <el-descriptions-item label="身份证号">{{ maskIdCard(authInfo.idCardNumber) }}</el-descriptions-item>
            <el-descriptions-item label="驾驶证号">{{ maskLicenseNo(authInfo.drivingLicenseNo) }}</el-descriptions-item>
            <el-descriptions-item label="驾龄">{{ authInfo.drivingYears }}年</el-descriptions-item>
            <el-descriptions-item label="手机号码">{{ maskPhone(authInfo.phoneNumber) }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ maskEmail(authInfo.email) }}</el-descriptions-item>
            <el-descriptions-item label="认证时间">{{ formatTime(authInfo.approveTime) }}</el-descriptions-item>
          </el-descriptions>
          <el-button type="primary" @click="showUpdateDialog = true">修改认证信息</el-button>
        </template>
      </el-result>
    </div>

    <!-- 认证失败状态 -->
    <div v-else-if="authStatus === 3" class="auth-rejected">
      <el-result
        icon="error"
        title="实名认证未通过"
        :sub-title="'原因: ' + (authInfo.rejectReason || '信息有误，请重新提交')"
      >
        <template #extra>
          <el-button type="primary" @click="authStatus = 0">重新提交</el-button>
        </template>
      </el-result>
    </div>

    <!-- 修改认证信息对话框 -->
    <el-dialog
      v-model="showUpdateDialog"
      title="修改认证信息"
      width="500px"
    >
      <el-alert
        title="修改认证信息后，需要重新审核，在审核通过前部分功能可能受限"
        type="warning"
        :closable="false"
        class="mb-4"
      />
      <el-form :model="updateForm" ref="updateFormRef" :rules="rules" label-width="100px">
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="updateForm.realName" maxlength="30" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCardNumber">
          <el-input v-model="updateForm.idCardNumber" maxlength="18" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="驾驶证号" prop="drivingLicenseNo">
          <el-input v-model="updateForm.drivingLicenseNo" maxlength="18" placeholder="请输入驾驶证号" />
        </el-form-item>
        <el-form-item label="驾龄(年)" prop="drivingYears">
          <el-input-number v-model="updateForm.drivingYears" :min="0" :max="50" />
        </el-form-item>
        <el-form-item label="手机号码" prop="phoneNumber">
          <el-input v-model="updateForm.phoneNumber" maxlength="11" placeholder="请输入手机号码" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="updateForm.email" maxlength="50" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="修改原因" prop="updateReason">
          <el-input 
            v-model="updateForm.updateReason" 
            type="textarea" 
            :rows="3"
            placeholder="请简要说明修改原因" 
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showUpdateDialog = false">取消</el-button>
          <el-button type="primary" @click="handleUpdateSubmit">提交修改</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { updateUserUsingPost } from '@/api/userController'
import { ref, reactive, watch, type PropType } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { useLoginUserStore } from '@/stores/useLoginUserStore'
import dayjs from 'dayjs'

const props = defineProps({
  user: {
    type: Object as PropType<API.UserVO>,
    required: true,
  },
})

// 定义事件
const emit = defineEmits(['update:user', 'update'])

// 认证状态：0-未认证，1-审核中，2-已认证，3-认证失败
const authStatus = ref(0)

// 认证信息
const authInfo = reactive({
  realName: '',
  idCardNumber: '',
  drivingLicenseNo: '',
  drivingYears: 0,
  phoneNumber: '',
  email: '',
  submitTime: '',
  approveTime: '',
  rejectReason: '',
})

// 使用ref创建可修改的表单数据
const form = reactive({
  realName: '',
  idCardNumber: '',
  drivingLicenseNo: '',
  drivingYears: 0,
  phoneNumber: '',
  email: '',
})

// 修改表单数据
const updateForm = reactive({
  realName: '',
  idCardNumber: '',
  drivingLicenseNo: '',
  drivingYears: 0,
  phoneNumber: '',
  email: '',
  updateReason: '',
})

// 是否显示修改对话框
const showUpdateDialog = ref(false)

// 监听props变化，更新表单数据和认证状态
watch(
  () => props.user,
  (newUser) => {
    if (newUser) {
      // 填充表单数据
      form.realName = newUser.realName || ''
      form.phoneNumber = newUser.phoneNumber || ''
      form.email = newUser.email || ''
      form.drivingLicenseNo = newUser.drivingLicenseNo || ''
      form.drivingYears = newUser.drivingYears || 0
      form.idCardNumber = newUser.idCardNumber || ''

      // 根据用户信息判断认证状态
      // 这里假设后端返回的用户信息中有一个authStatus字段表示认证状态
      // 实际开发中需要根据后端API调整
      if (newUser.realName && newUser.idCardNumber) {
        // 模拟：如果有真实姓名和身份证号，则认为已提交认证
        if (newUser.userRole === 2) {
          // 如果用户角色是司机，则认为已认证通过
          authStatus.value = 2
          // 填充认证信息
          Object.assign(authInfo, {
            realName: newUser.realName,
            idCardNumber: newUser.idCardNumber,
            drivingLicenseNo: newUser.drivingLicenseNo,
            drivingYears: newUser.drivingYears,
            phoneNumber: newUser.phoneNumber,
            email: newUser.email,
            approveTime: newUser.editTime || newUser.createTime,
          })
        } else {
          // 否则认为审核中
          authStatus.value = 1
          // 填充认证信息
          Object.assign(authInfo, {
            realName: newUser.realName,
            idCardNumber: newUser.idCardNumber,
            drivingLicenseNo: newUser.drivingLicenseNo,
            drivingYears: newUser.drivingYears,
            phoneNumber: newUser.phoneNumber,
            email: newUser.email,
            submitTime: newUser.editTime || newUser.createTime,
          })
        }
      } else {
        // 如果没有真实姓名和身份证号，则认为未认证
        authStatus.value = 0
      }
    }
  },
  { immediate: true, deep: true },
)

const loginUserStore = useLoginUserStore()
const formRef = ref<FormInstance>()
const updateFormRef = ref<FormInstance>()

// 表单校验规则
const rules = reactive<FormRules>({
  realName: [
    { required: true, message: '真实姓名不能为空', trigger: 'blur' },
    { min: 2, max: 30, message: '真实姓名长度必须在 2 到 30 个字符之间', trigger: 'blur' },
  ],
  idCardNumber: [
    { required: true, message: '身份证号不能为空', trigger: 'blur' },
    { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入正确的身份证号', trigger: 'blur' },
  ],
  drivingLicenseNo: [
    { required: true, message: '驾驶证号不能为空', trigger: 'blur' },
    { pattern: /^[0-9a-zA-Z]{15,18}$/, message: '请输入正确的驾驶证号', trigger: 'blur' },
  ],
  drivingYears: [
    { required: true, message: '驾龄不能为空', trigger: 'blur' },
  ],
  phoneNumber: [
    { required: true, message: '手机号码不能为空', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' },
  ],
  email: [
    { required: true, message: '邮箱不能为空', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] },
  ],
  updateReason: [
    { required: true, message: '修改原因不能为空', trigger: 'blur' },
    { min: 5, max: 200, message: '修改原因长度必须在 5 到 200 个字符之间', trigger: 'blur' },
  ],
})

// 提交认证信息
const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const res = await updateUserUsingPost(form)
        if (res.data?.code === 0 && res.data?.data) {
          ElMessage.success('实名认证信息提交成功，请等待审核')
          // 更新用户信息
          loginUserStore.setLoginUser(res.data.data)
          // 更新认证状态
          authStatus.value = 1
          // 更新认证信息
          Object.assign(authInfo, {
            ...form,
            submitTime: new Date().toISOString(),
          })
          // 通知父组件更新用户信息
          emit('update:user', res.data.data)
          emit('update', res.data.data)
        } else {
          ElMessage.error(res.data?.message || '提交失败')
        }
      } catch (error) {
        console.error('提交实名认证信息失败:', error)
        ElMessage.error('提交失败，请稍后重试')
      }
    }
  })
}

// 提交修改认证信息
const handleUpdateSubmit = async () => {
  if (!updateFormRef.value) return
  await updateFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const res = await updateUserUsingPost(updateForm)
        if (res.data?.code === 0 && res.data?.data) {
          ElMessage.success('修改申请提交成功，请等待审核')
          // 更新用户信息
          loginUserStore.setLoginUser(res.data.data)
          // 更新认证状态
          authStatus.value = 1
          // 更新认证信息
          Object.assign(authInfo, {
            ...updateForm,
            submitTime: new Date().toISOString(),
          })
          // 关闭对话框
          showUpdateDialog.value = false
          // 通知父组件更新用户信息
          emit('update:user', res.data.data)
          emit('update', res.data.data)
        } else {
          ElMessage.error(res.data?.message || '提交失败')
        }
      } catch (error) {
        ElMessage.error('提交失败，请稍后重试' + (error as Error).message)
      }
    }
  })
}

// 重置基本信息表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
    // 重新从props初始化表单
    if (props.user) {
      form.realName = props.user.realName || ''
      form.phoneNumber = props.user.phoneNumber || ''
      form.email = props.user.email || ''
    }
  }
}

// 格式化时间
const formatTime = (time: string) => {
  return time ? dayjs(time).format('YYYY-MM-DD HH:mm:ss') : '-'
}

// 隐藏身份证号中间部分
const maskIdCard = (idCard: string) => {
  if (!idCard) return '-'
  return idCard.replace(/^(.{6})(.*)(.{4})$/, '$1********$3')
}

// 隐藏驾驶证号中间部分
const maskLicenseNo = (licenseNo: string) => {
  if (!licenseNo) return '-'
  return licenseNo.replace(/^(.{4})(.*)(.{4})$/, '$1********$3')
}

// 隐藏手机号中间部分
const maskPhone = (phone: string) => {
  if (!phone) return '-'
  return phone.replace(/^(.{3})(.*)(.{4})$/, '$1****$3')
}

// 隐藏邮箱用户名部分
const maskEmail = (email: string) => {
  if (!email) return '-'
  const parts = email.split('@')
  if (parts.length !== 2) return email
  const name = parts[0]
  const domain = parts[1]
  return `${name.substring(0, 3)}***@${domain}`
}
</script>

<style scoped>
.real-name-auth {
  padding: 20px 0;
}

.auth-form {
  max-width: 600px;
}

.mb-4 {
  margin-bottom: 16px;
}

.auth-pending,
.auth-approved,
.auth-rejected {
  text-align: center;
}
</style>
