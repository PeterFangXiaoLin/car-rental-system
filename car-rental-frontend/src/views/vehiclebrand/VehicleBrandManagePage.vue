<template>
  <div>
    <!-- 搜索表单 -->
    <el-card shadow="never" class="mb-15px">
      <div>
        <el-form :model="searchParams" class="-mb-15px" label-width="88px" size="large">
          <el-row>
            <el-col :span="6">
              <el-form-item label="品牌名称">
                <el-input
                  v-model="searchParams.searchText"
                  placeholder="请输入品牌名称"
                  clearable
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item>
                <div class="flex">
                  <el-button type="primary" :icon="Search" @click="doSearch">搜索</el-button>
                  <el-button plain type="primary" :icon="Plus" @click="openForm()">新增 </el-button>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </el-card>

    <el-card shadow="never" class="mb-15px">
      <!-- 车辆表格 -->
      <el-table
        :data="dataList"
        style="width: 100%"
        v-loading="loading"
        :header-cell-style="{ 'background-color': '#ecf8fe', color: '#4986EA' }"
      >
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column prop="brandName" label="品牌名称" align="center" />
        <el-table-column label="品牌Logo" align="center">
          <template #default="{ row }">
            <el-image
              :src="row.brandLogo"
              fit="cover"
              style="width: 80px; height: 80px"
              :preview-teleported="true"
              :initial-index="0"
            />
          </template>
        </el-table-column>
        <el-table-column prop="firstLetter" label="首字母" align="center" />
        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row.id)">
              <el-icon>
                <Edit />
              </el-icon>
              编辑
            </el-button>
            <el-button link type="primary" @click="handleView(row.id)">
              <el-icon>
                <View />
              </el-icon>
              查看
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
          v-model:current-page="searchParams.current"
          v-model:page-size="searchParams.pageSize"
          background
          :total="total"
          :page-sizes="[10, 20, 30]"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>

  <VehicleBrandAddForm ref="addFormRef" @success="success" />
  <VehicleBrandUpdateForm ref="updateFormRef" @success="success" />
  <VehicleBrandViewForm ref="viewFormRef" />
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Edit, Plus, Search, View } from '@element-plus/icons-vue'
import {
  deleteVehicleBrandUsingPost,
  listVehicleBrandByPageUsingPost,
} from '@/api/vehicleBrandController.ts'
import VehicleBrandAddForm from '@/components/vehiclebrand/VehicleBrandAddForm.vue'
import VehicleBrandUpdateForm from '@/components/vehiclebrand/VehicleBrandUpdateForm.vue'
import VehicleBrandViewForm from '@/components/vehiclebrand/VehicleBrandViewForm.vue'

const loading = ref(false)
const dataList = ref<API.VehicleVO[]>([])
const total = ref(0)

const addFormRef = ref()
const updateFormRef = ref()
const viewFormRef = ref()

// 搜索参数
const searchParams = reactive<API.VehicleBrandQueryRequest>({
  current: 1,
  pageSize: 10,
  sortField: 'createTime',
  sortOrder: 'descend',
})

// 获取用户列表数据
const fetchData = async () => {
  loading.value = true
  try {
    const res = await listVehicleBrandByPageUsingPost(searchParams)
    if (res.data?.code === 0 && res.data.data) {
      dataList.value = res.data.data.records ?? []
      total.value = Number(res.data.data.total) || 0
    } else {
      ElMessage.error(res.data.message || '获取数据失败')
    }
  } catch (error) {
    ElMessage.error('获取数据失败：' + error)
  } finally {
    loading.value = false
  }
}

// 新增
const openForm = () => {
  addFormRef.value?.open()
}

// 搜索
const doSearch = () => {
  searchParams.current = 1
  fetchData()
}

// 处理页码变化
const handleCurrentChange = (current: number) => {
  searchParams.current = current
  fetchData()
}

// 处理每页条数变化
const handleSizeChange = (size: number) => {
  searchParams.pageSize = size
  searchParams.current = 1
  fetchData()
}

// 删除用户
const handleDelete = async (id: string) => {
  try {
    await ElMessageBox.confirm('确定要删除该品牌吗？', '提示', {
      type: 'warning',
    })
    const res = await deleteVehicleBrandUsingPost({ id: id })
    if (res.data.code === 0) {
      ElMessage.success('删除成功')
      await fetchData()
    } else {
      ElMessage.error(res.data.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败：' + error)
    }
  }
}

// 编辑
const handleEdit = (id: string) => {
  updateFormRef.value?.open(id)
}

// 查看
const handleView = (id: string) => {
  viewFormRef.value?.open(id)
}

// 成功
const success = (msg: string) => {
  fetchData()
}

// 页面加载时获取数据
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
