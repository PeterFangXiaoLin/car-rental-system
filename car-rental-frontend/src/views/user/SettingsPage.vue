<template>
  <div class="settings-container">
    <el-card class="settings-card">
      <template #header>
        <div class="card-header">
          <h3>账号设置</h3>
        </div>
      </template>
      
      <div class="settings-content">
        <el-tabs>
          <el-tab-pane label="修改密码">
            <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="100px" class="settings-form">
              <el-form-item label="当前密码" prop="currentPassword">
                <el-input v-model="passwordForm.currentPassword" type="password" show-password />
              </el-form-item>
              
              <el-form-item label="新密码" prop="newPassword">
                <el-input v-model="passwordForm.newPassword" type="password" show-password />
              </el-form-item>
              
              <el-form-item label="确认新密码" prop="confirmPassword">
                <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
              </el-form-item>
              
              <el-form-item>
                <el-button type="primary" @click="changePassword">修改密码</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          
          <el-tab-pane label="安全设置">
            <el-form label-width="100px" class="settings-form">
              <el-form-item label="手机绑定">
                <div class="security-item">
                  <span>{{ securityInfo.phone || '未绑定' }}</span>
                  <el-button type="primary" link>{{ securityInfo.phone ? '修改' : '绑定' }}</el-button>
                </div>
              </el-form-item>
              
              <el-form-item label="邮箱绑定">
                <div class="security-item">
                  <span>{{ securityInfo.email || '未绑定' }}</span>
                  <el-button type="primary" link>{{ securityInfo.email ? '修改' : '绑定' }}</el-button>
                </div>
              </el-form-item>
              
              <el-form-item label="登录设备">
                <el-button type="warning" size="small">查看登录设备</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          
          <el-tab-pane label="通知设置">
            <el-form label-width="100px" class="settings-form">
              <el-form-item label="系统通知">
                <el-switch v-model="notificationSettings.system" />
              </el-form-item>
              
              <el-form-item label="订单通知">
                <el-switch v-model="notificationSettings.order" />
              </el-form-item>
              
              <el-form-item label="活动通知">
                <el-switch v-model="notificationSettings.activity" />
              </el-form-item>
              
              <el-form-item>
                <el-button type="primary" @click="saveNotificationSettings">保存设置</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'

// 密码表单
const passwordForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 安全信息
const securityInfo = reactive({
  phone: '138****8000',
  email: 'zha****@example.com'
})

// 通知设置
const notificationSettings = reactive({
  system: true,
  order: true,
  activity: false
})

const passwordFormRef = ref<FormInstance>()

// 验证新密码
const validatePass = (rule: any, value: string, callback: any) => {
  if (value === '') {
    callback(new Error('请输入新密码'))
  } else {
    if (passwordForm.confirmPassword !== '') {
      passwordFormRef.value?.validateField('confirmPassword')
    }
    callback()
  }
}

// 验证确认密码
const validateConfirmPass = (rule: any, value: string, callback: any) => {
  if (value === '') {
    callback(new Error('请再次输入新密码'))
  } else if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入密码不一致!'))
  } else {
    callback()
  }
}

// 密码表单验证规则
const passwordRules = reactive<FormRules>({
  currentPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, validator: validatePass, trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validateConfirmPass, trigger: 'blur' }
  ]
})

// 修改密码
const changePassword = async () => {
  if (!passwordFormRef.value) return
  
  await passwordFormRef.value.validate((valid) => {
    if (valid) {
      // 这里应该调用API修改密码
      ElMessage.success('密码修改成功')
      // 重置表单
      passwordForm.currentPassword = ''
      passwordForm.newPassword = ''
      passwordForm.confirmPassword = ''
    }
  })
}

// 保存通知设置
const saveNotificationSettings = () => {
  // 这里应该调用API保存通知设置
  ElMessage.success('通知设置保存成功')
}
</script>

<style scoped>
.settings-container {
  padding: 20px;
}

.settings-card {
  max-width: 800px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.settings-content {
  padding: 20px 0;
}

.settings-form {
  max-width: 500px;
}

.security-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style> 