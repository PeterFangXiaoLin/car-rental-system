<template>
  <div class="order-list">
    <div v-if="!orders || orders.length === 0" class="empty-data">
      <el-empty description="暂无订单数据" />
    </div>
    <div v-else>
      <!-- 订单列表 -->
      <div v-for="order in orders" :key="order.id" class="order-item">
        <div class="order-header">
          <div class="order-info">
            <span class="order-no">订单号：{{ order.orderNo }}</span>
            <span class="order-time">下单时间：{{ formatDate(order.createTime) }}</span>
          </div>
          <div class="order-status">
            <el-tag :type="getStatusTag(order.status)">{{ getStatusText(order.status) }}</el-tag>
          </div>
        </div>

        <div class="order-content">
          <div class="vehicle-info">
            <el-image
              v-if="order.vehicleImage"
              :src="order.vehicleImage"
              fit="cover"
              class="vehicle-image"
              @click="navigateToVehicleDetail(order.vehicleId)"
            />
            <div v-else class="vehicle-image-placeholder">
              <el-icon><picture-filled /></el-icon>
            </div>

            <div class="vehicle-details">
              <h3 class="vehicle-name">
                <a href="javascript:;" @click="navigateToVehicleDetail(order.vehicleId)">
                  {{ order.vehicleName || '未知车辆' }}
                </a>
              </h3>
              <p class="rental-period">租车开始时间：{{ formatDate(order.startTime) }}</p>
              <p class="rental-period">租车结束时间：{{ formatDate(order.endTime) }}</p>
              <p class="store-info">取车门店：{{ order.pickupStoreName || '未指定' }}</p>
              <p class="store-info">还车门店：{{ order.returnStoreName || '未指定' }}</p>
            </div>
          </div>

          <div class="order-amount">
            <div class="price-info">
              <span class="price-label">总价</span>
              <span class="price-value">¥{{ order?.totalAmount || '未知' }}</span>
            </div>
          </div>
        </div>

        <div class="order-footer">
          <el-button
            v-if="order.status === ORDER_STATUS_ENUM.UNPAID"
            type="primary"
            @click="handlePay(order)"
          >
            去支付
          </el-button>

          <el-button v-if="order.status === ORDER_STATUS_ENUM.UNPAID" @click="handleCancel(order)">
            取消订单
          </el-button>

          <el-button
            v-if="order.status === ORDER_STATUS_ENUM.PAID_UNPICKED"
            type="success"
            @click="handlePickup(order)"
          >
            取车
          </el-button>

          <el-button
            v-if="order.status === ORDER_STATUS_ENUM.PAID_UNPICKED"
            type="danger"
            plain
            :loading="refundingOrderId === String(order.id)"
            @click="handleRefund(order)"
          >
            申请退款
          </el-button>

          <el-button
            v-if="order.status === ORDER_STATUS_ENUM.PICKED"
            type="warning"
            @click="handleReturn(order)"
          >
            还车
          </el-button>

          <el-button
            v-if="order.status === ORDER_STATUS_ENUM.RETURNED"
            type="primary"
            plain
            @click="handleComment(order)"
          >
            评价
          </el-button>

          <el-button
            v-if="order.status === ORDER_STATUS_ENUM.COMPLETED"
            type="info"
            plain
            @click="handleAddComment(order)"
          >
            追加评论
          </el-button>

          <el-button @click="viewOrderDetail(order)">查看详情</el-button>

          <el-button
            v-if="
              order.status === ORDER_STATUS_ENUM.COMPLETED ||
              order.status === ORDER_STATUS_ENUM.CANCELLED
            "
            type="danger"
            @click="handleDelete(order)"
          >
            删除订单
          </el-button>
        </div>
      </div>
    </div>
  </div>

  <OrderViewForm ref="orderViewFormRef" />
  <CommentForm ref="commentFormRef" @refresh="emit('refresh')" />
</template>

