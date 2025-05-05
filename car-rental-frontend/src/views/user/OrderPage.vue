<template>
  <div class="order-page">
    <el-card class="order-card">
      <template #header>
        <div class="card-header">
          <span class="title">我的订单</span>
          <div class="search-box">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索订单号、车辆信息..."
              class="search-input"
              clearable
            >
              <template #suffix>
                <el-icon class="search-icon" @click="handleSearch">
                  <search />
                </el-icon>
              </template>
            </el-input>
            <el-button type="primary" @click="handleSearch" style="margin-left: 10px"
              >搜索
            </el-button>
          </div>
        </div>
      </template>

      <!-- 订单状态标签页 -->
      <el-tabs v-model="activeStatus" @tab-click="handleTabClick">
        <el-tab-pane label="全部订单" name="ALL">
          <OrderList :orders="orderList" @refresh="fetchOrders" />
        </el-tab-pane>
        <el-tab-pane label="待支付" name="UNPAID">
          <order-list :orders="orderList" @refresh="fetchOrders" />
        </el-tab-pane>
        <el-tab-pane label="待取车" name="PAID_UNPICKED">
          <order-list :orders="orderList" @refresh="fetchOrders" />
        </el-tab-pane>
        <el-tab-pane label="使用中" name="PICKED">
          <order-list :orders="orderList" @refresh="fetchOrders" />
        </el-tab-pane>
        <el-tab-pane label="已还车" name="RETURNED">
          <order-list :orders="orderList" @refresh="fetchOrders" />
        </el-tab-pane>
        <el-tab-pane label="已完成" name="COMPLETED">
          <order-list :orders="orderList" @refresh="fetchOrders" />
        </el-tab-pane>
        <el-tab-pane label="已取消" name="CANCELLED">
          <order-list :orders="orderList" @refresh="fetchOrders" />
        </el-tab-pane>
      </el-tabs>

      <!-- 加载中状态 -->
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="5" animated />
      </div>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 30, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import OrderList from '@/components/order/OrderList.vue'
import { pageMyRentalOrderUsingPost } from '@/api/rentalOrderController'
import ORDER_STATUS_ENUM from '@/enums/OrderStatusEnum'

// 搜索关键词
const searchKeyword = ref('')
// 当前激活的状态标签
const activeStatus = ref('ALL')
// 分页参数
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
// 订单数据
const orderList = ref<API.RentalOrderVO[]>([])
// 加载状态
const loading = ref(false)

// 获取订单列表
const fetchOrders = async () => {
  loading.value = true
  try {
    // 将枚举值转换为对应的数字状态码
    let statusCode = undefined
    if (activeStatus.value !== 'ALL') {
      statusCode = ORDER_STATUS_ENUM[activeStatus.value]
    }

    const res = await pageMyRentalOrderUsingPost({
      current: currentPage.value,
      pageSize: pageSize.value,
      sortField: 'createTime',
      sortOrder: 'descend',
      // 根据标签过滤状态
      status: statusCode,
      // 搜索关键词
      searchText: searchKeyword.value || undefined,
    })

    if (res.data?.code === 0 && res.data.data) {
      orderList.value = res.data.data.records || []
      total.value = Number(res.data.data.total) || 0
    } else {
      ElMessage.error('获取订单列表失败')
    }
  } catch (error) {
    ElMessage.error('获取订单列表失败')
  } finally {
    loading.value = false
  }
}

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchOrders()
}

// 处理标签页切换
const handleTabClick = () => {
  currentPage.value = 1
  fetchOrders()
}

// 处理页大小变化
const handleSizeChange = (size: number) => {
  pageSize.value = size
  fetchOrders()
}

// 处理页码变化
const handleCurrentChange = (page: number) => {
  currentPage.value = page
  fetchOrders()
}

// 组件挂载时获取订单
onMounted(() => {
  fetchOrders()
  console.log(orderList.value)
})
</script>

<style scoped>
.order-page {
  padding: 20px;
}

.order-card {
  width: 100%;
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  font-size: 18px;
  font-weight: bold;
}

.search-box {
  display: flex;
  align-items: center;
}

.search-input {
  width: 300px;
}

.search-icon {
  cursor: pointer;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.loading-container {
  padding: 20px 0;
}
</style>
