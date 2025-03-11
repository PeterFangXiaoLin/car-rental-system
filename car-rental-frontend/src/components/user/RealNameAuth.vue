<template>
  <div class="real-name-auth">
    <!-- 未认证状态 -->
    <div v-if="authStatus === VERIFY_STATUS_ENUM.NOT_VERIFY" class="auth-form">
      <el-alert
        title="您尚未进行实名认证，请填写以下信息进行认证"
        type="info"
        :closable="false"
        class="mb-4"
      />
      <el-form :model="form" ref="formRef" :rules="rules" label-width="120px">
        <el-form-item label="真实姓名" prop="auth.realName">
          <el-input v-model="form.auth.realName" maxlength="30" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="身份证号" prop="auth.idCardNumber">
          <el-input v-model="form.auth.idCardNumber" maxlength="18" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="驾驶证号" prop="auth.driverLicenseNo">
          <el-input v-model="form.auth.driverLicenseNo" maxlength="18" placeholder="请输入驾驶证号" />
        </el-form-item>
        <el-form-item label="驾驶证类型" prop="auth.driverLicenseType">
          <el-select v-model="form.auth.driverLicenseType" placeholder="请选择驾驶证类型">
            <el-option label="A1" value="A1" />
            <el-option label="A2" value="A2" />
            <el-option label="A3" value="A3" />
            <el-option label="B1" value="B1" />
            <el-option label="B2" value="B2" />
            <el-option label="C1" value="C1" />
            <el-option label="C2" value="C2" />
            <el-option label="C3" value="C3" />
            <el-option label="D" value="D" />
            <el-option label="E" value="E" />
            <el-option label="F" value="F" />
          </el-select>
        </el-form-item>
        <el-form-item label="发证日期" prop="auth.driverLicenseIssueDate">
          <el-date-picker
            v-model="form.auth.driverLicenseIssueDate"
            type="date"
            placeholder="选择发证日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="有效期至" prop="auth.driverLicenseExpireDate">
          <el-date-picker
            v-model="form.auth.driverLicenseExpireDate"
            type="date"
            placeholder="选择有效期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="驾龄(年)" prop="userInfo.drivingYears">
          <el-input-number v-model="form.userInfo.drivingYears" :min="0" :max="50" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">提交认证</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 审核中状态 -->
    <div v-else-if="authStatus === VERIFY_STATUS_ENUM.VERIFY_ING" class="auth-pending">
      <el-result
        icon="info"
        title="实名认证审核中"
        sub-title="您的实名认证信息已提交，正在等待管理员审核，请耐心等待"
      >
        <template #extra>
          <el-descriptions title="认证信息" :column="1" border>
            <el-descriptions-item label="真实姓名">{{ authInfo.realName }}</el-descriptions-item>
            <el-descriptions-item label="身份证号">{{ maskIdCard(authInfo.idCardNumber) }}</el-descriptions-item>
            <el-descriptions-item label="驾驶证号">{{ maskLicenseNo(authInfo.driverLicenseNo) }}</el-descriptions-item>
            <el-descriptions-item label="驾驶证类型">{{ authInfo.driverLicenseType }}</el-descriptions-item>
            <el-descriptions-item label="发证日期">{{ authInfo.driverLicenseIssueDate }}</el-descriptions-item>
            <el-descriptions-item label="有效期至">{{ authInfo.driverLicenseExpireDate }}</el-descriptions-item>
            <el-descriptions-item label="驾龄">{{ authInfo.drivingYears }}年</el-descriptions-item>
            <el-descriptions-item label="手机号码">{{ maskPhone(authInfo.phoneNumber) }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ maskEmail(authInfo.email) }}</el-descriptions-item>
            <el-descriptions-item label="提交时间">{{ formatTime(authInfo.submitTime) }}</el-descriptions-item>
          </el-descriptions>
          <el-button type="primary" @click="handleShowUpdateDialog">修改认证信息</el-button>
        </template>
      </el-result>
    </div>

    <!-- 已认证状态 -->
    <div v-else-if="authStatus === VERIFY_STATUS_ENUM.VERIFY_SUCCESS" class="auth-approved">
      <el-result
        icon="success"
        title="实名认证已通过"
        sub-title="您的实名认证已通过审核，可以使用所有功能"
      >
        <template #extra>
          <el-descriptions title="认证信息" :column="1" border>
            <el-descriptions-item label="真实姓名">{{ authInfo.realName }}</el-descriptions-item>
            <el-descriptions-item label="身份证号">{{ maskIdCard(authInfo.idCardNumber) }}</el-descriptions-item>
            <el-descriptions-item label="驾驶证号">{{ maskLicenseNo(authInfo.driverLicenseNo) }}</el-descriptions-item>
            <el-descriptions-item label="驾驶证类型">{{ authInfo.driverLicenseType }}</el-descriptions-item>
            <el-descriptions-item label="发证日期">{{ authInfo.driverLicenseIssueDate }}</el-descriptions-item>
            <el-descriptions-item label="有效期至">{{ authInfo.driverLicenseExpireDate }}</el-descriptions-item>
            <el-descriptions-item label="驾龄">{{ authInfo.drivingYears }}年</el-descriptions-item>
            <el-descriptions-item label="手机号码">{{ maskPhone(authInfo.phoneNumber) }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ maskEmail(authInfo.email) }}</el-descriptions-item>
            <el-descriptions-item label="认证时间">{{ formatTime(authInfo.approveTime) }}</el-descriptions-item>
          </el-descriptions>
          <el-button type="primary" @click="handleShowUpdateDialog">修改认证信息</el-button>
        </template>
      </el-result>
    </div>

    <!-- 认证失败状态 -->
    <div v-else-if="authStatus === VERIFY_STATUS_ENUM.VERIFY_FAILED" class="auth-rejected">
      <el-result
        icon="error"
        title="实名认证未通过"
        :sub-title="'原因: ' + (authInfo.rejectReason || '信息有误，请重新提交')"
      >
        <template #extra>
          <el-descriptions v-if="authInfo.realName" title="认证信息" :column="1" border>
            <el-descriptions-item label="真实姓名">{{ authInfo.realName }}</el-descriptions-item>
            <el-descriptions-item label="身份证号">{{ maskIdCard(authInfo.idCardNumber) }}</el-descriptions-item>
            <el-descriptions-item label="驾驶证号">{{ maskLicenseNo(authInfo.driverLicenseNo) }}</el-descriptions-item>
            <el-descriptions-item label="驾驶证类型">{{ authInfo.driverLicenseType }}</el-descriptions-item>
            <el-descriptions-item label="发证日期">{{ authInfo.driverLicenseIssueDate }}</el-descriptions-item>
            <el-descriptions-item label="有效期至">{{ authInfo.driverLicenseExpireDate }}</el-descriptions-item>
          </el-descriptions>
          <div class="mt-4">
            <el-button type="primary" @click="handleShowUpdateDialog">修改认证信息</el-button>
            <el-button @click="authStatus = VERIFY_STATUS_ENUM.NOT_VERIFY">重新提交</el-button>
          </div>
        </template>
      </el-result>
    </div>

    <!-- 修改认证信息对话框 -->
    <el-dialog
      v-model="showUpdateDialog"
      :title="getDialogTitle"
      width="500px"
    >
      <el-alert
        :title="getDialogTip"
        type="warning"
        :closable="false"
        class="mb-4"
      />
      <el-form :model="updateForm" ref="updateFormRef" :rules="rules" label-width="120px">
        <el-form-item label="真实姓名" prop="auth.realName">
          <el-input v-model="updateForm.auth.realName" maxlength="30" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="身份证号" prop="auth.idCardNumber">
          <el-input v-model="updateForm.auth.idCardNumber" maxlength="18" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="驾驶证号" prop="auth.driverLicenseNo">
          <el-input v-model="updateForm.auth.driverLicenseNo" maxlength="18" placeholder="请输入驾驶证号" />
        </el-form-item>
        <el-form-item label="驾驶证类型" prop="auth.driverLicenseType">
          <el-select v-model="updateForm.auth.driverLicenseType" placeholder="请选择驾驶证类型">
            <el-option label="A1" value="A1" />
            <el-option label="A2" value="A2" />
            <el-option label="A3" value="A3" />
            <el-option label="B1" value="B1" />
            <el-option label="B2" value="B2" />
            <el-option label="C1" value="C1" />
            <el-option label="C2" value="C2" />
            <el-option label="C3" value="C3" />
            <el-option label="D" value="D" />
            <el-option label="E" value="E" />
            <el-option label="F" value="F" />
          </el-select>
        </el-form-item>
        <el-form-item label="发证日期" prop="auth.driverLicenseIssueDate">
          <el-date-picker
            v-model="updateForm.auth.driverLicenseIssueDate"
            type="date"
            placeholder="选择发证日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="有效期至" prop="auth.driverLicenseExpireDate">
          <el-date-picker
            v-model="updateForm.auth.driverLicenseExpireDate"
            type="date"
            placeholder="选择有效期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="驾龄(年)" prop="userInfo.drivingYears">
          <el-input-number v-model="updateForm.userInfo.drivingYears" :min="0" :max="50" />
        </el-form-item>
        <el-form-item label="手机号码" prop="userInfo.phoneNumber">
          <el-input v-model="updateForm.userInfo.phoneNumber" maxlength="11" placeholder="请输入手机号码" />
        </el-form-item>
        <el-form-item label="邮箱" prop="userInfo.email">
          <el-input v-model="updateForm.userInfo.email" maxlength="50" placeholder="请输入邮箱" />
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
import { authUserUsingPost, updateUserUsingPost } from '@/api/userController'
import { ref, reactive, watch, type PropType, computed } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { useLoginUserStore } from '@/stores/useLoginUserStore'
import dayjs from 'dayjs'
import VERIFY_STATUS_ENUM from '@/enums/VerifyStatusEnum'
const props = defineProps({
  user: {
    type: Object as PropType<API.UserVO>,
    required: true,
  },
})

