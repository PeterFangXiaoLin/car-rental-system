<template>
  <div>
    <!-- 搜索表单 -->
    <el-card shadow="never" class="mb-15px">
      <div>
        <el-form :model="searchForm" class="-mb-15px" label-width="78px" size="large">
          <el-row>
            <el-col :span="5">
              <el-form-item label="订单号">
                <el-input v-model="searchForm.orderNo" placeholder="请输入订单号" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="用户名">
                <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="车辆名称">
                <el-input v-model="searchForm.vehicleName" placeholder="请输入车辆名称" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="订单状态">
                <el-select
                  v-model="searchForm.status"
                  placeholder="请选择订单状态"
                  clearable
                  size="large"
                >
                  <el-option
                    v-for="(value, key) in statusOptions"
                    :key="key"
                    :label="value"
                    :value="Number(key)"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item class="search-buttons">
                <div class="flex">
                  <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </el-card>

    <!-- 表格区域 -->
    <el-card shadow="never" class="mb-15px">
      <el-table
        v-loading="tableLoading"
        :data="tableData"
        style="width: 100%"
        :header-cell-style="{ 'background-color': '#ecf8fe', color: '#4986EA' }"
      >
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column prop="orderNo" label="订单号" min-width="130" align="center" />
        <el-table-column prop="userName" label="用户名" min-width="100" align="center" />
        <el-table-column prop="vehicleName" label="车辆名称" min-width="130" align="center" />
        <el-table-column label="订单状态" min-width="100" align="center">
          <template #default="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="订单金额" min-width="100" align="center">
          <template #default="{ row }">
            <div class="color-#f56c6c">{{ row.totalAmount }} 元</div>
          </template>
        </el-table-column>
        <el-table-column label="租车时间" min-width="180" align="center">
          <template #default="scope">
            {{ formatDate(scope.row.startTime) }} 至 {{ formatDate(scope.row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column label="创建时间" min-width="150" align="center">
          <template #default="scope">
            {{ formatDateTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right" align="center">
          <template #default="scope">
            <el-button link type="primary" @click="viewOrderDetail(scope.row)">
              <el-icon>
                <View />
              </el-icon>
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 订单详情弹窗 -->
    <OrderViewForm ref="orderViewFormRef" />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, View } from '@element-plus/icons-vue'
import { pageRentalOrderUsingPost } from '@/api/rentalOrderController'
import ORDER_STATUS_ENUM from '@/enums/OrderStatusEnum'
import OrderViewForm from '@/components/order/OrderViewForm.vue'

// 订单状态选项
const statusOptions = {
  [ORDER_STATUS_ENUM.UNPAID]: '待支付',
  [ORDER_STATUS_ENUM.PAID_UNPICKED]: '待取车',
  [ORDER_STATUS_ENUM.PICKED]: '使用中',
  [ORDER_STATUS_ENUM.RETURNED]: '已还车',
  [ORDER_STATUS_ENUM.COMPLETED]: '已完成',
  [ORDER_STATUS_ENUM.CANCELLED]: '已取消',
}

// 表格数据
const tableData = ref<API.RentalOrderVO[]>([])
const tableLoading = ref(false)

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 订单详情弹窗引用
const orderViewFormRef = ref<InstanceType<typeof OrderViewForm>>()

// 搜索条件
const searchForm = reactive({
  orderNo: '',
  username: '',
  vehicleName: '',
  status: undefined as number | undefined,
})

// 初始化数据
onMounted(() => {
  fetchOrderList()
})

// 获取订单列表
const fetchOrderList = async () => {
  tableLoading.value = true
  try {
    const res = await pageRentalOrderUsingPost({
      current: currentPage.value,
      pageSize: pageSize.value,
      orderNo: searchForm.orderNo,
      userName: searchForm.username,
      vehicleName: searchForm.vehicleName,
      status: searchForm.status,
    })

    if (res.data?.code === 0 && res.data.data) {
      tableData.value = res.data.data.records || []
      total.value = Number(res.data.data.total) || 0
    } else {
      ElMessage.error(res.data?.message || '获取订单列表失败')
    }
  } catch (error: unknown) {
    console.error('获取订单列表失败', error)
    ElMessage.error('获取订单列表失败')
  } finally {
    tableLoading.value = false
  }
}

// 获取状态文本
const getStatusText = (status: number | undefined) => {
  if (status === undefined) return '未知状态'
  return statusOptions[status] || '未知状态'
}

// 获取状态标签类型
const getStatusTag = (status: number | undefined) => {
  if (status === undefined) return ''

  const tagMap: Record<number, string> = {
    [ORDER_STATUS_ENUM.UNPAID]: 'warning',
    [ORDER_STATUS_ENUM.PAID_UNPICKED]: 'info',
    [ORDER_STATUS_ENUM.PICKED]: 'primary',
    [ORDER_STATUS_ENUM.RETURNED]: 'success',
    [ORDER_STATUS_ENUM.COMPLETED]: 'success',
    [ORDER_STATUS_ENUM.CANCELLED]: 'danger',
  }

  return tagMap[status] || ''
}

// 查看订单详情
const viewOrderDetail = (row: API.RentalOrderVO) => {
  if (!row.id) {
    ElMessage.error('订单ID不存在')
    return
  }
  orderViewFormRef.value?.open(String(row.id))
}

// 格式化日期（年月日）
const formatDate = (dateString: string | undefined) => {
  if (!dateString) return '未设置'
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
  })
}

// 格式化日期时间（年月日时分秒）
const formatDateTime = (dateString: string | undefined) => {
  if (!dateString) return '未设置'
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
  })
}

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchOrderList()
}

// 处理每页显示数量变化
const handleSizeChange = (val: number) => {
  pageSize.value = val
  fetchOrderList()
}

// 处理页码变化
const handleCurrentChange = (val: number) => {
  currentPage.value = val
  fetchOrderList()
}
</script>

<style scoped>
.mb-15px {
  margin-bottom: 15px;
}

.-mb-15px {
  margin-bottom: -15px;
}

.flex {
  display: flex;
}

.search-buttons {
  margin-left: 12px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
