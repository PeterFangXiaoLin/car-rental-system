<template>
  <div id="orderSubmitPage">
    <el-row :gutter="16" class="mb-4">
      <!-- 订单信息与车辆信息 -->
      <el-col :sm="14" :lg="16">
        <el-card header="确认订单信息">
          <div v-loading="loading">
            <!-- 车辆基本信息 -->
            <div class="vehicle-info-container">
              <div class="vehicle-image">
                <el-image :src="vehicle?.imageUrl" fit="cover" class="vehicle-thumbnail" />
              </div>
              <div class="vehicle-details">
                <h3 class="vehicle-name">{{ vehicle?.name ?? '未命名' }}</h3>
                <el-descriptions :column="1" border size="small" class="mb-2">
                  <el-descriptions-item label="车牌号">{{
                    vehicle?.vehicleNo ?? '-'
                  }}</el-descriptions-item>
                  <el-descriptions-item label="车型">{{
                    vehicle?.vehicleTypeName ?? '-'
                  }}</el-descriptions-item>
                  <el-descriptions-item label="座位数">
                    {{ vehicle?.seatCount ?? '-' }}
                    <span v-if="vehicle?.seatCount">座</span>
                  </el-descriptions-item>
                  <el-descriptions-item label="日租金">
                    <span class="color-#f56c6c">{{ vehicle?.dailyPrice ?? '-' }} 元/天</span>
                  </el-descriptions-item>
                </el-descriptions>
              </div>
            </div>
          </div>

          <!-- 租车信息表单 -->
          <div class="rental-form mt-4">
            <h3 class="form-section-title">预订信息</h3>
            <el-form ref="formRef" :model="orderForm" :rules="formRules" label-width="100px">
              <!-- 租车时间 -->
              <el-form-item label="租车时间" prop="rentalPeriod" required>
                <el-date-picker
                  v-model="rentalPeriod"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                  :disabled-date="disabledDate"
                  @change="handleDateChange"
                />
              </el-form-item>

              <!-- 租车天数 -->
              <el-form-item label="租车天数">
                <span class="rental-days">{{ rentalDays }}天</span>
              </el-form-item>

              <!-- 是否需要司机 -->
              <el-form-item label="是否需要司机" prop="needDriver">
                <el-radio-group v-model="orderForm.needDriver" @change="handleDriverChange">
                  <el-radio :label="true">是</el-radio>
                  <el-radio :label="false">否</el-radio>
                </el-radio-group>
              </el-form-item>

              <!-- 选择司机 -->
              <el-form-item v-if="orderForm.needDriver" label="选择司机" prop="driverId" required>
                <el-select v-model="orderForm.driverId" placeholder="请选择司机">
                  <el-option
                    v-for="driver in driverOptions"
                    :key="driver.id"
                    :label="driver.name"
                    :value="driver.id"
                  />
                </el-select>
              </el-form-item>

              <!-- 取车地点 -->
              <el-form-item label="取车地点" prop="pickupLocation" required>
                <el-input v-model="orderForm.pickupLocation" placeholder="请输入取车地点" />
              </el-form-item>

              <!-- 还车地点 -->
              <el-form-item label="还车地点" prop="returnLocation" required>
                <el-input v-model="orderForm.returnLocation" placeholder="请输入还车地点" />
              </el-form-item>

              <!-- 备注 -->
              <el-form-item label="备注" prop="remark">
                <el-input
                  v-model="orderForm.remark"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入备注信息（选填）"
                />
              </el-form-item>
            </el-form>
          </div>
        </el-card>
      </el-col>

      <!-- 付款详情 -->
      <el-col :sm="10" :lg="8">
        <el-card header="付款详情">
          <div v-loading="loading">
            <!-- 订单金额 -->
            <el-descriptions :column="1" border>
              <el-descriptions-item label="车辆单价">
                <div>{{ vehicle?.dailyPrice ?? 0 }} 元/天</div>
              </el-descriptions-item>
              <el-descriptions-item label="租车天数">
                <div>{{ rentalDays }} 天</div>
              </el-descriptions-item>
              <el-descriptions-item label="车辆费用">
                <div>{{ (vehicle?.dailyPrice ?? 0) * rentalDays }} 元</div>
              </el-descriptions-item>
              <el-descriptions-item v-if="orderForm.needDriver" label="司机费用">
                <div>{{ driverPrice * rentalDays }} 元</div>
              </el-descriptions-item>
              <el-descriptions-item label="总计费用" class="total-amount">
                <div class="color-#f56c6c font-bold">{{ formattedPrice }} 元</div>
              </el-descriptions-item>
            </el-descriptions>

            <!-- 下单 -->
            <div class="submit-section mt-4">
              <el-button
                type="primary"
                size="large"
                @click="handleSubmitOrder"
                :loading="submitting"
                class="submit-button"
              >
                提交订单
              </el-button>
              <div class="payment-terms mt-2">
                <small>点击"提交订单"表示您同意《租车服务条款》</small>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRoute, useRouter } from 'vue-router'
