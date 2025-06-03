<template>
  <el-input
    v-model="selectedStore.storeName"
    placeholder="请选择门店"
    @click="openStoreDialog"
    readonly
    class="store-input"
    :prefix-icon="Location"
  />
  <el-dialog v-model="dialogVisible" title="选择门店" width="850px" destroy-on-close center>
    <div class="selected-store-info" v-if="selectedStoreId">
      <div class="store-info-header">当前选择的门店</div>
      <div class="store-info-content">
        <div><span class="label">门店名称：</span>{{ selectedStore.storeName }}</div>
        <div><span class="label">门店地址：</span>{{ selectedStore.address }}</div>
      </div>
    </div>

    <div class="map-container">
      <el-amap ref="mapRef" :center="center" :zoom="zoom" @init="init" />
    </div>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmSelection" :disabled="!selectedStoreId">
          确定
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ElAmap } from '@vuemap/vue-amap'
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Location } from '@element-plus/icons-vue'
import { listStoreByCityNameUsingGet } from '@/api/storeController'

// 选择的门店
const storeId = defineModel<string>('storeId', { default: '' })
const dialogVisible = ref(false)

// 当前选择的门店
const selectedStore = reactive<{
  id: string
  storeName: string
  address: string
}>({
  id: '',
  storeName: '',
  address: '',
})

// 选中的门店ID
const selectedStoreId = ref('')

// 定义地图参数
const center = ref([113.380183, 23.122169]) // 默认中心点(广州)
const zoom = ref(12)
let map: AMap.Map
// 存储所有标记点
const markers: AMap.Marker[] = []
// 存储信息窗口
let infoWindow: AMap.InfoWindow | null = null

// 城市名称
const cityName = ref('广州')

// 存储门店数据
const storeList = ref<API.StoreVO[]>([])
const mapRef = ref()

// 打开门店选择对话框
const openStoreDialog = () => {
  dialogVisible.value = true
}

// 初始化地图
const init = (e: AMap.Map) => {
  map = e

  // 创建信息窗口
  infoWindow = new AMap.InfoWindow({
    offset: new AMap.Pixel(0, -30), // 设置信息窗口的偏移量
    closeWhenClickMap: true, // 点击地图其他区域时关闭信息窗口
  })

  // 获取定位
  getLocation()

  // 获取门店列表
  fetchStoreList()
}

// 获取定位
const getLocation = () => {
  if (!map) return

  const citySearch = new AMap.CitySearch()
  citySearch.getLocalCity((status, result) => {
    if (status === 'complete' && result.info === 'OK') {
      let city = result.city
      if (city.indexOf('市') !== -1) {
        city = city.replace('市', '')
      }
      cityName.value = city
    } else {
      ElMessage.error('定位失败')
    }
  })
}

// 获取门店列表
const fetchStoreList = async () => {
  try {
    const res = await listStoreByCityNameUsingGet({ cityName: cityName.value })
    if (res.data?.code === 0 && res.data.data) {
      storeList.value = res.data.data
      displayStores(storeList.value)
      ElMessage.success(`已显示${cityName.value}的${storeList.value.length}家门店`)

      // 如果已经选择了门店，显示该门店信息
      if (storeId.value) {
        showSelectedStore()
      }
    } else {
      ElMessage.error('加载门店列表失败')
    }
  } catch (error) {
    ElMessage.error('加载门店列表失败:' + error)
  }
}

// 显示已选择的门店
const showSelectedStore = () => {
  const store = storeList.value.find((item) => String(item.id) === storeId.value)
  if (store) {
    // 设置选中的门店
    selectedStoreId.value = String(store.id)
    selectedStore.id = String(store.id || '')
    selectedStore.storeName = store.storeName || ''
    selectedStore.address = store.address || ''

    // 如果有经纬度信息，移动地图到该位置
    if (store.longitude && store.latitude) {
      center.value = [store.longitude, store.latitude]
      map.setCenter([store.longitude, store.latitude])
      map.setZoom(16)

      // 打开信息窗口
      if (infoWindow) {
        showStoreInfoWindow(store)
      }
    }
  }
}

