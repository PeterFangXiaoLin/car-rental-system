<template>
  <div id="vehicleDetailPage">
    <el-row :gutter="16" class="mb-4">
      <!--      车辆图片-->
      <el-col :sm="14" :lg="16">
        <el-card header="车辆图片">
          <div v-loading="loading">
            <el-image :src="vehicle?.imageUrl" fit="contain" style="width: 100%" />
          </div>
        </el-card>
      </el-col>
      <!--      车辆信息-->
      <el-col :sm="10" :lg="8">
        <el-card header="车辆信息">
          <el-descriptions :column="1" v-loading="loading" border>
            <el-descriptions-item label="名称">
              {{ vehicle?.name ?? '未命名' }}
            </el-descriptions-item>
            <el-descriptions-item label="描述">
              {{ vehicle?.description ?? '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="车牌号">
              {{ vehicle?.vehicleNo ?? '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="品牌">
              {{ vehicle?.brandName ?? '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="系列">
              {{ vehicle?.modelName ?? '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="车型">
              {{ vehicle?.vehicleTypeName ?? '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="能源类型">
              {{ vehicle?.energyTypeName ?? '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="生产年份">
              {{ vehicle?.productionYear ?? '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="日租金">
              <div class="color-#f56c6c">{{ vehicle?.dailyPrice ?? '-' }} 元/天</div>
            </el-descriptions-item>
            <el-descriptions-item label="座位数">
              {{ vehicle?.seatCount ?? '-' }}
              <span v-if="vehicle?.seatCount">座</span>
            </el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag v-if="vehicle?.status === VehicleStatusEnum.AVAILABLE"> 可租 </el-tag>
              <el-tag v-else-if="vehicle?.status === VehicleStatusEnum.RENTED"> 已租出 </el-tag>
              <el-tag v-else-if="vehicle?.status === VehicleStatusEnum.MAINTENANCE">
                维修中
              </el-tag>
              <el-tag v-else-if="vehicle?.status === VehicleStatusEnum.SCRAPPED"> 报废 </el-tag>
            </el-descriptions-item>
          </el-descriptions>

          <!-- 下单 -->
          <el-space class="mt-4 flex justify-center items-center">
            <el-button type="primary" @click="handleOrder">
              <el-icon><ShoppingCart /></el-icon>
              立即下单
            </el-button>

            <el-button
              :type="isFavorite ? 'danger' : 'default'"
              @click="toggleFavorite"
              :loading="favoriteLoading"
            >
              <el-icon><Star /></el-icon>
              {{ isFavorite ? '取消收藏' : '收藏' }}
            </el-button>
          </el-space>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import { ElMessage } from 'element-plus'
import { ShoppingCart, Star } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { getVehicleByIdUsingGet } from '@/api/vehicleController.ts'
import VehicleStatusEnum from '@/enums/VehicleStatusEnum.ts'
import {
  addVehicleFavoriteUsingPost,
  cancelVehicleFavoriteUsingPost,
  checkVehicleFavoriteUsingGet
} from "@/api/vehicleFavoriteController.ts";
import {addOrUpdateBrowsHistoryUsingPost} from "@/api/vehicleBrowsingHistoryController.ts";

interface Props {
  id: string | number
}

const router = useRouter()
const loginUserStore = useLoginUserStore()

const props = defineProps<Props>()
const vehicle = ref<API.VehicleVO>()
const loading = ref(false)
const isFavorite = ref(false)
const favoriteLoading = ref(false)

// 获取车辆详情
const fetchPictureDetail = async () => {
  loading.value = true
  try {
    const res = await getVehicleByIdUsingGet({ id: props.id })
    if (res.data.code === 0 && res.data.data) {
      vehicle.value = res.data.data
    } else {
      ElMessage.error('获取车辆详情失败：' + res.data.message)
    }
  } catch (error) {
    ElMessage.error('获取车辆详情失败：' + error)
  } finally {
    loading.value = false
  }
}

// 记录浏览历史
const recordBrowsingHistory = async () => {
  // 如果用户已登录，记录浏览历史
  if (loginUserStore.loginUser?.id) {
    try {
      await addOrUpdateBrowsHistoryUsingPost({ vehicleId: props.id })
    } catch (error) {
      console.error('记录浏览历史失败：', error)
    }
  }
}

// 检查收藏状态
const checkFavoriteStatus = async () => {
  // 如果用户已登录，检查收藏状态
  if (loginUserStore.loginUser?.id) {
    try {
      const res = await checkVehicleFavoriteUsingGet({vehicleId: props.id})
      if (res.data.code === 0) {
        isFavorite.value = !!res.data.data
      }
    } catch (error) {
      console.error('检查收藏状态失败：', error)
    }
  }
}

// 切换收藏状态
const toggleFavorite = async () => {
  // 判断是否登录
  if (!loginUserStore.loginUser?.id || loginUserStore.loginUser?.userName === '未登录') {
    ElMessage.warning('请先登录后再进行收藏操作')
    // 跳转到登录页，并设置登录成功后的回调地址为当前页面
    router.push({
      path: '/auth/login',
      query: {
        redirect: router.currentRoute.value.fullPath,
      },
    })
    return
  }

  favoriteLoading.value = true
  try {
    if (isFavorite.value) {
      // 已收藏，取消收藏
      const res = await cancelVehicleFavoriteUsingPost({
        vehicleId: props.id,
      })
      if (res.data.code === 0) {
        isFavorite.value = false
        ElMessage.success('取消收藏成功')
      } else {
        ElMessage.error('取消收藏失败：' + res.data.message)
      }
    } else {
      // 未收藏，添加收藏
      const res = await addVehicleFavoriteUsingPost({ vehicleId: props.id })
      if (res.data.code === 0) {
        isFavorite.value = true
        ElMessage.success('收藏成功')
      } else {
        ElMessage.error('收藏失败：' + res.data.message)
      }
    }
  } catch (error) {
    ElMessage.error('操作失败：' + error)
  } finally {
    favoriteLoading.value = false
  }
}

// 处理下单操作
const handleOrder = () => {
  // 判断是否登录
  if (!loginUserStore.loginUser?.id || loginUserStore.loginUser?.userName === '未登录') {
    ElMessage.warning('请先登录后再进行下单操作')
    // 跳转到登录页，并设置登录成功后的回调地址为当前页面
    router.push({
      path: '/auth/login',
      query: {
        redirect: router.currentRoute.value.fullPath,
      },
    })
    return
  }

  if (vehicle.value?.status !== VehicleStatusEnum.AVAILABLE) {
    ElMessage.error('该车辆不可租')
    return
  }

  // 已登录，跳转到订单生成页面
  router.push({
    path: '/order/create',
    query: {
      vehicleId: props.id,
    },
  })
}

onMounted(() => {
  fetchPictureDetail()
  recordBrowsingHistory()
  checkFavoriteStatus()
})
</script>

<style scoped>
#vehicleDetailPage {
  margin-bottom: 16px;
}
</style>
