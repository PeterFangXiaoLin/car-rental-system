<template>
  <div id="homePage">
    <!-- 搜索表单 -->
    <el-card shadow="never" class="mb-15px">
      <div class="search-bar">
        <el-input
          v-model="searchParams.searchText"
          placeholder="从海量汽车中搜索"
          clearable
          size="large"
        >
          <template #append>
            <el-button
              type="primary"
              :icon="Search"
              @click="doSearch"
              style="display: flex; align-items: center; justify-content: center; gap: 4px"
            >
              搜索
            </el-button>
          </template>
        </el-input>
      </div>
    </el-card>

    <!-- 品牌选择 -->
    <el-card shadow="never" class="mb-15px">
      <div class="brand-filter">
        <div class="brand-title">品牌</div>
        <div class="brand-list">
          <el-radio-group v-model="selectedBrand" @change="handleBrandChange">
            <el-radio label="all">不限</el-radio>
            <template v-for="letter in brandLetters" :key="letter">
              <el-radio :label="letter">{{ letter }}</el-radio>
            </template>
          </el-radio-group>
        </div>

        <div class="brand-content" v-if="selectedBrand !== 'all'">
          <div class="brand-items">
            <div
              v-for="brand in brandList"
              :key="brand.id"
              class="brand-item"
              @click="selectBrandItem(brand.id)"
            >
              <div class="brand-logo" v-if="brand.brandLogo">
                <img :src="brand.brandLogo" :alt="brand.brandName" />
              </div>
              <div class="brand-name" :class="{ active: searchParams.brandId === brand.id }">
                {{ brand.brandName }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 车型和能源筛选 -->
    <el-card shadow="never" class="mb-15px">
      <el-tabs v-model="selectedVehicleType" @tab-change="selectVehicleType">
        <el-tab-pane label="不限" name="all" />
        <el-tab-pane
          v-for="vehicleType in vehicleTypeDictList"
          :key="vehicleType.id"
          :label="vehicleType.typeName"
          :name="vehicleType.id"
        />
      </el-tabs>
      <div class="tag-bar">
        <el-tabs v-model="selectedEnergyType" @tab-change="selectEnergyType">
          <el-tab-pane label="不限" name="all" />
          <el-tab-pane
            v-for="energyType in energyTypeDictList"
            :key="energyType.id"
            :label="energyType.typeName"
            :name="energyType.id"
          />
        </el-tabs>
      </div>
      <div class="tag-bar">
        <el-tabs v-model="selectedSeatCount" @tab-change="handleSeatCountChange">
          <el-tab-pane label="不限" name="all" />
          <el-tab-pane
            v-for="seatCount in seatCountList"
            :key="seatCount"
            :label="`${seatCount}座`"
            :name="seatCount"
          />
        </el-tabs>
      </div>
    </el-card>

    <!-- 车辆列表 -->
    <el-card shadow="never" v-loading="loading">
      <el-row :gutter="16">
        <el-col
          v-for="vehicle in dataList"
          :key="vehicle.id"
          :xs="24"
          :sm="12"
          :md="8"
          :lg="6"
          :xl="4"
          class="mb-15px"
        >
          <el-card
            :body-style="{ padding: '0px' }"
            shadow="hover"
            class="picture-card"
            @click="doClickVehicle(vehicle)"
          >
            <el-image
              :src="vehicle.imageUrl"
              :alt="vehicle.name"
              fit="cover"
              class="picture-image"
              :preview-src-list="[vehicle.imageUrl]"
            />
            <div class="picture-info">
              <h3 class="picture-title">{{ vehicle.name }}</h3>
              <div class="text-center color-#f56c6c">{{ vehicle.dailyPrice }}元/天</div>
              <div class="picture-tags">
                <el-tag size="small" type="success" class="mr-5px">
                  {{ vehicle.modelName }}
                </el-tag>
                <el-tag v-if="vehicle.brandId" size="small" type="warning" class="mr-5px">
                  {{ vehicle.brandName }}
                </el-tag>
                <el-tag size="small" class="mr-5px"> {{ vehicle.seatCount }}座 </el-tag>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 分页 -->
      <div class="mt-20px flex justify-end">
        <el-pagination
          v-model:currentPage="searchParams.current"
          v-model:pageSize="searchParams.pageSize"
          :total="total"
          :page-sizes="[12, 24, 36]"
          background
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { listVehicleBrandByLetterUsingGet } from '@/api/vehicleBrandController.ts'
import { listVehicleTypeDictUsingPost } from '@/api/vehicleTypeDictController.ts'
import { listEnergyTypeDictUsingPost } from '@/api/energyTypeDictController.ts'
import { listVehicleByPageUsingPost } from '@/api/vehicleController.ts'

// 定义数据
const dataList = ref<API.VehicleVO[]>([])
const total = ref(0)
const loading = ref(true)

// 搜索条件
const searchParams = reactive<API.VehicleQueryRequest>({
  current: 1,
  pageSize: 12,
  sortField: 'createTime',
  sortOrder: 'descend',
})

// 车型和能源类型列表
const vehicleTypeDictList = ref<API.VehicleTypeDictVO[]>([])
const energyTypeDictList = ref<API.EnergyTypeDictVO[]>([])
const selectedVehicleType = ref<any>('all')
const selectedEnergyType = ref<any>('all')
const selectedSeatCount = ref<any>('all')

// 品牌相关
const selectedBrand = ref<string>('all')
const brandLetters = [
  'A',
  'B',
  'C',
  'D',
  'E',
  'F',
  'G',
  'H',
  'I',
  'J',
  'K',
  'L',
  'M',
  'N',
  'O',
  'P',
  'Q',
  'R',
  'S',
  'T',
  'U',
  'V',
  'W',
  'X',
  'Y',
  'Z',
]

// 座位数
const seatCountList = [4, 5, 6, 7, 8]

// 品牌数据
const brandList = ref<API.VehicleBrandVO[]>([])

// 选择具体品牌
const selectBrandItem = (brandId: any) => {
  searchParams.brandId = brandId
  doSearch()
}

// 品牌字母切换
const handleBrandChange = (value: any) => {
  if (selectedBrand.value !== 'all') {
    getBrandList()
  } else {
    searchParams.brandId = undefined
  }
  doSearch()
}

// 选择车型
const selectVehicleType = (vehicleTypeId: any) => {
  searchParams.vehicleTypeId =
    selectedVehicleType.value === 'all' ? undefined : selectedVehicleType.value
  doSearch()
}

// 选择能源类型
const selectEnergyType = (energyTypeId: any) => {
  searchParams.energyTypeId =
    selectedEnergyType.value === 'all' ? undefined : selectedEnergyType.value
  doSearch()
}

// 选择座位数
const handleSeatCountChange = (seatCount: any) => {
  searchParams.seatCount = selectedSeatCount.value === 'all' ? undefined : selectedSeatCount.value
  doSearch()
}

// 获取数据
const fetchData = async () => {
  loading.value = true
  try {
    const res = await listVehicleByPageUsingPost(searchParams)
    if (res.data.code === 0 && res.data.data) {
      dataList.value = res.data.data.records ?? []
      total.value = Number(res.data.data.total) || 0
    } else {
      ElMessage.error('获取数据失败，' + res.data.message)
    }
  } catch (error) {
    ElMessage.error('获取数据失败：' + error)
  } finally {
    loading.value = false
  }
}

// 获取品牌
const getBrandList = async () => {
  try {
    const res = await listVehicleBrandByLetterUsingGet({
      letter: selectedBrand.value,
    })
    if (res.data.code === 0 && res.data.data) {
      brandList.value = res.data.data ?? ([] as API.VehicleBrandVO[])
    } else {
      ElMessage.error('获取品牌列表失败，' + res.data.message)
    }
  } catch (error) {
    ElMessage.error('获取品牌列表失败：' + error)
  }
}

// 获取车系
const getVehicleTypeList = async () => {
  try {
    const res = await listVehicleTypeDictUsingPost()
    if (res.data.code === 0 && res.data.data) {
      vehicleTypeDictList.value = res.data.data ?? ([] as API.VehicleTypeDictVO[])
    } else {
      ElMessage.error('获取车系列表失败，' + res.data.message)
    }
  } catch (error) {
    ElMessage.error('获取车系列表失败：' + error)
  }
}

// 获取能源类型
const getEnergyTypeList = async () => {
  try {
    const res = await listEnergyTypeDictUsingPost()
    if (res.data.code === 0 && res.data.data) {
      energyTypeDictList.value = res.data.data ?? ([] as API.EnergyTypeDictVO[])
    } else {
      ElMessage.error('获取能源类型列表失败，' + res.data.message)
    }
  } catch (error) {
    ElMessage.error('获取能源类型列表失败：' + error)
  }
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

const router = useRouter()
// 跳转至车辆详情页
const doClickVehicle = (vehicle: API.VehicleVO) => {
  router.push(`/vehicle/${vehicle.id}`)
}

// 页面加载时获取数据
onMounted(() => {
  getBrandList()
  getVehicleTypeList()
  getEnergyTypeList()
  fetchData()
})
</script>

<style scoped>
.mb-15px {
  margin-bottom: 15px;
}

.mr-5px {
  margin-right: 5px;
}

.search-bar {
  max-width: 480px;
  margin: 0 auto;
}

.tag-bar {
  margin-top: 16px;
}

/* 品牌选择样式 */
.brand-filter {
  margin-bottom: 10px;
}

.brand-title {
  font-weight: bold;
  margin-bottom: 12px;
}

.brand-list {
  margin-bottom: 15px;
}

.brand-content {
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #ebeef5;
}

.brand-items {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.brand-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  width: 80px;
}

.brand-logo {
  width: 40px;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 5px;
}

.brand-logo img {
  max-width: 100%;
  max-height: 100%;
}

.brand-name {
  font-size: 12px;
  text-align: center;
  color: #606266;
}

.brand-name.active {
  color: #409eff;
  font-weight: bold;
}

/* 图片卡片样式 */
.picture-card {
  cursor: pointer;
  transition: all 0.3s;
}

.picture-card:hover {
  transform: translateY(-5px);
}

.picture-image {
  width: 100%;
  height: 180px;
}

.picture-info {
  padding: 14px;
}

.picture-title {
  margin: 0 0 10px 0;
  font-size: 16px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.picture-tags {
  display: flex;
  flex-wrap: nowrap;
  overflow: hidden;
  gap: 5px;
}
</style>
