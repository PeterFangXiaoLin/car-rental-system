<template>
  <div>
    <!-- 搜索表单 -->
    <el-card shadow="never" class="mb-15px">
      <div>
        <el-form :model="searchForm" class="-mb-15px" label-width="78px" size="large">
          <el-row>
            <el-col :span="5">
              <el-form-item label="用户名">
                <el-input v-model="searchForm.searchText" placeholder="请输入用户名" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="车辆ID">
                <el-input
                  v-model="searchForm.vehicleId"
                  placeholder="请输入车辆ID"
                  clearable
                  type="number"
                />
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="订单ID">
                <el-input
                  v-model="searchForm.orderId"
                  placeholder="请输入订单ID"
                  clearable
                  type="number"
                />
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="司机ID">
                <el-input
                  v-model="searchForm.driverId"
                  placeholder="请输入司机ID"
                  clearable
                  type="number"
                />
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
        :data="commentList"
        style="width: 100%"
        :header-cell-style="{ 'background-color': '#ecf8fe', color: '#4986EA' }"
      >
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column prop="userName" label="用户名" min-width="100" align="center" />
        <el-table-column prop="vehicleName" label="车辆名称" min-width="120" align="center" />
        <el-table-column label="评分" min-width="120" align="center">
          <template #default="{ row }">
            <div>
              <div>车辆评分：<el-rate v-model="row.vehicleRating" disabled /></div>
              <div v-if="row.driverRating">
                司机评分：<el-rate v-model="row.driverRating" disabled />
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="content"
          label="评论内容"
          min-width="200"
          align="center"
          show-overflow-tooltip
        />
        <el-table-column label="评论图片" min-width="120" align="center">
          <template #default="{ row }">
            <el-image
              v-if="row.images"
              :src="row.images.split(',')[0]"
              style="width: 80px; height: 80px"
              :preview-src-list="row.images.split(',')"
              fit="cover"
            />
            <span v-else>无图片</span>
          </template>
        </el-table-column>
        <el-table-column label="评论时间" min-width="150" align="center">
          <template #default="{ row }">
            {{ formatDateTime(row.commentTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row.vehicleId)">
              <el-icon>
                <View />
              </el-icon>
              查看车辆
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
          v-model:current-page="searchForm.current"
          v-model:page-size="searchForm.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          background
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, View, Delete } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { listCommentByPageUsingPost, deleteCommentUsingPost } from '@/api/commentController'

const router = useRouter()
const tableLoading = ref(false)
const commentList = ref<API.CommentVO[]>([])
const total = ref(0)

// 搜索表单
const searchForm = reactive<API.CommentQueryRequest>({
  searchText: '',
  vehicleId: undefined,
  orderId: undefined,
  driverId: undefined,
  current: 1,
  pageSize: 10,
  sortField: 'commentTime',
  sortOrder: 'descend',
})

// 加载评论列表
const fetchCommentList = async () => {
  tableLoading.value = true
  try {
    const res = await listCommentByPageUsingPost(searchForm)
    if (res.data?.code === 0 && res.data.data) {
      commentList.value = res.data.data.records || []
      total.value = Number(res.data.data.total) || 0
    } else {
      ElMessage.error(res.data?.message || '获取评论列表失败')
    }
  } catch (error: unknown) {
    console.error('获取评论列表失败', error)
    ElMessage.error('获取评论列表失败')
  } finally {
    tableLoading.value = false
  }
}

// 初始化
onMounted(() => {
  fetchCommentList()
})

// 格式化日期时间
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
  searchForm.current = 1
  fetchCommentList()
}

// 处理每页显示数量变化
const handleSizeChange = (val: number) => {
  searchForm.pageSize = val
  fetchCommentList()
}

// 处理页码变化
const handleCurrentChange = (val: number) => {
  searchForm.current = val
  fetchCommentList()
}

// 查看车辆详情
const handleView = (vehicleId: number | undefined) => {
  if (!vehicleId) {
    ElMessage.warning('车辆ID不存在')
    return
  }
  router.push(`/vehicle/${vehicleId}`)
}

// 删除评论
const handleDelete = async (id: number | undefined) => {
  if (!id) {
    ElMessage.error('评论ID不存在')
    return
  }

  try {
    await ElMessageBox.confirm('确定要删除这条评论吗？删除后不可恢复', '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })

    const res = await deleteCommentUsingPost({ id })
    if (res.data?.code === 0 && res.data.data) {
      ElMessage.success('评论删除成功')
      fetchCommentList()
    } else {
      ElMessage.error(res.data?.message || '删除评论失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除评论失败', error)
      ElMessage.error('删除评论失败，请稍后再试')
    }
  }
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
