<template>
  <!-- 订单详情对话框 -->
  <el-dialog v-model="detailDialogVisible" title="订单详情" width="60%" destroy-on-close>
    <div v-if="currentOrder" class="order-detail">
      <div class="detail-header">
        <h3>订单号：{{ currentOrder.orderNo }}</h3>
        <el-tag :type="getStatusTag(currentOrder.status)">{{
          getStatusText(currentOrder.status)
        }}</el-tag>
      </div>

      <el-divider />

      <div class="detail-section">
        <h4>基本信息</h4>
        <div class="detail-item">
          <span class="label">下单时间：</span>
          <span>{{ formatDate(currentOrder.createTime) }}</span>
        </div>
        <div class="detail-item">
          <span class="label">付款时间：</span>
          <span>{{ currentOrder.paymentTime ? formatDate(currentOrder.paymentTime) : '未支付' }}</span>
        </div>
        <div class="detail-item">
          <span class="label">订单状态：</span>
          <span>{{ getStatusText(currentOrder.status) }}</span>
        </div>
        <div v-if="currentOrder.cancelReason" class="detail-item">
          <span class="label">取消原因：</span>
          <span>{{ currentOrder.cancelReason }}</span>
        </div>
      </div>

      <el-divider />

      <div class="detail-section">
        <h4>车辆信息</h4>
        <div class="detail-item">
          <span class="label">车辆名称：</span>
          <span>{{ currentOrder.vehicleName || '未知车辆' }}</span>
        </div>
      </div>

      <el-divider />

      <div class="detail-section">
        <h4>租车信息</h4>
        <div class="detail-item">
          <span class="label">租车开始时间：</span>
          <span>{{ formatDate(currentOrder.startTime) }}</span>
        </div>
        <div class="detail-item">
          <span class="label">租车结束时间：</span>
          <span>{{ formatDate(currentOrder.endTime) }}</span>
        </div>
        <div class="detail-item">
          <span class="label">取车门店：</span>
          <span>{{ currentOrder.pickupStoreName || '未指定' }}</span>
        </div>
        <div class="detail-item">
          <span class="label">还车门店：</span>
          <span>{{ currentOrder.returnStoreName || '未指定' }}</span>
        </div>
      </div>

      <el-divider />

      <div class="detail-section">
        <h4>费用信息</h4>
        <div class="detail-item">
          <span class="label">车辆租金：</span>
          <span>¥{{ currentOrder.vehicleTotalAmount || '0.00' }}</span>
        </div>
        <div v-if="currentOrder.needDriver" class="detail-item">
          <span class="label">司机费用：</span>
          <span>¥{{ currentOrder.driverTotalAmount || '0.00' }}</span>
        </div>
        <div class="detail-item">
          <span class="label">订单总价：</span>
          <span class="total-price">¥{{ currentOrder.totalAmount || '0.00' }}</span>
        </div>
      </div>
    </div>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
        <el-button
          v-if="
            currentOrder &&
            currentOrder.status === ORDER_STATUS_ENUM.UNPAID &&
            loginUser?.id === currentOrder.userId
          "
          type="primary"
          @click="handlePay(currentOrder)"
        >
          去支付
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElDialog, ElTag, ElDivider, ElMessage } from 'element-plus'
import ORDER_STATUS_ENUM from '@/enums/OrderStatusEnum'
import { getRentalOrderUsingGet, payOrderUsingGet } from '@/api/rentalOrderController'
import { useLoginUserStore } from '@/stores/useLoginUserStore'

// 当前查看的订单详情
const currentOrder = ref<API.RentalOrderVO | null>(null)
// 详情对话框可见性
const detailDialogVisible = ref(false)

const loading = ref(false)

const loginUserStore = useLoginUserStore()

const loginUser = loginUserStore.loginUser

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

// 格式化日期
const formatDate = (dateString: string | undefined) => {
  if (!dateString) return '未设置'
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
  })
}

// 获取状态文本
const getStatusText = (status: number | undefined) => {
  if (status === undefined) return '未知状态'

  const statusMap: Record<number, string> = {
    [ORDER_STATUS_ENUM.UNPAID]: '待支付',
    [ORDER_STATUS_ENUM.PAID_UNPICKED]: '待取车',
    [ORDER_STATUS_ENUM.PICKED]: '使用中',
    [ORDER_STATUS_ENUM.RETURNED]: '已还车',
    [ORDER_STATUS_ENUM.COMPLETED]: '已完成',
    [ORDER_STATUS_ENUM.CANCELLED]: '已取消',
  }

  return statusMap[status] || '未知状态'
}

const getOrderDetail = async (orderId: string) => {
  try {
    loading.value = true
    const res = await getRentalOrderUsingGet({ orderId: orderId })
    if (res.data?.code === 0 && res.data.data) {
      currentOrder.value = res.data.data
    } else {
      ElMessage.error(res.data?.message || '获取订单详情失败')
    }
  } catch (error) {
    ElMessage.error('获取订单详情失败:' + error)
  } finally {
    loading.value = false
  }
}

// 支付订单
const handlePay = async (order: API.RentalOrderVO) => {
  if (!order.id) {
    ElMessage.error('订单ID不存在')
    return
  }

  try {
    const res = await payOrderUsingGet({ orderId: order.id })
    if (res.data) {
      const div = document.createElement('div')
      div.innerHTML = res.data
      document.body.appendChild(div)
      document.forms[document.forms.length - 1].submit()
    } else {
      ElMessage.error('获取支付链接失败')
    }
  } catch (error) {
    console.error('支付请求失败', error)
    ElMessage.error('支付失败，请稍后再试')
  }
}

const open = (orderId: string) => {
  detailDialogVisible.value = true
  getOrderDetail(orderId)
}

defineExpose({
  open,
})
</script>

<style scoped>
/* 订单详情对话框样式 */
.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.detail-header h3 {
  margin: 0;
  font-size: 18px;
}

.detail-section {
  margin-bottom: 20px;
}

.detail-section h4 {
  font-size: 16px;
  margin-bottom: 12px;
  color: #303133;
}

.detail-item {
  display: flex;
  margin-bottom: 10px;
  font-size: 14px;
}

.detail-item .label {
  font-weight: bold;
  width: 120px;
  color: #606266;
}

.total-price {
  font-weight: bold;
  color: #f56c6c;
  font-size: 16px;
}
</style>
