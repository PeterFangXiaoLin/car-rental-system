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
              <el-icon><Avatar /></el-icon>
              真实姓名
              <div class="pull-right">{{ form.user.realName || '未设置' }}</div>
            </li>
            <li class="list-group-item">
              <el-icon><Iphone /></el-icon>
              手机号码
              <div class="pull-right">{{ form.user.phoneNumber || '未绑定' }}</div>
            </li>
            <li class="list-group-item">
              <el-icon><Message /></el-icon>
              电子邮箱
              <div class="pull-right">{{ form.user.email || '未绑定' }}</div>
            </li>
            <!-- 司机特有信息 -->
            <template v-if="form.user.userRole === USER_ROLE_ENUM.DIRVER">
              <li class="list-group-item">
                <el-icon><Ticket /></el-icon>
                驾驶证号
                <div class="pull-right">{{ form.user.drivingLicenseNo || '未设置' }}</div>
              </li>
              <li class="list-group-item">
                <el-icon><Van /></el-icon>
                驾龄(年)
                <div class="pull-right">{{ form.user.drivingYears || '0' }}</div>
              </li>
              <li class="list-group-item">
                <el-icon><Star /></el-icon>
                信用评分
                <div class="pull-right">
                  <el-rate
                    v-model="creditScore"
                    disabled
                    show-score
                    text-color="#ff9900"
                    score-template="{value}"
                  />
                </div>
              </li>
            </template>
            <li class="list-group-item">
              <el-icon><Management /></el-icon>
              用户角色
              <div class="pull-right">
                <el-tag :type="form.user?.userRole === 3 ? 'success' : 'info'">
                  {{
                    form.user?.userRole === 3
                      ? '管理员'
                      : form.user?.userRole === 2
                        ? '司机'
                        : '普通用户'
                  }}
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
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { User, UserFilled, Management, Timer, Iphone, Message, Avatar, Ticket, Van, Star } from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import { getLoginUserUsingGet } from '@/api/userController'
import UserAvatar from '@/components/user/UserAvatar.vue'
import UserInfo from '@/components/user/UserInfo.vue'
import ResetPassword from '@/components/user/ResetPassword.vue'
import USER_ROLE_ENUM from '@/enums/UserRoleEnum'

const activeTab = ref('userinfo')

// 计算信用评分（满分5分）
const creditScore = computed(() => {
  const score = form.user?.creditScore || 0
  // 假设信用分满分为100，转换为5分制
  return Math.min(5, Math.max(0, score / 2))
})

// 获取最新的用户信息
const getUser = async () => {
  try {
    const res = await getLoginUserUsingGet()
    if (res?.code === 0 && res?.data) {
      form.user = res.data
    }
  } catch (error: unknown) {
    console.error('获取用户信息失败:', error)
    if (error instanceof Error) {
      ElMessage.error(error.message || '获取用户信息失败')
    } else {
      ElMessage.error('获取用户信息失败')
    }
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
</style>