<script setup lang="ts">
import { ElMessage, ElMessageBox } from 'element-plus'
import { ref } from 'vue'
import { PictureFilled } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import {
  payOrderUsingGet,
  cancelRentalOrderUsingPost,
  deleteRentalOrderUsingPost,
  pickupVehicleUsingGet,
  returnVehicleUsingGet,
  refundOrderUsingPost,
} from '@/api/rentalOrderController'
import ORDER_STATUS_ENUM from '@/enums/OrderStatusEnum'
import OrderViewForm from './OrderViewForm.vue'
import CommentForm from './CommentForm.vue'

const router = useRouter()

defineProps({
  orders: {
    type: Array as () => API.RentalOrderVO[],
    default: () => [],
  },
})

const orderViewFormRef = ref<InstanceType<typeof OrderViewForm>>()
const commentFormRef = ref<InstanceType<typeof CommentForm>>()

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
    [ORDER_STATUS_ENUM.REFUNDED]: '已退款',
  }

  return statusMap[status] || '未知状态'
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

// 取消订单
const handleCancel = async (order: API.RentalOrderVO) => {
  if (!order.id) {
    ElMessage.error('订单ID不存在')
    return
  }

  try {
    await ElMessageBox.confirm('确定要取消此订单吗？', '取消订单', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })

    const res = await cancelRentalOrderUsingPost({ orderId: order.id })
    if (res.data?.code === 0 && res.data.data) {
      ElMessage.success('订单已取消')
      // 通知父组件刷新数据，通过事件
      emit('refresh')
    } else {
      ElMessage.error(res.data?.message || '取消订单失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消订单失败', error)
      ElMessage.error('取消订单失败，请稍后再试')
    }
  }
}

// 删除订单
const handleDelete = async (order: API.RentalOrderVO) => {
  if (!order.id) {
    ElMessage.error('订单ID不存在')
    return
  }

  try {
    await ElMessageBox.confirm('确定要删除此订单吗？删除后不可恢复', '删除订单', {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning',
    })

    const res = await deleteRentalOrderUsingPost({ id: order.id })
    if (res.data?.code === 0 && res.data.data) {
      ElMessage.success('订单已删除')
      // 通知父组件刷新数据
      emit('refresh')
    } else {
      ElMessage.error(res.data?.message || '删除订单失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除订单失败', error)
      ElMessage.error('删除订单失败，请稍后再试')
    }
  }
}

// 查看订单详情
const viewOrderDetail = (order: API.RentalOrderVO) => {
  if (!order.id) {
    ElMessage.error('订单ID不存在')
    return
  }
  orderViewFormRef.value?.open(String(order.id))
}

// 取车
const handlePickup = async (order: API.RentalOrderVO) => {
  if (!order.id) {
    ElMessage.error('订单ID不存在')
    return
  }

  try {
    await ElMessageBox.confirm('确定要取车吗？取车后订单状态将变更为"使用中"', '取车确认', {
      confirmButtonText: '确定取车',
      cancelButtonText: '取消',
      type: 'info',
    })

    const res = await pickupVehicleUsingGet({ orderId: order.id })
    if (res.data?.code === 0 && res.data.data) {
      ElMessage.success('取车成功')
      // 通知父组件刷新数据
      emit('refresh')
    } else {
      ElMessage.error(res.data?.message || '取车失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取车失败', error)
      ElMessage.error('取车操作失败，请稍后再试')
    }
  }
}

