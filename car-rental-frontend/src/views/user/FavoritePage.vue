<template>
  <div id="favoritePage" class="container mx-auto p-4">
    <el-card shadow="hover" class="mb-4">
      <template #header>
        <div class="flex justify-between items-center">
          <span class="text-lg font-bold">我的收藏</span>
        </div>
      </template>

      <!-- 搜索框 -->
      <div class="search-container mb-4">
        <el-input
          v-model="searchParams.searchText"
          placeholder="搜索车辆名称、品牌、型号等"
          class="w-full md:w-1/2 lg:w-1/3"
          clearable
          @clear="handleSearch"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button :icon="Search" @click="handleSearch" :loading="loading">搜索</el-button>
          </template>
        </el-input>
      </div>

      <el-empty v-if="dataList.length === 0" description="暂无收藏记录"></el-empty>

      <el-row :gutter="16" v-else>
        <el-col
          v-for="item in dataList"
          :key="item.id"
          :xs="24"
          :sm="12"
          :md="8"
          :lg="6"
          class="mb-4"
        >
          <el-card shadow="hover" class="h-full">
            <div class="flex flex-col h-full">
              <!-- 车辆图片 -->
              <div class="relative">
                <el-image
                  :src="item.vehicle?.imageUrl || 'https://via.placeholder.com/300x200'"
                  class="w-full h-48 object-cover rounded-md mb-2"
                  fit="cover"
                  :preview-src-list="item.vehicle?.imageUrl ? [item.vehicle.imageUrl] : []"
                />
                <div class="absolute top-2 right-2">
                  <el-tooltip content="取消收藏" placement="top">
                    <el-button
                      type="danger"
                      circle
                      plain
                      size="small"
                      @click.stop="handleCancelFavorite(item.vehicleId)"
                      :loading="cancelLoading === item.vehicleId"
                    >
                      <el-icon><Delete /></el-icon>
                    </el-button>
                  </el-tooltip>
                </div>
              </div>

              <!-- 车辆信息 -->
              <div @click="goToDetail(item.vehicleId)" class="cursor-pointer flex-grow">
                <h3 class="text-lg font-bold mb-2 truncate">
                  {{ item.vehicle?.name || '未知车辆' }}
                </h3>
                <div class="text-sm mb-1" v-if="item.vehicle">
                  <span class="text-gray-600">品牌：</span>
                  <span>{{ item.vehicle.brandName }}</span>
                </div>
                <div class="text-sm mb-1" v-if="item.vehicle">
                  <span class="text-gray-600">型号：</span>
                  <span>{{ item.vehicle.modelName }}</span>
                </div>
                <div class="text-sm mb-1" v-if="item.vehicle">
                  <span class="text-gray-600">能源类型：</span>
                  <span>{{ item.vehicle.energyTypeName }}</span>
                </div>
                <div class="text-sm mb-1" v-if="item.vehicle">
                  <span class="text-gray-600">座位数：</span>
                  <span>{{ item.vehicle.seatCount }}座</span>
                </div>
                <div class="text-sm mb-1" v-if="item.vehicle">
                  <span class="text-gray-600">状态：</span>
                  <el-tag v-if="item.vehicle.status === 0" size="small">可租</el-tag>
                  <el-tag v-else-if="item.vehicle.status === 1" size="small" type="warning"
                    >已租出</el-tag
                  >
                  <el-tag v-else-if="item.vehicle.status === 2" size="small" type="info"
                    >维修中</el-tag
                  >
                  <el-tag v-else-if="item.vehicle.status === 3" size="small" type="danger"
                    >报废</el-tag
                  >
                </div>
              </div>

              <!-- 价格和收藏时间 -->
              <div class="mt-3 pt-3 border-t border-gray-200 flex justify-between items-center">
                <div class="text-red-500 font-bold" v-if="item.vehicle">
                  ¥{{ item.vehicle.dailyPrice }}/天
                </div>
                <div class="text-gray-500 text-xs">
                  {{ formatDate(item.createTime) }}
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 分页 -->
      <div class="mt-4 flex justify-center">
        <el-pagination
          v-model:current-page="searchParams.current"
          v-model:page-size="searchParams.pageSize"
          :page-sizes="[8, 16, 24, 32]"
          layout="total, sizes, prev, pager, next, jumper"
          background
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Search } from '@element-plus/icons-vue'
import {
  pageVehicleFavoriteUsingPost,
  cancelVehicleFavoriteUsingPost,
} from '@/api/vehicleFavoriteController.ts'
import { getVehicleByIdUsingGet } from '@/api/vehicleController.ts'

