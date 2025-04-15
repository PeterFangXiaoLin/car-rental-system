<template>
  <div class="user-manage-container">
    <!-- 搜索表单 -->
    <el-card shadow="never" class="mb-15px">
      <div>
        <el-form :model="searchParams" class="-mb-15px" label-width="78px" size="large">
          <el-row>
            <el-col :span="6">
              <el-form-item label="账号">
                <el-input v-model="searchParams.userAccount" placeholder="请输入账号" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="用户名">
                <el-input v-model="searchParams.userName" placeholder="请输入用户名" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="简介">
                <el-input v-model="searchParams.userProfile" placeholder="请输入简介" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="性别">
                <el-select v-model="searchParams.gender" placeholder="请选择性别" clearable>
                  <el-option label="男" :value="0" />
                  <el-option label="女" :value="1" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="会员状态">
                <el-select
                  v-model="searchParams.memberLevel"
                  placeholder="请选择会员状态"
                  clearable
                >
                  <el-option label="vip" :value="1" />
                  <el-option label="普通用户" :value="0" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="用户角色">
                <el-select v-model="searchParams.userRole" placeholder="请选择用户角色" clearable>
                  <el-option label="管理员" :value="UserRoleEnum.ADMIN" />
                  <el-option label="普通用户" :value="UserRoleEnum.USER" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item>
                <div class="flex">
                  <el-button type="primary" :icon="Search" @click="doSearch">搜索</el-button>
                  <el-button plain type="primary" :icon="Plus" @click="openForm()">新增</el-button>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </el-card>

    <el-card shadow="never" class="mb-15px">
      <!-- 用户表格 -->
      <el-table
        :data="dataList"
        style="width: 100%"
        v-loading="loading"
        :header-cell-style="{ 'background-color': '#ecf8fe', color: '#4986EA' }"
      >
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column prop="userAccount" label="账号" width="120" align="center" />
        <el-table-column prop="userName" label="用户名" width="120" align="center" />
        <el-table-column label="头像" width="120" align="center">
          <template #default="{ row }">
            <el-image
              :src="row.userAvatar"
              :preview-src-list="[row.userAvatar]"
              fit="cover"
              style="width: 80px; height: 80px"
              :preview-teleported="true"
              :initial-index="0"
            />
          </template>
        </el-table-column>
        <el-table-column label="性别" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.gender === GenderEnum.MALE ? 'success' : 'primary'">
              {{ row.gender === GenderEnum.MALE ? '男' : '女' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="userProfile" label="简介" align="center" />
        <el-table-column prop="phoneNumber" label="手机号" align="center" />
        <el-table-column prop="email" label="邮箱" align="center" />
        <el-table-column label="会员状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.memberLevel === 1 ? 'success' : 'primary'">
              {{ row.memberStatus === 1 ? 'vip' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="用户角色" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.userRole === UserRoleEnum.ADMIN ? 'success' : 'info'">
              {{ row.userRole === USER_ROLE_ENUM.ADMIN ? '管理员' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="180" align="center">
          <template #default="{ row }">
            {{ dayjs(row.createTime).format('YYYY-MM-DD HH:mm:ss') }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row.id)">
              <el-icon>
                <Edit />
              </el-icon>
              编辑
            </el-button>
            <el-button link type="primary" @click="handleView(row.id)">
              <el-icon>
                <View />
              </el-icon>
              查看
            </el-button>
            <el-button type="danger" link @click="handleDelete(row.id)">
              <el-icon>
                <Delete />
              </el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="searchParams.current"
          v-model:page-size="searchParams.pageSize"
          background
          :total="total"
          :page-sizes="[10, 20, 30]"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>

  <UserAddForm ref="addFormRef" @success="success" />
  <UserUpdateForm ref="updateFormRef" @success="success" />
  <UserViewForm ref="viewFormRef" @success="success" />
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminDeleteUserUsingPost, pageUserVoUsingPost } from '@/api/userController'
import dayjs from 'dayjs'
import { Delete, Edit, Plus, Search, View } from '@element-plus/icons-vue'
import USER_ROLE_ENUM from '../../enums/UserRoleEnum.ts'
import UserRoleEnum from '../../enums/UserRoleEnum.ts'
import GenderEnum from '@/enums/GenderEnum.ts'
import UserAddForm from '@/components/user/UserAddForm.vue'
import UserUpdateForm from '@/components/user/UserUpdateForm.vue'
import UserViewForm from '@/components/user/UserViewForm.vue'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'

const loading = ref(false)
const dataList = ref<API.UserVO[]>([])
const total = ref(0)

const loginUserStore = useLoginUserStore()

const addFormRef = ref()
const updateFormRef = ref()
const viewFormRef = ref()

// 搜索参数
const searchParams = reactive<API.UserQueryRequest>({
  current: 1,
  pageSize: 10,
  sortField: 'createTime',
  sortOrder: 'ascend',
  userAccount: '',
  userName: '',
})

// 获取用户列表数据
const fetchData = async () => {
  loading.value = true
  try {
    const res = await pageUserVoUsingPost(searchParams)
    if (res.data?.code === 0 && res.data.data) {
      dataList.value = res.data.data.records ?? []
      total.value = Number(res.data.data.total) || 0
    } else {
      ElMessage.error(res.data.message || '获取数据失败')
    }
  } catch (error) {
    ElMessage.error('获取数据失败：' + error)
  } finally {
    loading.value = false
  }
}

// 新增
const openForm = () => {
  addFormRef.value?.open()
}

// 搜索
const doSearch = () => {
  searchParams.current = 1
  fetchData()
}

// 处理页码变化
const handleCurrentChange = (current: number) => {
  searchParams.current = current
  fetchData()
}

// 处理每页条数变化
const handleSizeChange = (size: number) => {
  searchParams.pageSize = size
  searchParams.current = 1
  fetchData()
}

// 删除用户
const handleDelete = async (id: string) => {
  try {
    await ElMessageBox.confirm('确定要删除该用户吗？', '提示', {
      type: 'warning',
    })

    // 当前登录用户不能删除自己
    const loginUser = loginUserStore.loginUser
    if (loginUser?.id == id) {
      ElMessage.error('不能删除自己')
      return
    }
    const res = await adminDeleteUserUsingPost({ id })
    if (res.data.code === 0) {
      ElMessage.success('删除成功')
      await fetchData()
    } else {
      ElMessage.error(res.data.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败：' + error)
    }
  }
}

// 编辑和查看用户
const handleEdit = (id: string) => {
  updateFormRef.value?.open(id)
}
const handleView = (id: string) => {
  viewFormRef.value?.open(id)
}

// 编辑成功
const success = (msg: string) => {
  fetchData()
}

// 页面加载时获取数据
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