import { getVehicleByIdUsingGet } from '@/api/vehicleController.ts'
import { createRentalOrderUsingPost } from '@/api/rentalOrderController'
import VehicleStatusEnum from '@/enums/VehicleStatusEnum.ts'
import dayjs from 'dayjs'

const router = useRouter()
const route = useRoute()
const loginUserStore = useLoginUserStore()
const formRef = ref()

// 获取URL参数中的车辆ID
const vehicleId = computed(() => {
  return route.query.vehicleId as string
})

const vehicle = ref<API.VehicleVO>()
const loading = ref(false)
const submitting = ref(false)

// 租车日期
const today = dayjs().format('YYYY-MM-DD')
const tomorrow = dayjs().add(1, 'day').format('YYYY-MM-DD')
const rentalPeriod = ref([today, tomorrow])

// 租车天数
const rentalDays = computed(() => {
  if (!rentalPeriod.value || !rentalPeriod.value[0] || !rentalPeriod.value[1]) {
    return 1
  }
  const start = dayjs(rentalPeriod.value[0])
  const end = dayjs(rentalPeriod.value[1])
  return end.diff(start, 'day') + 1
})

// 司机费用（示例值，实际应从API获取）
const driverPrice = 200

// 总价计算
const totalPrice = computed(() => {
  let price = (vehicle.value?.dailyPrice ?? 0) * rentalDays.value
  if (orderForm.value.needDriver) {
    price += driverPrice * rentalDays.value
  }
  return price
})

// 格式化价格显示（保留两位小数）
const formattedPrice = computed(() => {
  return totalPrice.value.toFixed(2)
})

// 订单表单
const orderForm = ref({
  vehicleId: '',
  startDate: today,
  endDate: tomorrow,
  needDriver: false,
  driverId: null,
  pickupLocation: '',
  returnLocation: '',
  remark: '',
})

// 表单验证规则
const formRules = {
  pickupLocation: [{ required: true, message: '请输入取车地点', trigger: 'blur' }],
  returnLocation: [{ required: true, message: '请输入还车地点', trigger: 'blur' }],
  driverId: [{ required: true, message: '请选择司机', trigger: 'change' }],
}

// 禁用的日期（今天之前的日期不可选）
const disabledDate = (time: Date) => {
  return time.getTime() < Date.now() - 8.64e7 // 8.64e7 是一天的毫秒数
}

// 日期变更处理
const handleDateChange = (dates: string[]) => {
  if (dates) {
    orderForm.value.startDate = dates[0]
    orderForm.value.endDate = dates[1]
  }
}

// 司机选择变化处理
const handleDriverChange = (value: boolean) => {
  if (!value) {
    orderForm.value.driverId = null
  }
}

