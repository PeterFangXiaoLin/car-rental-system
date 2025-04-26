<template>
  <el-input
    v-model="selectedDriver.driverName"
    placeholder="请选择司机"
    @click="openDriverDialog"
    readonly
    class="driver-input"
    :prefix-icon="User"
  />
  <el-dialog v-model="dialogVisible" title="选择司机" width="800px" destroy-on-close center>
    <div class="search-container">
      <el-input
        v-model="searchKeyword"
        placeholder="输入司机姓名或电话搜索"
        :prefix-icon="Search"
        clearable
        @keyup.enter="searchDrivers"
      >
        <template #append>
          <el-button @click="searchDrivers">搜索</el-button>
        </template>
      </el-input>
    </div>

    <div class="selected-driver-info" v-if="selectedDriverId">
      <div class="driver-info-header">当前选择的司机</div>
      <div class="driver-info-content">
        <div><span class="label">姓名：</span>{{ selectedDriver.driverName }}</div>
        <div><span class="label">电话：</span>{{ selectedDriver.phoneNumber }}</div>
      </div>
    </div>

    <div class="driver-list-container">
      <el-table
        :data="driverList"
        style="width: 100%"
        height="400"
        border
        highlight-current-row
        @row-click="handleRowClick"
        v-loading="loading"
      >
        <el-table-column type="index" width="50" align="center" />
        <el-table-column prop="driverName" label="姓名" align="center" />
        <el-table-column prop="phoneNumber" label="电话" align="center" />
        <el-table-column prop="age" label="年龄" width="80" align="center" />
        <el-table-column label="性别" width="80" align="center">
          <template #default="{ row }">
            {{ row.gender === 1 ? '男' : row.gender === 2 ? '女' : '未知' }}
          </template>
        </el-table-column>
        <el-table-column label="驾照类型" prop="driverLicenseType" align="center" />
        <el-table-column label="驾龄" prop="drivingYears" width="80" align="center" />
        <el-table-column label="日薪" prop="dailyPrice" width="100" align="center">
          <template #default="{ row }">
            {{ row.dailyPrice ? `¥${row.dailyPrice}` : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.workStatus)">
              {{ getStatusText(row.workStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link @click.stop="selectDriver(row)"> 选择 </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.current"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmSelection" :disabled="!selectedDriverId">
          确定
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { User, Search } from '@element-plus/icons-vue'
import { listDriverVoByPageUsingPost } from '@/api/driverController'

// 司机ID，通过v-model绑定
const driverId = defineModel<string>('driverId', { default: '' })
const dialogVisible = ref(false)

// 当前选择的司机
const selectedDriver = reactive<{
  id: string
  driverName: string
  phoneNumber: string
}>({
  id: '',
  driverName: '',
  phoneNumber: '',
})

// 查询参数
const searchKeyword = ref('')
const queryParams = reactive<API.DriverQueryRequest>({
  current: 1,
  pageSize: 10,
  sortField: 'createTime',
  sortOrder: 'descend',
  driverName: '',
})

// 列表数据
const driverList = ref<API.DriverVO[]>([])
const total = ref(0)
const loading = ref(false)
const selectedDriverId = ref('')

// 打开司机选择对话框
const openDriverDialog = () => {
  dialogVisible.value = true
  fetchDriverList()
}

// 获取司机列表
const fetchDriverList = async () => {
  loading.value = true
  try {
    const res = await listDriverVoByPageUsingPost(queryParams)
    if (res.data?.code === 0 && res.data.data) {
      driverList.value = res.data.data.records || []
      total.value = res.data.data.total || 0

      // 如果已有选中的司机，定位到该司机
      if (driverId.value && !selectedDriverId.value) {
        const driver = driverList.value.find((item) => String(item.id) === driverId.value)
        if (driver) {
          selectDriver(driver)
        }
      }
    } else {
      ElMessage.error('获取司机列表失败:' + (res.data?.message || '未知错误'))
    }
  } catch (error) {
    ElMessage.error('加载司机列表失败:' + error)
  } finally {
    loading.value = false
  }
}

// 搜索司机
const searchDrivers = () => {
  queryParams.driverName = searchKeyword.value
  queryParams.current = 1
  fetchDriverList()
}

// 处理页码变化
const handleCurrentChange = (current: number) => {
  queryParams.current = current
  fetchDriverList()
}

// 处理每页条数变化
const handleSizeChange = (size: number) => {
  queryParams.pageSize = size
  queryParams.current = 1
  fetchDriverList()
}

// 选择司机
const selectDriver = (driver: API.DriverVO) => {
  if (!driver.id) return

  selectedDriverId.value = String(driver.id)
  selectedDriver.id = String(driver.id)
  selectedDriver.driverName = driver.driverName || ''
  selectedDriver.phoneNumber = driver.phoneNumber || ''

  // 更新model值
  driverId.value = selectedDriver.id

  ElMessage.success(`已选择司机: ${driver.driverName}`)
  // 自动关闭对话框
  dialogVisible.value = false
}

// 处理行点击
const handleRowClick = (row: API.DriverVO) => {
  selectDriver(row)
}

// 确认选择
const confirmSelection = () => {
  if (!selectedDriverId.value) {
    ElMessage.warning('请先选择一个司机')
    return
  }

  // 更新model值
  driverId.value = selectedDriver.id

  // 关闭对话框
  dialogVisible.value = false

  ElMessage.success(`已选择司机: ${selectedDriver.driverName}`)
}

// 获取状态文本
const getStatusText = (status?: number): string => {
  switch (status) {
    case 0:
      return '休息中'
    case 1:
      return '可接单'
    case 2:
      return '已接单'
    default:
      return '未知状态'
  }
}

// 获取状态标签类型
const getStatusTagType = (status?: number): '' | 'success' | 'warning' | 'info' | 'danger' => {
  switch (status) {
    case 0:
      return 'info'
    case 1:
      return 'success'
    case 2:
      return 'warning'
    default:
      return ''
  }
}
</script>

<style scoped>
.driver-input {
  width: 100%;
}

.search-container {
  margin-bottom: 15px;
}

.driver-list-container {
  margin-top: 15px;
}

.selected-driver-info {
  margin-bottom: 15px;
  padding: 12px;
  background-color: #f0f9ff;
  border-radius: 4px;
  border-left: 4px solid #409eff;
}

.driver-info-header {
  font-weight: bold;
  font-size: 16px;
  margin-bottom: 8px;
  color: #303133;
}

.driver-info-content {
  line-height: 1.8;
  color: #606266;
}

.driver-info-content .label {
  font-weight: bold;
  margin-right: 5px;
}

.pagination-container {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
}
</style>
