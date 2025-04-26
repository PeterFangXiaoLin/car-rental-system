<template>
  <div>
    <!-- 搜索表单 -->
    <el-card shadow="never" class="mb-15px">
      <div>
        <el-form :model="searchParams" class="-mb-15px" label-width="110px" size="large">
          <el-row>
            <el-col :span="6">
              <el-form-item label="门店名称">
                <el-input v-model="searchParams.storeName" placeholder="请输入门店名称" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="门店地址">
                <el-input v-model="searchParams.address" placeholder="请输入门店地址" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="城市名称">
                <el-input v-model="searchParams.cityName" placeholder="请输入城市名称" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="联系电话">
                <el-input
                  v-model="searchParams.contactPhone"
                  placeholder="请输入联系电话"
                  clearable
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="开始营业时间">
                <el-time-picker
                  v-model="searchParams.openTime"
                  placeholder="选择开始营业时间"
                  value-format="HH:mm:ss"
                  format="HH:mm:ss"
                  clearable
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="结束营业时间">
                <el-time-picker
                  v-model="searchParams.closeTime"
                  placeholder="选择结束营业时间"
                  value-format="HH:mm:ss"
                  format="HH:mm:ss"
                  clearable
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="状态">
                <el-select v-model="searchParams.status" placeholder="请选择状态" clearable>
                  <el-option label="营业中" :value="1" />
                  <el-option label="关闭" :value="0" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item>
                <div class="flex">
                  <el-button type="primary" :icon="Search" @click="doSearch">搜索</el-button>
                  <el-button plain type="primary" :icon="Plus" @click="openForm()">新增</el-button>
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
        <el-table-column prop="storeName" label="门店名称" align="center" />
        <el-table-column prop="address" label="门店地址" align="center" />
        <el-table-column prop="cityName" label="所在城市" align="center" />
        <el-table-column label="门店图片" align="center">
          <template #default="{ row }">
            <div v-if="row.images" style="position: relative; display: inline-block">
              <el-image
                :src="row.images.split(',')[0]"
                :preview-src-list="row.images.split(',')"
                fit="cover"
                style="width: 80px; height: 80px"
                :preview-teleported="true"
                :initial-index="0"
              />
              <div v-if="row.images.includes(',')" class="image-count">
                +{{ row.images.split(',').length - 1 }}
              </div>
            </div>
            <span v-else>无图片</span>
          </template>
        </el-table-column>
        <el-table-column prop="mobile" label="联系电话" align="center" />
        <el-table-column prop="openTime" label="开始营业时间" align="center" />
        <el-table-column prop="closeTime" label="结束营业时间" align="center" />
        <el-table-column prop="status" label="状态" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status ? 'success' : 'danger'">
              {{ row.status ? '营业中' : '关闭' }}
            </el-tag>
          </template>
        </el-table-column>
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

  <StoreAddForm ref="addFormRef" @success="success" />
  <StoreUpdateForm ref="updateFormRef" @success="success" />
  <StoreViewForm ref="viewFormRef" />
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Edit, Plus, Search, View } from '@element-plus/icons-vue'
import { deleteStoreUsingPost, listStoreByPageUsingPost } from '@/api/storeController.ts'
import StoreAddForm from '@/components/store/StoreAddForm.vue'
import StoreUpdateForm from '@/components/store/StoreUpdateForm.vue'
import StoreViewForm from '@/components/store/StoreViewForm.vue'

const loading = ref(false)
const dataList = ref<API.StoreVO[]>([])
const total = ref(0)

const addFormRef = ref()
const updateFormRef = ref()
const viewFormRef = ref()

// 搜索参数
const searchParams = reactive<API.StoreQueryRequest>({
  current: 1,
  pageSize: 10,
  sortField: 'createTime',
  sortOrder: 'descend',
})

// 获取商店列表数据
const fetchData = async () => {
  loading.value = true
  try {
    const res = await listStoreByPageUsingPost(searchParams)
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
    await ElMessageBox.confirm('确定要删除该商店吗？', '提示', {
      type: 'warning',
    })
    const res = await deleteStoreUsingPost({ id: id })
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

.image-count {
  position: absolute;
  bottom: 0;
  right: 0;
  background-color: rgba(0, 0, 0, 0.6);
  color: white;
  font-size: 12px;
  padding: 2px 4px;
  border-radius: 4px;
}
</style>