// 显示门店标记
const displayStores = (stores: API.StoreVO[]) => {
  if (!map) return

  // 清除现有标记
  if (markers.length > 0) {
    map.remove(markers)
    markers.length = 0
  }

  // 创建门店标记
  stores.forEach((store) => {
    // 跳过没有经纬度的门店
    if (!store.longitude || !store.latitude) return

    // 创建标记
    const marker = new AMap.Marker({
      position: [store.longitude, store.latitude],
      icon: new AMap.Icon({
        image: 'https://webapi.amap.com/theme/v1.3/markers/n/mark_r.png', // 红色标记
        size: new AMap.Size(25, 34),
        imageSize: new AMap.Size(25, 34),
      }),
      offset: new AMap.Pixel(-12, -34),
      title: store.storeName || '未命名门店',
    })

    // 添加点击事件
    marker.on('click', () => {
      // 设置选中的门店
      selectedStoreId.value = String(store.id || '')
      selectedStore.id = String(store.id || '')
      selectedStore.storeName = store.storeName || ''
      selectedStore.address = store.address || ''

      // 显示信息窗口
      showStoreInfoWindow(store)
    })

    // 将标记添加到地图
    map.add(marker)
    markers.push(marker)
  })
}

// 显示门店信息窗口
const showStoreInfoWindow = (store: API.StoreVO) => {
  if (!infoWindow || !map) return

  // 设置信息窗口内容
  const imageHtml = store.images
    ? `<div class="store-image"><img src="${store.images.split(',')[0]}" alt="${store.storeName || '门店图片'}" /></div>`
    : ''

  infoWindow.setContent(`
    <div class="info-window store-info">
      <h3>${store.storeName || '未命名门店'}</h3>
      ${imageHtml}
      <p><strong>地址:</strong> ${store.address || '暂无地址'}</p>
      <p><strong>电话:</strong> ${store.mobile || '暂无电话'}</p>
      <p><strong>营业时间:</strong> ${store.openTime || '00:00'}-${store.closeTime || '00:00'}</p>
    </div>
  `)

  // 打开信息窗口
  infoWindow.open(map, [store.longitude || 0, store.latitude || 0])
}

// 确认选择
const confirmSelection = () => {
  if (!selectedStoreId.value) {
    ElMessage.warning('请先选择一个门店')
    return
  }

  // 更新model值
  storeId.value = selectedStore.id

  // 关闭对话框
  dialogVisible.value = false

  ElMessage.success(`已选择门店: ${selectedStore.storeName}`)
}
</script>

<style scoped>
.store-input {
  width: 100%;
}

.map-container {
  width: 100%;
  height: 500px;
  position: relative;
  margin-top: 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  overflow: hidden;
}

.status-bar {
  margin-bottom: 10px;
}

.selected-store-info {
  padding: 12px;
  background-color: #f0f9ff;
  border-radius: 4px;
  border-left: 4px solid #409eff;
}

.store-info-header {
  font-weight: bold;
  font-size: 16px;
  margin-bottom: 8px;
  color: #303133;
}

.store-info-content {
  line-height: 1.8;
  color: #606266;
}

.store-info-content .label {
  font-weight: bold;
  margin-right: 5px;
}

:deep(.info-window) {
  padding: 8px;
  min-width: 200px;
}

:deep(.info-window h3) {
  margin: 0 0 10px 0;
  font-size: 16px;
  color: #303133;
}

:deep(.info-window p) {
  margin: 0 0 5px 0;
  font-size: 14px;
  line-height: 1.5;
  color: #606266;
}

:deep(.store-image) {
  margin-bottom: 10px;
}

:deep(.store-image img) {
  width: 100%;
  max-height: 120px;
  object-fit: cover;
  border-radius: 4px;
}

:deep(.select-store-btn) {
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 5px 10px;
  margin-top: 10px;
  cursor: pointer;
  font-size: 14px;
}

:deep(.select-store-btn:hover) {
  background-color: #66b1ff;
}
</style>