// 定义事件
const emit = defineEmits(['update:user', 'update'])

// 认证状态：0-未认证，1-审核中，2-已认证，3-认证失败
const authStatus = ref(props.user?.verifyStatus)

// 认证信息
const authInfo = reactive({
  realName: '',
  idCardNumber: '',
  driverLicenseNo: '',
  driverLicenseExpireDate: '',
  driverLicenseIssueDate: '',
  driverLicenseType: '',
  drivingYears: 0,
  phoneNumber: '',
  email: '',
  submitTime: '',
  approveTime: '',
  rejectReason: '',
})

// 使用ref创建可修改的表单数据
const form = reactive<{
  auth: API.UserAuthRequest;
  userInfo: {
    phoneNumber: string;
    email: string;
    drivingYears: number;
  }
}>({
  auth: {
    realName: '',
    idCardNumber: '',
    driverLicenseNo: '',
    driverLicenseExpireDate: '',
    driverLicenseIssueDate: '',
    driverLicenseType: '',
  },
  userInfo: {
    phoneNumber: '',
    email: '',
    drivingYears: 0,
  }
})

// 修改表单数据
const updateForm = reactive<{
  auth: API.UserAuthRequest;
  userInfo: {
    phoneNumber: string;
    email: string;
    drivingYears: number;
  };
  updateReason: string;
}>({
  auth: {
    realName: '',
    idCardNumber: '',
    driverLicenseNo: '',
    driverLicenseExpireDate: '',
    driverLicenseIssueDate: '',
    driverLicenseType: '',
  },
  userInfo: {
    phoneNumber: '',
    email: '',
    drivingYears: 0,
  },
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
      form.auth.realName = newUser.realName || ''
      form.auth.idCardNumber = newUser.idCardNumber || ''
      form.auth.driverLicenseNo = newUser.driverLicenseNo || ''
      form.auth.driverLicenseExpireDate = newUser.driverLicenseExpireDate || ''
      form.auth.driverLicenseIssueDate = newUser.driverLicenseIssueDate || ''
      form.auth.driverLicenseType = newUser.driverLicenseType || ''
      form.userInfo.phoneNumber = newUser.phoneNumber || ''
      form.userInfo.email = newUser.email || ''
      form.userInfo.drivingYears = newUser.drivingYears || 0

      // 根据用户信息判断认证状态
      // 这里假设后端返回的用户信息中有一个verifyStatus字段表示认证状态
      if (newUser.verifyStatus !== undefined) {
        authStatus.value = newUser.verifyStatus
      } else if (newUser.realName && newUser.idCardNumber) {
        // 模拟：如果有真实姓名和身份证号，则认为已提交认证
        if (newUser.userRole === 2) {
          // 如果用户角色是司机，则认为已认证通过
          authStatus.value = VERIFY_STATUS_ENUM.VERIFY_SUCCESS
          // 填充认证信息
          Object.assign(authInfo, {
            realName: newUser.realName,
            idCardNumber: newUser.idCardNumber,
            driverLicenseNo: newUser.driverLicenseNo,
            driverLicenseExpireDate: newUser.driverLicenseExpireDate,
            driverLicenseIssueDate: newUser.driverLicenseIssueDate,
            driverLicenseType: newUser.driverLicenseType,
            drivingYears: newUser.drivingYears,
            phoneNumber: newUser.phoneNumber,
            email: newUser.email,
            approveTime: newUser.verifyTime || newUser.editTime || newUser.createTime,
          })
        } else {
          // 否则认为审核中
          authStatus.value = VERIFY_STATUS_ENUM.VERIFY_ING
          // 填充认证信息
          Object.assign(authInfo, {
            realName: newUser.realName,
            idCardNumber: newUser.idCardNumber,
            driverLicenseNo: newUser.driverLicenseNo,
            driverLicenseExpireDate: newUser.driverLicenseExpireDate,
            driverLicenseIssueDate: newUser.driverLicenseIssueDate,
            driverLicenseType: newUser.driverLicenseType,
            drivingYears: newUser.drivingYears,
            phoneNumber: newUser.phoneNumber,
            email: newUser.email,
            submitTime: newUser.editTime || newUser.createTime,
          })
        }
      } else {
        // 如果没有真实姓名和身份证号，则认为未认证
        authStatus.value = VERIFY_STATUS_ENUM.NOT_VERIFY
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
  'auth.realName': [
    { required: true, message: '真实姓名不能为空', trigger: 'blur' },
    { min: 2, max: 30, message: '真实姓名长度必须在 2 到 30 个字符之间', trigger: 'blur' },
  ],
  'auth.idCardNumber': [
    { required: true, message: '身份证号不能为空', trigger: 'blur' },
    { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入正确的身份证号', trigger: 'blur' },
  ],
  'auth.driverLicenseNo': [
    { required: true, message: '驾驶证号不能为空', trigger: 'blur' },
    { pattern: /^[0-9a-zA-Z]{15,18}$/, message: '请输入正确的驾驶证号', trigger: 'blur' },
  ],
  'auth.driverLicenseExpireDate': [
    { required: true, message: '驾驶证有效期不能为空', trigger: 'blur' },
  ],
  'auth.driverLicenseIssueDate': [
    { required: true, message: '驾驶证发证日期不能为空', trigger: 'blur' },
  ],
  'auth.driverLicenseType': [
    { required: true, message: '驾驶证类型不能为空', trigger: 'blur' },
  ],
  'userInfo.drivingYears': [
    { required: true, message: '驾龄不能为空', trigger: 'blur' },
  ],
  'userInfo.phoneNumber': [
    { required: true, message: '手机号码不能为空', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' },
  ],
  'userInfo.email': [
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
        // 调用实名认证接口
        const res = await authUserUsingPost(form.auth)
        
        if (res.data?.code === 0) {
          ElMessage.success('实名认证信息提交成功，请等待审核')
          
          // 更新用户信息
          const userInfo = await updateUserUsingPost({
            phoneNumber: form.userInfo.phoneNumber,
            email: form.userInfo.email,
          })
          
          if (userInfo.data?.code === 0 && userInfo.data?.data) {
            loginUserStore.setLoginUser(userInfo.data.data)
            // 更新认证状态
            authStatus.value = VERIFY_STATUS_ENUM.VERIFY_ING
            // 更新认证信息
            Object.assign(authInfo, {
              ...form.auth,
              phoneNumber: form.userInfo.phoneNumber,
              email: form.userInfo.email,
              drivingYears: form.userInfo.drivingYears,
              submitTime: new Date().toISOString(),
            })
            // 通知父组件更新用户信息
            emit('update:user', userInfo.data.data)
            emit('update', userInfo.data.data)
          }
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
        // 调用实名认证接口
        const res = await authUserUsingPost(updateForm.auth)
        
        if (res.data?.code === 0) {
          ElMessage.success('修改申请提交成功，请等待审核')
          
          // 更新用户信息
          const userInfo = await updateUserUsingPost({
            phoneNumber: updateForm.userInfo.phoneNumber,
            email: updateForm.userInfo.email,
          })
          
          if (userInfo.data?.code === 0 && userInfo.data?.data) {
            loginUserStore.setLoginUser(userInfo.data.data)
            // 更新认证状态
            authStatus.value = VERIFY_STATUS_ENUM.VERIFY_ING
            // 更新认证信息
            Object.assign(authInfo, {
              ...updateForm.auth,
              phoneNumber: updateForm.userInfo.phoneNumber,
              email: updateForm.userInfo.email,
              drivingYears: updateForm.userInfo.drivingYears,
              submitTime: new Date().toISOString(),
            })
            // 关闭对话框
            showUpdateDialog.value = false
            // 通知父组件更新用户信息
            emit('update:user', userInfo.data.data)
            emit('update', userInfo.data.data)
          }
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
      form.auth.realName = props.user.realName || ''
      form.userInfo.phoneNumber = props.user.phoneNumber || ''
      form.userInfo.email = props.user.email || ''
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

// 打开修改对话框
const handleShowUpdateDialog = () => {
  // 初始化修改表单数据
  updateForm.auth.realName = authInfo.realName
  updateForm.auth.idCardNumber = authInfo.idCardNumber
  updateForm.auth.driverLicenseNo = authInfo.driverLicenseNo
  updateForm.auth.driverLicenseExpireDate = authInfo.driverLicenseExpireDate
  updateForm.auth.driverLicenseIssueDate = authInfo.driverLicenseIssueDate
  updateForm.auth.driverLicenseType = authInfo.driverLicenseType
  updateForm.userInfo.drivingYears = authInfo.drivingYears
  updateForm.userInfo.phoneNumber = authInfo.phoneNumber
  updateForm.userInfo.email = authInfo.email
  updateForm.updateReason = ''
  
  // 显示对话框
  showUpdateDialog.value = true
}

// 获取对话框标题
const getDialogTitle = computed(() => {
  switch (authStatus.value) {
    case VERIFY_STATUS_ENUM.VERIFY_ING:
      return '修改审核中的认证信息'
    case VERIFY_STATUS_ENUM.VERIFY_SUCCESS:
      return '修改已认证的信息'
    case VERIFY_STATUS_ENUM.VERIFY_FAILED:
      return '修改未通过的认证信息'
    default:
      return '修改认证信息'
  }
})

// 获取对话框提示信息
const getDialogTip = computed(() => {
  switch (authStatus.value) {
    case VERIFY_STATUS_ENUM.VERIFY_ING:
      return '修改审核中的认证信息将重置审核流程，需要重新审核'
    case VERIFY_STATUS_ENUM.VERIFY_SUCCESS:
      return '修改认证信息后，需要重新审核，在审核通过前部分功能可能受限'
    case VERIFY_STATUS_ENUM.VERIFY_FAILED:
      return '请修改认证信息后重新提交，确保信息准确无误'
    default:
      return '请填写认证信息'
  }
})
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

.mt-4 {
  margin-top: 16px;
}

.auth-pending,
.auth-approved,
.auth-rejected {
  text-align: center;
}
</style>
