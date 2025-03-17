<template>
  <div class="map-page-container">
    <el-amap
      :center="center"
      :zoom="zoom"
      @init="init"
      @click="handleClick"
    />
    <div class="map-controls">
      <el-button type="primary" @click="clearMarkers">清除所有标记</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ElAmap} from "@vuemap/vue-amap";
import {ref} from "vue"

// 定义地图参数
const center = ref([113.380183, 23.122169])
const zoom = ref(12)
let map: AMap.Map
// 存储所有标记点
const markers: AMap.Marker[] = []
// 存储信息窗口
let infoWindow: AMap.InfoWindow | null = null

const init = (e: AMap.Map) => {
  map = e
  // 创建信息窗口
  infoWindow = new AMap.InfoWindow({
    offset: new AMap.Pixel(0, -30), // 设置信息窗口的偏移量
    closeWhenClickMap: true // 点击地图其他区域时关闭信息窗口
  })
}

// 为点击事件定义正确的类型
const handleClick = (e: { lnglat: { lng: number; lat: number } }) => {
  const {lnglat} = e
  // 明确指定position为[number, number]类型，符合高德地图API要求
  const position: [number, number] = [lnglat.lng, lnglat.lat]
  const positionText = `经度：${lnglat.lng.toFixed(6)}, 纬度：${lnglat.lat.toFixed(6)}`
  
  // 创建一个标记点
  const marker = new AMap.Marker({
    position: position,
    cursor: 'pointer', // 鼠标悬停时显示手型光标
    title: positionText
  })
  
  // 添加点击事件
  marker.on('click', () => {
    if (infoWindow) {
      // 设置信息窗口内容
      infoWindow.setContent(`
        <div class="info-window">
          <h3>位置信息</h3>
          <p>${positionText}</p>
        </div>
      `)
      // 打开信息窗口
      infoWindow.open(map, position)
    }
    
    // 调整地图中心点到标记点位置
    map.setCenter(position)
    // 放大地图
    const currentZoom = map.getZoom()
    // 确保不超过最大缩放级别(通常是18-20，根据具体地图可能有所不同)
    const newZoom = Math.min(currentZoom + 2, 18)
    map.setZoom(newZoom)
  })
  
  // 将标记点添加到地图
  map.add(marker)
  // 存储标记点以便后续管理
  markers.push(marker)
}

// 清除所有标记点的函数
const clearMarkers = () => {
  if (markers.length > 0) {
    map.remove(markers)
    markers.length = 0
    // 关闭信息窗口
    if (infoWindow) {
      infoWindow.close()
    }
  }
}
</script>

<style scoped>
.map-page-container {
  width: 800px;
  height: 600px;
  position: relative;
}

.map-controls {
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 100;
}

:deep(.info-window) {
  padding: 8px 0;
}

:deep(.info-window h3) {
  margin: 0 0 5px 0;
  font-size: 16px;
}

:deep(.info-window p) {
  margin: 0;
  font-size: 14px;
  line-height: 1.5;
}
</style>