<template>
  <div>
    <el-card shadow="never" class="mb-15px">
      <div>
        <el-form :model="searchParams" class="-mb-15px" label-width="68px" size="large">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item label="店铺名称">
                <el-input v-model="searchParams.storeName" placeholder="输入店铺名称" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="店铺地址">
                <el-input v-model="searchParams.address" placeholder="请输入店铺地址" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="省份">
                <el-input v-model="searchParams.province" placeholder="请输入省份" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="城市">
                <el-input v-model="searchParams.city" placeholder="请输入城市" clearable />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item label="联系电话">
                <el-input
                  v-model="searchParams.contactPhone"
                  placeholder="请输入联系电话"
                  clearable
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="店铺状态">
                <el-select v-model="searchParams.status" placeholder="请选择店铺状态" clearable>
                  <el-option label="营业中" :value="1" />
                  <el-option label="已关闭" :value="0" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="门店描述">
                <el-input
                  v-model="searchParams.description"
                  placeholder="请输入门店描述"
                  clearable
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-button type="primary" :icon="Search" @click="doSearch">搜索</el-button>
              <el-button plain type="primary" :icon="Plus" @click="openForm()">新增</el-button>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </el-card>

    <el-card shadow="never" class="mb-15px">
      <!-- 店铺表格 -->
      <el-table
        :data="dataList"
        style="width: 100%"
        v-loading="loading"
        :header-cell-style="{ 'background-color': '#ecf8fe', color: '#4986EA' }"
      >
        <el-table-column label="店铺名称" prop="storeName" />
        <el-table-column label="店铺地址" prop="address" />
        <el-table-column label="省份" prop="province" />
        <el-table-column label="城市" prop="city" />
        <el-table-column label="联系电话" prop="contactPhone" />
        <el-table-column label="营业时间">
          <template #default="{ row }">
            <span>{{ row.openTime }} - {{ row.closeTime }}</span>
          </template>
        </el-table-column>
        <el-table-column label="店铺状态" prop="status" />
        <el-table-column label="门店描述" prop="description" />

        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="selectOpen(1, row.id)">
              <el-icon>
                <Edit />
              </el-icon>
              编辑
            </el-button>
            <el-button link type="primary" @click="selectOpen(2, row.id)">
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
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import { listStoreByPageUsingPost } from '@/api/storeController'
import { ElMessage } from 'element-plus'
import { Search, Plus } from '@element-plus/icons-vue'

const searchParams = reactive<API.StoreQueryRequest>({
  current: 1,
  pageSize: 10,
  storeName: '',
  address: '',
  province: '',
  city: '',
  contactPhone: '',
  description: '',
})

const dataList = ref<API.StoreVO[]>([])
const total = ref<number>(0)
const loading = ref<boolean>(false)

const handleSizeChange = (size: number) => {
  searchParams.pageSize = size
  getStoreList()
}

const handleCurrentChange = (current: number) => {
  searchParams.current = current
}

const getStoreList = async () => {
  loading.value = true
  try {
    const res = await listStoreByPageUsingPost(searchParams)
    if (res.data.code === 0 && res.data.data) {
      dataList.value = res.data.data.records || []
      total.value = Number(res.data.data.total)
    } else {
      ElMessage.error('获取店铺列表失败：' + (res.data?.message ? res.data.message : '未知错误'))
    }
  } catch (error) {
    console.error('获取店铺列表失败：', error)
  } finally {
    loading.value = false
  }
}

const doSearch = () => {
  searchParams.current = 1
  getStoreList()
}

onMounted(() => {
  getStoreList()
})
</script>

<style scoped></style>
