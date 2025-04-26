<template>
  <el-input
    v-model="modelValue"
    @click="dialogVisible = true"
    placeholder="选择地址"
    readonly
    class="address-input"
    :prefix-icon="Location"
  />

  <el-dialog v-model="dialogVisible" title="选择地点" width="800px" top="5vh">
    <div class="address-info-container">
      <div class="address-card">
        <div class="address-label">当前选择的地址：</div>
        <div class="address-value">{{ modelValue || '请在地图上选择位置或搜索地址' }}</div>
      </div>
      <div v-if="selectedPosition" class="coordinates-info">
        <span>经度：{{ selectedPosition.lng.toFixed(6) }}</span>
        <span>纬度：{{ selectedPosition.lat.toFixed(6) }}</span>
      </div>
    </div>
    <div class="map-page-container">
      <el-amap ref="mapRef" :center="center" :zoom="zoom" @init="init" @click="handleClick">
        <el-amap-search-box
          :visible="true"
          :debounce="1000"
          @select="selectPoi"
          @choose="choosePoi"
          placeholder="请输入所在地附近的地名"
          :closeResultOnScroll="false"
        />
      </el-amap>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmLocation" :disabled="!selectedPosition"
          >确定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { ElAmap, ElAmapSearchBox } from '@vuemap/vue-amap'
import { Location } from '@element-plus/icons-vue'

// 使用defineModel让父组件可以双向绑定地址
const modelValue = defineModel<string>('modelValue', { default: '' })

// 定义事件
const emit = defineEmits(['positionSelected'])

const selectedPosition = ref<{ lng: number; lat: number } | null>(null)
const selectCityName = ref('')

const mapRef = ref()
const zoom = ref(12)
const center = ref([113.2644, 23.1291])
let map: AMap.Map | null = null
let marker: AMap.Marker | null = null

const init = (e: AMap.Map) => {
  map = e
}

const handleClick = (mapsEvent: { lnglat: { lng: number; lat: number } }) => {
  if (!map) return

  const { lnglat } = mapsEvent

  // 清除之前的标记
  if (marker) {
    map.remove(marker)
  }

  // 创建新标记
  marker = new AMap.Marker({
    position: [lnglat.lng, lnglat.lat],
  })
  map.add(marker)

  // 保存选中的位置信息
  selectedPosition.value = {
    lng: lnglat.lng,
    lat: lnglat.lat,
  }

  // 通过逆地理编码获取位置的详细地址
  getAddressByLocation(lnglat.lng, lnglat.lat)
}

// 通过POI搜索选择位置
const selectPoi = (e: { id: string; name: string; location: { lng: number; lat: number } }) => {
  if (!map || !e.location) return

  // 清除之前的标记
  if (marker) {
    map.remove(marker)
  }

  // 创建新标记
  marker = new AMap.Marker({
    position: [e.location.lng, e.location.lat],
  })
  map.add(marker)

  // 保存选中的位置信息
  selectedPosition.value = {
    lng: e.location.lng,
    lat: e.location.lat,
  }

  // 设置地址
  modelValue.value = e.name
  // 设置城市
  getCityName(selectedPosition.value.lng, selectedPosition.value.lat)
}

// 选择POI
const choosePoi = (e: { id: string; name: string; location: { lng: number; lat: number } }) => {
  if (!e.location) return

  // 保存选中的位置信息
  selectedPosition.value = {
    lng: e.location.lng,
    lat: e.location.lat,
  }

  // 设置地址
  modelValue.value = e.name
  // 设置城市
  getCityName(selectedPosition.value.lng, selectedPosition.value.lat)
}

// 根据经纬度获取详细地址
const getAddressByLocation = (lng: number, lat: number) => {
  if (!map) return

  const geocoder = new AMap.Geocoder()

  geocoder.getAddress([lng, lat], (status, result) => {
    if (status === 'complete' && result.info === 'OK') {
      const address = result.regeocode.formattedAddress

      modelValue.value = address
      selectCityName.value = result.regeocode.addressComponent.city
    } else {
      ElMessage.warning('无法获取地址信息')
      modelValue.value = `经度：${lng.toFixed(6)}, 纬度：${lat.toFixed(6)}`
    }
  })
}

// 获取城市位置
const getCityName = (lng: number, lat: number) => {
  if (!map) return

  const geocoder = new AMap.Geocoder()

  geocoder.getAddress([lng, lat], (status, result) => {
    if (status === 'complete' && result.info === 'OK') {
      const cityName = result.regeocode.addressComponent.city
      selectCityName.value = cityName
    }
  })
}

// 确认选择位置，并将经纬度传递给父组件
const confirmLocation = () => {
  if (selectedPosition.value) {
    emit('positionSelected', {
      ...selectedPosition.value,
      cityName: selectCityName.value,
    })
    ElMessage.success('位置已选择')
    dialogVisible.value = false
  } else {
    ElMessage.warning('请先在地图上选择一个位置')
  }
}

const dialogVisible = ref(false)
</script>

<style scoped>
.address-input {
  width: 100%;
}

.map-container {
  margin-top: 15px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  overflow: hidden;
}

.search-box-container {
  padding: 10px;
  background-color: #f5f7fa;
}

.map-page-container {
  width: 100%;
  height: 450px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 10px;
}

.address-info-container {
  background-color: #f0f9ff;
  padding: 10px 15px;
  border-radius: 4px;
  border-left: 4px solid #409eff;
}

.address-card {
  display: flex;
  align-items: flex-start;
  margin-bottom: 5px;
}

.address-label {
  font-weight: bold;
  color: #606266;
  margin-right: 5px;
  white-space: nowrap;
}

.address-value {
  color: #303133;
  word-break: break-all;
  flex: 1;
}

.coordinates-info {
  display: flex;
  gap: 15px;
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}
</style>

<style>
.amap-sug-result {
  z-index: 9999;
}
</style>
