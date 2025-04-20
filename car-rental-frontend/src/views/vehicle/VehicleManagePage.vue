<template>
  <div>
    <!-- 搜索表单 -->
    <el-card shadow="never" class="mb-15px">
      <div>
        <el-form :model="searchParams" class="-mb-15px" label-width="88px" size="large">
          <el-row>
            <el-col :span="6">
              <el-form-item label="车辆名称">
                <el-input v-model="searchParams.name" placeholder="请输入车辆名称" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="品牌">
                <el-select
                  v-model="searchParams.brandId"
                  placeholder="请选择品牌"
                  clearable
                  size="large"
                >
                  <el-option
                    v-for="item in brandList"
                    :key="item.id"
                    :label="item.brandName"
                    :value="item.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="型号">
                <el-select
                  v-model="searchParams.modelId"
                  placeholder="请选择型号"
                  clearable
                  size="large"
                >
                  <el-option
                    v-for="item in modelList"
                    :key="item.id"
                    :label="item.modelName"
                    :value="item.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="能源类型">
                <el-select
                  v-model="searchParams.energyTypeId"
                  placeholder="请选择能源类型"
                  clearable
                  size="large"
                >
                  <el-option
                    v-for="item in energyTypeList"
                    :key="item.id"
                    :label="item.typeName"
                    :value="item.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="最小日租金">
                <el-input-number
                  v-model="searchParams.minDailyPrice"
                  :min="0"
                  :precision="2"
                  :step="10"
                  placeholder="最小日薪"
                  size="large"
                  class="w-full"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="最大日薪">
                <el-input-number
                  v-model="searchParams.maxDailyPrice"
                  :min="0"
                  :precision="2"
                  :step="10"
                  placeholder="最大日薪"
                  size="large"
                  class="w-full"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="座位数">
                <el-input-number
                  v-model="searchParams.seatCount"
                  :min="4"
                  :max="20"
                  placeholder="请输入座位数"
                  class="w-full"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="状态">
                <el-select v-model="searchParams.status" placeholder="请选择状态" clearable>
                  <el-option label="可用" :value="0" />
                  <el-option label="已租出" :value="1" />
                  <el-option label="维修中" :value="2" />
                  <el-option label="报废" :value="3" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6" :offset="18">
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
        <el-table-column label="车辆图片" width="120" align="center">
          <template #default="{ row }">
            <el-image
              :src="row.imageUrl"
              :preview-src-list="[row.imageUrl]"
              fit="cover"
              style="width: 80px; height: 80px"
              :preview-teleported="true"
              :initial-index="0"
            />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="车辆名称" width="120" align="center" />
        <el-table-column prop="vehicleNo" label="车牌号" width="120" align="center" />
        <el-table-column prop="brandName" label="品牌" width="120" align="center" />
        <el-table-column prop="modelName" label="型号" width="120" align="center" />
        <el-table-column prop="vehicleTypeName" label="车型名称" width="120" align="center" />
        <el-table-column prop="energyTypeName" label="能源类型" width="120" align="center" />
        <el-table-column label="生产年份" width="120" align="center">
          <template #default="{ row }">
            <div>{{ row.productionYear }} 年</div>
          </template>
        </el-table-column>
        <el-table-column label="日租金" align="center">
          <template #default="{ row }">
            <div class="color-#f56c6c">{{ row.dailyPrice }} 元/天</div>
          </template>
        </el-table-column>
        <el-table-column label="座位数" align="center">
          <template #default="{ row }">
            <div>{{ row.seatCount }} 座</div>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === VehicleStatusEnum.AVAILABLE" type="primary">可用</el-tag>
            <el-tag v-else-if="row.status === VehicleStatusEnum.RENTED" type="primary"
              >已租出
            </el-tag>
            <el-tag v-else-if="row.status === VehicleStatusEnum.AVAILABLE" type="primary"
              >可用
            </el-tag>
            <el-tag v-else-if="row.status === VehicleStatusEnum.AVAILABLE" type="primary"
              >可用
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="描述" width="200" align="center">
          <template #default="{ row }">
            <div>{{ row.description }}</div>
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

  <VehicleAddForm ref="addFormRef" @success="success" />
  <VehicleUpdateForm ref="updateFormRef" @success="success" />
  <VehicleViewForm ref="viewFormRef" />
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Edit, Plus, Search, View } from '@element-plus/icons-vue'
import { deleteDriverUsingPost } from '@/api/driverController.ts'
import { listVehicleBrandUsingPost } from '@/api/vehicleBrandController.ts'
import { listVehicleModelUsingPost } from '@/api/vehicleModelController.ts'
import { listEnergyTypeDictUsingPost } from '@/api/energyTypeDictController.ts'
import VehicleStatusEnum from '@/enums/VehicleStatusEnum.ts'
import VehicleAddForm from '@/components/vehicle/VehicleAddForm.vue'
import VehicleUpdateForm from '@/components/vehicle/VehicleUpdateForm.vue'
import VehicleViewForm from '@/components/vehicle/VehicleViewForm.vue'
import { listVehicleByPageUsingPost } from '@/api/vehicleController.ts'

const loading = ref(false)
const dataList = ref<API.VehicleVO[]>([])
const total = ref(0)

const brandList = ref<API.VehicleBrandVO[]>([])
const modelList = ref<API.VehicleModelVO[]>([])
const energyTypeList = ref<API.EnergyTypeDictVO[]>([])

const addFormRef = ref()
const updateFormRef = ref()
const viewFormRef = ref()

// 搜索参数
const searchParams = reactive<API.VehicleQueryRequest>({
  current: 1,
  pageSize: 10,
  sortField: 'createTime',
  sortOrder: 'descend',
})

// 获取用户列表数据
const fetchData = async () => {
  loading.value = true
  try {
    const res = await listVehicleByPageUsingPost(searchParams)
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

// 获取品牌列表
const fetchBrandList = async () => {
  try {
    const res = await listVehicleBrandUsingPost()
    if (res.data?.code === 0 && res.data.data) {
      brandList.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('获取品牌列表失败：' + error)
  }
}

// 获取车型列表
const fetchModelList = async (brandId: string) => {
  try {
    const res = await listVehicleModelUsingPost({ brandId })
    if (res.data?.code === 0 && res.data.data) {
      modelList.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('获取车型列表失败：' + error)
  }
}

// 获取能源类型列表
const fetchEnergyTypeList = async () => {
  try {
    const res = await listEnergyTypeDictUsingPost()
    if (res.data?.code === 0 && res.data.data) {
      energyTypeList.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('获取能源类型列表失败：' + error)
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
    await ElMessageBox.confirm('确定要删除该车辆吗？', '提示', {
      type: 'warning',
    })
    const res = await deleteDriverUsingPost({ id: id })
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

watch(
  () => searchParams.brandId,
  (newVal) => {
    if (newVal) {
      fetchModelList(String(newVal))
    }
  },
)

// 页面加载时获取数据
onMounted(() => {
  fetchData()
  fetchBrandList()
  fetchEnergyTypeList()
})
</script>

<style scoped>
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
