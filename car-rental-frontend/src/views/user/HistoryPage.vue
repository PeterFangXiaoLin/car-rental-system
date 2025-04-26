<template>
  <div id="historyPage" class="container mx-auto">
    <el-card shadow="hover" class="mb-4">
      <template #header>
        <div class="flex justify-between items-center">
          <span class="text-lg font-bold">浏览历史</span>
          <div>
            <el-button
              type="danger"
              size="small"
              @click="handleClearHistory"
              :loading="clearLoading"
            >
              清空历史
            </el-button>
          </div>
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
        >
          <template #append>
            <el-button :icon="Search" @click="handleSearch" :loading="loading">搜索</el-button>
          </template>
        </el-input>
      </div>

      <el-empty v-if="dataList.length === 0" description="暂无浏览历史"></el-empty>

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
                  :src="item.imageUrl || 'https://via.placeholder.com/300x200'"
                  class="w-full h-48 object-cover rounded-md mb-2"
                  fit="cover"
                  :preview-src-list="[item.imageUrl]"
                />
                <div class="absolute top-2 right-2">
                  <el-tooltip content="删除此记录" placement="top">
                    <el-button
                      type="danger"
                      circle
                      plain
                      size="small"
                      @click.stop="handleDeleteHistory(item.id)"
                      :loading="deleteLoading === item.id"
                    >
                      <el-icon><Delete /></el-icon>
                    </el-button>
                  </el-tooltip>
                </div>
              </div>

              <!-- 车辆信息 -->
              <div @click="goToDetail(item.vehicleId)" class="cursor-pointer flex-grow">
                <h3 class="text-lg font-bold mb-2 truncate">{{ item.vehicleName }}</h3>
                <div class="text-sm mb-1">
                  <span class="text-gray-600">品牌：</span>
                  <span>{{ item.brandName }}</span>
                </div>
                <div class="text-sm mb-1">
                  <span class="text-gray-600">型号：</span>
                  <span>{{ item.modelName }}</span>
                </div>
                <div class="text-sm mb-1">
                  <span class="text-gray-600">能源类型：</span>
                  <span>{{ item.energyTypeName }}</span>
                </div>
                <div class="text-sm mb-1">
                  <span class="text-gray-600">座位数：</span>
                  <span>{{ item.seatCount }}座</span>
                </div>
                <div class="text-sm mb-1">
                  <span class="text-gray-600">状态：</span>
                  <el-tag v-if="item.status === 0" size="small">可租</el-tag>
                  <el-tag v-else-if="item.status === 1" size="small" type="warning">已租出</el-tag>
                  <el-tag v-else-if="item.status === 2" size="small" type="info">维修中</el-tag>
                  <el-tag v-else-if="item.status === 3" size="small" type="danger">报废</el-tag>
                </div>
              </div>

              <!-- 价格和浏览时间 -->
              <div class="mt-3 pt-3 border-t border-gray-200 flex justify-between items-center">
                <div class="text-red-500 font-bold">¥{{ item.dailyPrice }}/天</div>
                <div class="text-gray-500 text-xs">
                  {{ formatDate(item.browseTime) }}
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
  pageBrowseHistoryUsingPost,
  deleteBrowsHistoryUsingPost,
  clearBrowsHistoryUsingPost,
} from '@/api/vehicleBrowsingHistoryController.ts'

const router = useRouter()

// 搜索参数
const searchParams = reactive<API.BrowsHistoryQueryRequest>({
  current: 1,
  pageSize: 8,
  sortField: 'browseTime',
  sortOrder: 'descend',
  searchText: '',
})

// 数据列表和加载状态
const dataList = ref<API.VehicleBrowsingHistoryVO[]>([])
const total = ref<number>(0)
const loading = ref<boolean>(false)
const deleteLoading = ref<number | null>(null)
const clearLoading = ref<boolean>(false)

// 获取浏览历史列表
const fetchHistoryList = async () => {
  loading.value = true
  try {
    const res = await pageBrowseHistoryUsingPost(searchParams)
    if (res.data.code === 0 && res.data.data) {
      dataList.value = res.data.data.records || []
      total.value = Number(res.data.data.total) || 0
    } else {
      ElMessage.error('获取浏览历史失败：' + res.data.message)
    }
  } catch (error) {
    console.error('获取浏览历史失败：', error)
  } finally {
    loading.value = false
  }
}

// 处理搜索
const handleSearch = () => {
  searchParams.current = 1
  fetchHistoryList()
}

// 清空全部历史
const handleClearHistory = () => {
  ElMessageBox.confirm('确定要清空所有浏览历史吗？此操作不可恢复！', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(async () => {
      clearLoading.value = true
      try {
        const res = await clearBrowsHistoryUsingPost()
        if (res.data.code === 0 && res.data.data) {
          ElMessage.success('清空历史成功')
          fetchHistoryList()
        } else {
          ElMessage.error('清空历史失败：' + res.data.message)
        }
      } catch (error) {
        console.error('清空历史失败：', error)
      } finally {
        clearLoading.value = false
      }
    })
    .catch(() => {
      // 用户取消操作，不做任何事
    })
}

// 删除单条历史
const handleDeleteHistory = (id: number | undefined) => {
  if (!id) return
  deleteLoading.value = id
  ElMessageBox.confirm('确定要删除该条浏览记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(async () => {
      try {
        const res = await deleteBrowsHistoryUsingPost({ id })
        if (res.data.code === 0 && res.data.data) {
          ElMessage.success('删除成功')
          fetchHistoryList()
        } else {
          ElMessage.error('删除失败：' + res.data.message)
        }
      } catch (error) {
        console.error('删除失败：', error)
      } finally {
        deleteLoading.value = null
      }
    })
    .catch(() => {
      deleteLoading.value = null
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
  fetchHistoryList()
}

const handleCurrentChange = (current: number) => {
  searchParams.current = current
  fetchHistoryList()
}

// 组件挂载时加载数据
onMounted(() => {
  fetchHistoryList()
})
</script>

<style scoped>
#historyPage {
  min-height: calc(100vh - 260px);
}

.search-container {
  display: flex;
  justify-content: flex-start;
}
</style>