const router = useRouter()

// 搜索参数
const searchParams = reactive<API.VehicleFavoriteQueryRequest>({
  current: 1,
  pageSize: 8,
  sortField: 'createTime',
  sortOrder: 'descend',
  searchText: '',
})

// 数据列表和加载状态
interface EnhancedFavoriteItem extends API.VehicleFavoriteVO {
  vehicle?: API.VehicleVO
}

const dataList = ref<EnhancedFavoriteItem[]>([])
const total = ref<number>(0)
const loading = ref<boolean>(false)
const cancelLoading = ref<number | null>(null)

// 获取收藏列表
const fetchFavoriteList = async () => {
  loading.value = true
  try {
    const res = await pageVehicleFavoriteUsingPost(searchParams)
    if (res.data.code === 0 && res.data.data) {
      const favorites = res.data.data.records || []
      total.value = Number(res.data.data.total) || 0

      // 初始化增强的收藏项列表
      const enhancedFavorites = [...favorites]

      // 为每个收藏项获取车辆详情
      await Promise.all(
        enhancedFavorites.map(async (favorite) => {
          try {
            if (favorite.vehicleId) {
              const vehicleRes = await getVehicleByIdUsingGet({ id: favorite.vehicleId })
              if (vehicleRes.data.code === 0) {
                favorite.vehicle = vehicleRes.data.data
              }
            }
          } catch (error) {
            console.error('获取车辆详情失败：', error)
          }
        }),
      )

      dataList.value = enhancedFavorites
    } else {
      ElMessage.error('获取收藏列表失败：' + res.data.message)
    }
  } catch (error) {
    console.error('获取收藏列表失败：', error)
  } finally {
    loading.value = false
  }
}

// 处理搜索
const handleSearch = () => {
  searchParams.current = 1
  fetchFavoriteList()
}

// 取消收藏
const handleCancelFavorite = (vehicleId: number | undefined) => {
  if (!vehicleId) return
  cancelLoading.value = vehicleId

  ElMessageBox.confirm('确定要取消收藏这辆车吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(async () => {
      try {
        const res = await cancelVehicleFavoriteUsingPost({ vehicleId })
        if (res.data.code === 0 && res.data.data) {
          ElMessage.success('取消收藏成功')
          fetchFavoriteList()
        } else {
          ElMessage.error('取消收藏失败：' + res.data.message)
        }
      } catch (error) {
        console.error('取消收藏失败：', error)
      } finally {
        cancelLoading.value = null
      }
    })
    .catch(() => {
      cancelLoading.value = null
    })
}

// 格式化日期
const formatDate = (dateString: string | undefined) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
  })
}

// 跳转到车辆详情页
const goToDetail = (vehicleId: number | undefined) => {
  if (!vehicleId) return
  router.push(`/vehicle/${vehicleId}`)
}

// 分页处理
const handleSizeChange = (size: number) => {
  searchParams.pageSize = size
  fetchFavoriteList()
}

const handleCurrentChange = (current: number) => {
  searchParams.current = current
  fetchFavoriteList()
}

// 组件挂载时加载数据
onMounted(() => {
  fetchFavoriteList()
})
</script>

<style scoped>
#favoritePage {
  min-height: calc(100vh - 260px);
}

.search-container {
  display: flex;
  justify-content: flex-start;
}
</style>
