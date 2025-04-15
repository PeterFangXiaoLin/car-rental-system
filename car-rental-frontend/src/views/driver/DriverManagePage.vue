<template>
  <div>
    <!-- 搜索表单 -->
    <el-card shadow="never" class="mb-15px">
      <div>
        <el-form :model="searchParams" class="-mb-15px" label-width="78px" size="large">
          <el-row>
            <el-col :span="6">
              <el-form-item label="司机姓名">
                <el-input
                  v-model="searchParams.driverName"
                  placeholder="请输入司机姓名"
                  clearable
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="性别">
                <el-select
                  v-model="searchParams.gender"
                  placeholder="请选择性别"
                  clearable
                  size="large"
                >
                  <el-option label="男" :value="0" />
                  <el-option label="女" :value="1" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="驾照类型">
                <el-select
                  v-model="searchParams.driverLicenseType"
                  placeholder="请选择驾照类型"
                  clearable
                  size="large"
                >
                  <el-option label="A1" value="A1" />
                  <el-option label="A2" value="A2" />
                  <el-option label="B1" value="B1" />
                  <el-option label="B2" value="B2" />
                  <el-option label="C1" value="C1" />
                  <el-option label="C2" value="C2" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="最小日薪">
                <el-input-number
                  v-model="searchParams.minPrice"
                  :min="0"
                  :precision="2"
                  :step="50"
                  placeholder="最小日薪"
                  size="large"
                  class="w-full"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="最大日薪">
                <el-input-number
                  v-model="searchParams.maxPrice"
                  :min="0"
                  :precision="2"
                  :step="50"
                  placeholder="最大日薪"
                  size="large"
                  class="w-full"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="工作状态">
                <el-select v-model="searchParams.workStatus" placeholder="请选择工作状态" clearable>
                  <el-option label="休息中" :value="0" />
                  <el-option label="可接单" :value="1" />
                  <el-option label="已接单" :value="2" />
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
      <!-- 司机表格 -->
      <el-table
        :data="dataList"
        style="width: 100%"
        v-loading="loading"
        :header-cell-style="{ 'background-color': '#ecf8fe', color: '#4986EA' }"
      >
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column prop="driverName" label="司机姓名" width="120" align="center" />
        <el-table-column label="头像" width="120" align="center">
          <template #default="{ row }">
            <el-image
              :src="row.driverAvatar"
              :preview-src-list="[row.driverAvata]"
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
        <el-table-column label="年龄" width="100" align="center" prop="age" />
        <el-table-column label="联系电话" width="120" align="center" prop="phoneNumber" />
        <el-table-column label="驾驶证号码" width="120" align="center" prop="driverLicenseNo" />
        <el-table-column label="驾驶证类型" width="120" align="center" prop="driverLicenseType" />
        <el-table-column label="驾驶证照片" width="120" align="center">
          <template #default="{ row }">
            <el-image
              :src="row.driverLicenseImg"
              :preview-src-list="[row.driverLicenseImg]"
              fit="cover"
              style="width: 80px; height: 80px"
              :preview-teleported="true"
              :initial-index="0"
            />
          </template>
        </el-table-column>
        <el-table-column label="发证日期" width="120" align="center">
          <template #default="{ row }">
            {{
              row.driverLicenseIssueDate
                ? dayjs(row.driverLicenseIssueDate).format('YYYY-MM-DD')
                : '-'
            }}
          </template>
        </el-table-column>
        <el-table-column label="到期日期" width="120" align="center">
          <template #default="{ row }">
            {{
              row.driverLicenseExpireDate
                ? dayjs(row.driverLicenseExpireDate).format('YYYY-MM-DD')
                : '-'
            }}
          </template>
        </el-table-column>
        <el-table-column label="驾龄" width="60" align="center">
          <template #default="{ row }"> {{ row.driverExperienceYears }} 年 </template>
        </el-table-column>
        <el-table-column label="日薪" width="100" align="center">
          <template #default="{ row }">
            <div class="color-#f56c6c">{{ row.dailyPrice }} 元/天</div>
          </template>
        </el-table-column>
        <el-table-column label="工作状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.workStatus === DriverWorkStatusEnum.RESTING"> 休息中 </el-tag>
            <el-tag v-else-if="row.workStatus === DriverWorkStatusEnum.AVAILABLE"> 可接单 </el-tag>
            <el-tag v-else-if="row.workStatus === DriverWorkStatusEnum.ON_ORDER"> 已接单 </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="评分" width="100" align="center">
          <template #default="{ row }">
            <el-rate v-model="row.rating" :max="5" readonly />
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

  <DriverAddForm ref="addFormRef"/>
  <DriverUpdateForm ref="updateFormRef"/>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'
import { Delete, Edit, Plus, Search, View } from '@element-plus/icons-vue'
import UserInfoForm from '@/components/admin/UserInfoForm.vue'
import GenderEnum from '@/enums/GenderEnum.ts'
import DriverWorkStatusEnum from '@/enums/DriverWorkStatusEnum.ts'
import { deleteDriverUsingPost, listDriverVoByPageUsingPost } from '@/api/driverController.ts'
import DriverAddForm from "@/views/driver/DriverAddForm.vue";
import DriverUpdateForm from "@/views/driver/DriverUpdateForm.vue";

const loading = ref(false)
const dataList = ref<API.UserVO[]>([])
const total = ref(0)

const addFormRef = ref()
const updateFormRef = ref()

// 搜索参数
const searchParams = reactive<API.DriverQueryRequest>({
  current: 1,
  pageSize: 10,
  sortField: 'createTime',
  sortOrder: 'ascend',
})

// 获取用户列表数据
const fetchData = async () => {
  loading.value = true
  try {
    const res = await listDriverVoByPageUsingPost(searchParams)
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
    await ElMessageBox.confirm('确定要删除该司机吗？', '提示', {
      type: 'warning',
    })
    const res = await deleteDriverUsingPost({ id: id })
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

// 编辑
const handleEdit = (id: string) => {
  updateFormRef.value?.open(id)
}

// 查看
const handleView = (id: string) => {
  updateFormRef.value?.open(id)
}

// 成功
const success = (msg: string) => {
  ElMessage.success(msg)
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
