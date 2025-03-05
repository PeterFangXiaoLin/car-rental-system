<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 左侧个人信息卡片 -->
      <el-col :span="6" :xs="24">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>个人信息</span>
            </div>
          </template>
          <div class="text-center">
            <UserAvatar />
          </div>
          <ul class="list-group">
            <li class="list-group-item">
              <el-icon><User /></el-icon>
              用户账号
              <div class="pull-right">{{ form.user.userAccount }}</div>
            </li>
            <li class="list-group-item">
              <el-icon><UserFilled /></el-icon>
              用户昵称
              <div class="pull-right">{{ form.user.userName }}</div>
            </li>
            <li class="list-group-item">
              <el-icon><Management /></el-icon>
              用户角色
              <div class="pull-right">
                <el-tag :type="form.user.userRole?.toString() === 'admin' ? 'success' : 'info'">
                  {{ form.user.userRole?.toString() === 'admin' ? '管理员' : '普通用户' }}
                </el-tag>
              </div>
            </li>
            <li class="list-group-item">
              <el-icon><Timer /></el-icon>
              创建时间
              <div class="pull-right">
                {{ dayjs(form.user.createTime).format('YYYY-MM-DD HH:mm:ss') }}
              </div>
            </li>
          </ul>
        </el-card>
      </el-col>

      <!-- 右侧选项卡 -->
      <el-col :span="18" :xs="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>基本资料</span>
            </div>
          </template>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <UserInfo :user="form.user" />
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <ResetPassword />
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { User, UserFilled, Management, Timer } from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import { getLoginUserUsingGet } from '@/api/userController'
import UserAvatar from '@/components/user/UserAvatar.vue'

const activeTab = ref('userinfo')

// 获取最新的用户信息
const getUser = async () => {
  try {
    const res = await getLoginUserUsingGet()
    if (res?.code === 0 && res?.data) {
      form.user = res.data as API.LoginUserVO
    }
  } catch (error) {
    ElMessage.error('获取用户信息失败')
  }
}

// 基本信息表单
const form = reactive({
  user: {} as API.LoginUserVO,
})

onMounted(() => {
  // 获取最新的用户信息
  getUser()
})
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.box-card {
  margin-bottom: 20px;
}

.card-header {
  font-weight: bold;
}

.list-group {
  padding: 0;
  margin: 0;
  list-style: none;
}

.list-group-item {
  position: relative;
  padding: 12px 0;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.list-group-item:last-child {
  border-bottom: none;
}

.pull-right {
  float: right;
}

.text-center {
  text-align: center;
  margin-bottom: 20px;
}

.avatar-uploader {
  text-align: center;
  margin-bottom: 20px;
}

.upload-tip {
  font-size: 14px;
  color: #666;
  margin-top: 8px;
}

.cursor-pointer {
  cursor: pointer;
}

:deep(.el-icon) {
  margin-right: 8px;
  vertical-align: middle;
}
</style>
