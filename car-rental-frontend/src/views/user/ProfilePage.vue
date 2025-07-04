<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 左侧个人信息卡片 -->
      <el-col :span="6" :xs="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>个人信息</span>
            </div>
          </template>
          <div class="text-center">
            <UserAvatar />
          </div>
          <ul class="list-group list-group-striped">
            <li class="list-group-item">
              <el-icon>
                <User />
              </el-icon>
              用户账号
              <div class="pull-right">{{ form.user.userAccount }}</div>
            </li>
            <li class="list-group-item">
              <el-icon>
                <UserFilled />
              </el-icon>
              用户昵称
              <div class="pull-right">{{ form.user.userName }}</div>
            </li>
            <li class="list-group-item">
              <el-icon>
                <Iphone />
              </el-icon>
              手机号码
              <div class="pull-right">{{ form.user.phoneNumber || '未绑定' }}</div>
            </li>
            <li class="list-group-item">
              <el-icon>
                <Message />
              </el-icon>
              电子邮箱
              <div class="pull-right">{{ form.user.email || '未绑定' }}</div>
            </li>
            <li class="list-group-item">
              <el-icon>
                <Management />
              </el-icon>
              用户角色
              <div class="pull-right">
                <el-tag :type="form.user?.userRole === UserRoleEnum.ADMIN ? 'success' : 'info'">
                  {{ form.user?.userRole === UserRoleEnum.ADMIN ? '管理员' : '普通用户' }}
                </el-tag>
              </div>
            </li>
            <li class="list-group-item">
              <el-icon>
                <Timer />
              </el-icon>
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
              <UserInfo :user="form.user" @update="handleUserUpdate" />
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
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Iphone, Management, Message, Timer, User, UserFilled } from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import { getUserByIdUsingGet } from '@/api/userController'
import UserAvatar from '@/components/user/UserAvatar.vue'
import UserInfo from '@/components/user/UserInfo.vue'
import ResetPassword from '@/components/user/ResetPassword.vue'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import router from '@/router'
import UserRoleEnum from '@/enums/UserRoleEnum.ts'

const activeTab = ref('userinfo')

const loginUserStore = useLoginUserStore()

// 获取最新的用户信息
const getUser = async () => {
  const loginUser = loginUserStore.loginUser
  if (!loginUser?.id) {
    ElMessage.error('用户未登录')
    router.replace('/auth/login')
    return
  }
  try {
    const res = await getUserByIdUsingGet({
      id: loginUser.id,
    })
    if (res.data?.code === 0 && res.data?.data) {
      form.user = res.data.data
    } else {
      ElMessage.error(res.data?.message || '获取用户信息失败')
    }
  } catch (error) {
    ElMessage.error('获取用户信息失败' + error)
  }
}

// 基本信息表单
const form = reactive({
  user: {} as API.UserVO,
})

// 处理用户信息更新
const handleUserUpdate = (updatedUser: API.LoginUserVO) => {
  form.user = updatedUser
}

onMounted(() => {
  // 获取最新的用户信息
  getUser()
})
</script>

<style scoped>
.card-header:after {
  visibility: hidden;
  display: block;
  font-size: 0;
  content: ' ';
  clear: both;
  height: 0;
}

.list-group {
  padding-left: 0;
  list-style: none;
}

.list-group-striped > .list-group-item {
  border-left: 0;
  border-right: 0;
  border-radius: 0;
  padding-left: 0;
  padding-right: 0;
}

.list-group-item {
  border-bottom: 1px solid #e7eaec;
  border-top: 1px solid #e7eaec;
  margin-bottom: -1px;
  padding: 11px 0;
  font-size: 14px;
  display: flex;
  align-items: center;
}

.pull-right {
  margin-left: auto;
}

.text-center {
  text-align: center;
}

:deep(.el-icon) {
  margin-right: 8px;
  vertical-align: middle;
  font-size: 14px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

/* 确保图片预览在最顶层 */
:deep(.el-image-viewer__wrapper) {
  z-index: 2100 !important;
}

:deep(.el-image-viewer__mask) {
  z-index: 2100 !important;
}

:deep(.el-overlay) {
  z-index: 2000 !important;
}
</style>