// 获取车辆详情
const fetchVehicleDetail = async () => {
  loading.value = true
  try {
    const res = await getVehicleByIdUsingGet({ id: vehicleId.value })
    if (res.data.code === 0 && res.data.data) {
      vehicle.value = res.data.data
      // 设置车辆ID到表单
      orderForm.value.vehicleId = String(vehicle.value.id)
    } else {
      ElMessage.error('获取车辆详情失败：' + res.data.message)
    }
  } catch (error) {
    ElMessage.error('获取车辆详情失败：' + error)
  } finally {
    loading.value = false
  }
}

// 提交订单
const handleSubmitOrder = async () => {
  // 表单验证
  await formRef.value.validate(async (valid: boolean) => {
    if (!valid) {
      ElMessage.warning('请完善必填信息')
      return
    }

    // 判断车辆状态
    if (vehicle.value?.status !== VehicleStatusEnum.AVAILABLE) {
      ElMessage.error('该车辆不可租')
      return
    }

    submitting.value = true
    try {
      // 构建提交数据
      const orderData: API.RentalOrderCreateRequest = {
        vehicleId: orderForm.value.vehicleId,
        startDate: orderForm.value.startDate,
        endDate: orderForm.value.endDate,
        needDriver: orderForm.value.needDriver ? 1 : 0, // 将布尔值转换为数字
        driverId: orderForm.value.needDriver ? Number(orderForm.value.driverId) : undefined,
        pickupLocation: orderForm.value.pickupLocation,
        returnLocation: orderForm.value.returnLocation,
        remark: orderForm.value.remark || undefined,
      }

      // 确认提交
      ElMessageBox.confirm(`确认支付 ${formattedPrice.value} 元创建此订单吗？`, '订单确认', {
        confirmButtonText: '确认支付',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(async () => {
          const res = await createRentalOrderUsingPost(orderData)
          if (res.data?.code === 0) {
            ElMessage.success('订单创建成功')
            // 跳转到订单详情页
            router.push({
              path: '/order/detail',
              query: { id: res.data?.data },
            })
          } else {
            ElMessage.error('订单创建失败：' + res.data?.message)
          }
        })
        .catch(() => {
          ElMessage.info('已取消订单')
        })
        .finally(() => {
          submitting.value = false
        })
    } catch (error) {
      ElMessage.error('提交订单失败：' + error)
      submitting.value = false
    }
  })
}

onMounted(() => {
  if (!vehicleId.value) {
    ElMessage.error('缺少车辆信息')
    router.push('/')
    return
  }

  // 检查登录状态
  if (!loginUserStore.loginUser?.id) {
    ElMessage.warning('请先登录后再进行下单操作')
    router.push({
      path: '/auth/login',
      query: {
        redirect: route.fullPath,
      },
    })
    return
  }

  fetchVehicleDetail()
})
</script>

<style scoped>
#orderSubmitPage {
  margin-bottom: 16px;
}

.rental-form {
  padding: 16px 0;
}

.rental-days {
  font-size: 16px;
  font-weight: bold;
  color: #409eff;
}

.vehicle-info-container {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
  border-bottom: 1px solid #eee;
  padding-bottom: 16px;
}

.vehicle-image {
  flex: 0 0 180px;
}

.vehicle-thumbnail {
  width: 180px;
  height: 120px;
  border-radius: 4px;
  object-fit: cover;
}

.vehicle-details {
  flex: 1;
}

.vehicle-name {
  font-size: 18px;
  margin-top: 0;
  margin-bottom: 12px;
  color: #303133;
}

.form-section-title {
  font-size: 16px;
  margin-bottom: 16px;
  color: #303133;
  font-weight: 500;
  border-left: 3px solid #409eff;
  padding-left: 10px;
}

.submit-section {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.submit-button {
  width: 100%;
}

.payment-terms {
  color: #909399;
  text-align: center;
}

.total-amount {
  font-size: 16px;
}
</style>
