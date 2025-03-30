<template>
  <div>
    <el-card shadow="never" class="mb-15px">
      <div>
        <el-form :model="searchParams" label-width="68px" size="large" class="-mb-15px">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item label="车型">
                <el-input v-model="searchParams.typeName" placeholder="请输入车型" clearable />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item>
                <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
                <el-button plain :icon="Plus" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </el-card>

    <el-card shadow="never" class="mb-15px">
      <el-table
        :data="dataList"
        style="width: 100%"
        v-loading="loading"
        :header-cell-style="{ 'background-color': '#ecf8fe', color: '#4986EA' }"
      >
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="类型" prop="typeName" align="center" />
        <el-table-column label="创建时间" prop="createTime" align="center">
          <template #default="{ row }">
            {{ dayjs(row.createTime).format('YYYY-MM-DD HH:mm:ss') }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row.id)">
              <el-icon>
                <Edit />
              </el-icon>
              编辑
            </el-button>
            <el-button link type="danger" @click="handleDelete(row.id)">
              <el-icon>
                <Delete />
              </el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>
<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { Edit, Plus, Search, Delete } from '@element-plus/icons-vue'
import {
  deleteVehicleTypeDictUsingPost,
  pageVehicleTypeDictUsingPost,
} from '@/api/vehicleTypeDictController.ts'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'

const searchParams = reactive<API.VehicleTypeDictQueryRequest>({
  typeName: '',
  current: 1,
  pageSize: 10,
})

const loading = ref(false)
const dataList = ref<API.VehicleTypeDictVO[]>([])
const total = ref(0)

const handleSearch = () => {
  searchParams.current = 1
  fetchData()
}

const handleEdit = (id: string) => {}

const handleDelete = async (id: string) => {
  try {
    await ElMessageBox.confirm('确定要删除该类型吗？', '提示', {
      type: 'warning',
    })
    const res = await deleteVehicleTypeDictUsingPost({ id: id })
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

/**
 * 获取数据
 */
const fetchData = async () => {
  loading.value = true
  try {
    const res = await pageVehicleTypeDictUsingPost(searchParams)
    if (res.data?.code === 0 && res.data.data) {
      dataList.value = res.data.data.records || []
      total.value = Number(res.data.data.total) || 0
    } else {
      ElMessage.error(res.data?.message || '获取数据失败')
    }
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped></style>