// 还车
const handleReturn = async (order: API.RentalOrderVO) => {
  if (!order.id) {
    ElMessage.error('订单ID不存在')
    return
  }

  try {
    await ElMessageBox.confirm('确定要归还车辆吗？还车后订单状态将变更为"已还车"', '还车确认', {
      confirmButtonText: '确定还车',
      cancelButtonText: '取消',
      type: 'warning',
    })

    const res = await returnVehicleUsingGet({ orderId: order.id })
    if (res.data?.code === 0 && res.data.data) {
      ElMessage.success('还车成功')
      // 通知父组件刷新数据
      emit('refresh')
    } else {
      ElMessage.error(res.data?.message || '还车失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('还车失败', error)
      ElMessage.error('还车操作失败，请稍后再试')
    }
  }
}

// 评价订单
const handleComment = (order: API.RentalOrderVO) => {
  if (!order.id) {
    ElMessage.error('订单ID不存在')
    return
  }
  commentFormRef.value?.open(String(order.id))
}

// 追加评论
const handleAddComment = (order: API.RentalOrderVO) => {
  if (!order.id) {
    ElMessage.error('订单ID不存在')
    return
  }
  // 第二个参数传入1，表示这是追加评论
  commentFormRef.value?.open(String(order.id), 1)
}

// 跳转到车辆详情页
const navigateToVehicleDetail = (vehicleId: string | number | undefined) => {
  if (!vehicleId) {
    ElMessage.warning('车辆信息不存在')
    return
  }
  router.push(`/vehicle/${vehicleId}`)
}

// 存储当前正在退款的订单ID
const refundingOrderId = ref('')

// 处理退款
const handleRefund = async (order: API.RentalOrderVO) => {
  if (!order.id) {
    ElMessage.error('订单ID不存在')
    return
  }

  try {
    await ElMessageBox.confirm('确定要申请退款吗？申请后将无法取消', '退款确认', {
      confirmButtonText: '确定退款',
      cancelButtonText: '取消',
      type: 'warning',
    })

    // 设置正在退款的订单ID
    refundingOrderId.value = String(order.id)

    try {
      const res = await refundOrderUsingPost({ orderId: order.id })
      if (res.data?.code === 0 && res.data.data) {
        ElMessage.success('退款申请成功')
        // 通知父组件刷新数据
        emit('refresh')
      } else {
        ElMessage.error(res.data?.message || '退款申请失败')
      }
    } finally {
      // 清除退款状态
      refundingOrderId.value = ''
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('退款申请失败', error)
      ElMessage.error('退款申请失败，请稍后再试')
    }
  }
}

// 定义事件
const emit = defineEmits(['refresh'])
</script>

<style scoped>
.order-list {
  margin-top: 15px;
}

.order-item {
  margin-bottom: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  background-color: #fff;
  overflow: hidden;
}

.order-header {
  padding: 15px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #f5f7fa;
  border-bottom: 1px solid #e6ebf5;
}

.order-info {
  display: flex;
  gap: 20px;
}

.order-no {
  font-weight: bold;
}

.order-content {
  padding: 20px;
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid #e6ebf5;
}

.vehicle-info {
  display: flex;
  gap: 20px;
  flex: 1;
}

.vehicle-image,
.vehicle-image-placeholder {
  width: 180px;
  height: 150px;
  border-radius: 4px;
  background-color: #f5f7fa;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
}

.vehicle-image {
  cursor: pointer;
  transition: opacity 0.3s;
}

.vehicle-image:hover {
  opacity: 0.8;
}

.vehicle-image-placeholder .el-icon {
  font-size: 30px;
  color: #909399;
}

.vehicle-details {
  flex: 1;
}

.vehicle-name {
  margin: 0 0 10px 0;
  font-size: 16px;
}

.vehicle-name a {
  color: #409eff;
  text-decoration: none;
  transition: color 0.3s;
}

.vehicle-name a:hover {
  color: #66b1ff;
  text-decoration: underline;
}

.rental-period,
.store-info {
  margin: 5px 0;
  font-size: 14px;
  color: #606266;
}

.order-amount {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-end;
  min-width: 120px;
}

.price-info {
  text-align: right;
}

.price-label {
  font-size: 14px;
  color: #909399;
  margin-right: 5px;
}

.price-value {
  font-size: 18px;
  font-weight: bold;
  color: #f56c6c;
}

.order-footer {
  padding: 15px 20px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.empty-data {
  margin: 40px 0;
}
</style>
