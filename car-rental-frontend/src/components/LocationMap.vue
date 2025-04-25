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
import { ElMessage } from "element-plus";

// 定义地图参数
const center = ref([113.380183, 23.122169]) // 默认中心点
const zoom = ref(12)
let map: AMap.Map
// 存储所有标记点
const markers: AMap.Marker[] = []
// 存储信息窗口
let infoWindow: AMap.InfoWindow | null = null

// 定位和加载状态
const locating = ref(false);
const loadingStores = ref(false);
const statusMessage = ref("");

// 当前城市信息
const currentCity = ref("");
const currentPosition = reactive({
  lng: 0,
  lat: 0
});

const init = (e: AMap.Map) => {
  map = e
  // 创建信息窗口
  infoWindow = new AMap.InfoWindow({
    offset: new AMap.Pixel(0, -30), // 设置信息窗口的偏移量
    closeWhenClickMap: true // 点击地图其他区域时关闭信息窗口
  })

  // 页面加载后自动尝试获取当前位置
  getCurrentLocation();
}

// 获取当前位置
const getCurrentLocation = () => {
  if (!map) return;
  
  locating.value = true;
  statusMessage.value = "正在获取您的位置...";
  
  // 创建定位对象
  const geolocation = new AMap.Geolocation({
    enableHighAccuracy: true, // 使用高精度定位
    timeout: 10000, // 超时时间，默认为无穷大
    maximumAge: 0, // 定位结果缓存时间，默认0
    convert: true, // 自动偏移坐标，为了更精确
    showButton: false, // 不显示定位按钮
    showMarker: false, // 不显示定位点
    showCircle: false, // 不显示定位精度圈
    panToLocation: false, // 不定位成功后将定位到地图中心点
    zoomToAccuracy: false // 不定位成功后调整地图视野范围使定位位置及精度范围视野内可见
  });
  
  // 监听定位成功事件
  geolocation.getCurrentPosition((status: string, result: any) => {
    if (status === 'complete') {
      // 定位成功，更新中心点
      const { position } = result;
      center.value = [position.lng, position.lat];
      
      // 存储当前位置
      currentPosition.lng = position.lng;
      currentPosition.lat = position.lat;
      
      // 创建一个用户位置的标记点
      const userMarker = new AMap.Marker({
        position: [position.lng, position.lat],
        icon: new AMap.Icon({
          image: 'https://webapi.amap.com/theme/v1.3/markers/n/mark_b.png',
          size: new AMap.Size(25, 34),
          imageSize: new AMap.Size(25, 34)
        }),
        offset: new AMap.Pixel(-12, -34),
        title: '当前位置'
      });
      
      // 添加标记点到地图
      map.add(userMarker);
      markers.push(userMarker);
      
      // 调整地图到适当的缩放级别
      map.setZoom(14);
      
      // 获取当前城市信息
      getCityByLocation(position.lng, position.lat);
      
      // 显示位置信息窗口
      if (infoWindow) {
        const address = result.formattedAddress || '未知地址';
        infoWindow.setContent(`
          <div class="info-window">
            <h3>您的当前位置</h3>
            <p>经度：${position.lng.toFixed(6)}, 纬度：${position.lat.toFixed(6)}</p>
            <p>地址：${address}</p>
          </div>
        `);
        infoWindow.open(map, [position.lng, position.lat]);
      }
      
      statusMessage.value = "定位成功：" + (result.formattedAddress || '未知地址');
      setTimeout(() => {
        statusMessage.value = "";
      }, 3000);
    } else {
      // 定位失败
      ElMessage.error('定位失败：' + result.message);
      statusMessage.value = "定位失败，请检查位置权限";
      setTimeout(() => {
        statusMessage.value = "";
      }, 3000);
    }
    locating.value = false;
  });
}

// 根据经纬度获取城市信息
const getCityByLocation = (lng: number, lat: number) => {
  const geocoder = new AMap.Geocoder();
  
  geocoder.getAddress([lng, lat], (status: string, result: any) => {
    if (status === 'complete' && result.regeocode) {
      const addressComponent = result.regeocode.addressComponent;
      currentCity.value = addressComponent.city || addressComponent.province;
      
      // 如果城市信息可用，则更新状态消息
      if (currentCity.value) {
        statusMessage.value = `您当前位于: ${currentCity.value}`;
        setTimeout(() => {
          statusMessage.value = `点击"显示附近门店"查看${currentCity.value}的所有门店`;
        }, 2000);
      }
    } else {
      ElMessage.warning('无法获取城市信息');
      currentCity.value = "";
    }
  });
}

// 加载门店信息
const loadStores = async () => {
  if (!currentCity.value) {
    ElMessage.warning('请先获取位置信息');
    return;
  }
  
  loadingStores.value = true;
  statusMessage.value = `正在加载${currentCity.value}的门店信息...`;
  
  try {
    // 清除之前的门店标记
    clearStoreMarkers();
    
    // 这里需要调用你的后端API获取门店数据
    // 下面是模拟的API调用，你需要替换为实际的API
    // const response = await fetch(`/api/stores?city=${encodeURIComponent(currentCity.value)}`);
    // const stores = await response.json();
    
    // 模拟数据，你需要替换为实际API返回的数据
    const stores = mockStoreData(currentCity.value);
    
    // 显示门店标记
    displayStores(stores);
    
    // 调整地图视野以包含所有门店
    if (stores.length > 0) {
      // 创建包含当前位置和所有门店的边界
      const bounds = new AMap.Bounds([currentPosition.lng, currentPosition.lat]);
      stores.forEach(store => {
        bounds.extend([store.longitude, store.latitude]);
      });
      
      // 设置地图视野
      map.setBounds(bounds);
    }
    
    statusMessage.value = `已显示${currentCity.value}的${stores.length}家门店`;
    setTimeout(() => {
      statusMessage.value = "";
    }, 3000);
    
  } catch (error) {
    console.error('加载门店失败:', error);
    ElMessage.error('加载门店信息失败');
    statusMessage.value = "加载门店失败，请重试";
  } finally {
    loadingStores.value = false;
  }
}

// 显示门店标记
const displayStores = (stores: any[]) => {
  if (!map) return;
  
  // 创建门店标记
  stores.forEach(store => {
    // 创建标记
    const marker = new AMap.Marker({
      position: [store.longitude, store.latitude],
      icon: new AMap.Icon({
        image: 'https://webapi.amap.com/theme/v1.3/markers/n/mark_r.png', // 红色标记
        size: new AMap.Size(25, 34),
        imageSize: new AMap.Size(25, 34)
      }),
      offset: new AMap.Pixel(-12, -34),
      title: store.storeName
    });
    
    // 添加点击事件
    marker.on('click', () => {
      if (infoWindow) {
        // 设置信息窗口内容
        infoWindow.setContent(`
          <div class="info-window store-info">
            <h3>${store.storeName}</h3>
            <p><strong>地址:</strong> ${store.address}</p>
            <p><strong>电话:</strong> ${store.contactPhone}</p>
            <p><strong>营业时间:</strong> ${store.openTime}-${store.closeTime}</p>
            ${store.description ? `<p><strong>简介:</strong> ${store.description}</p>` : ''}
          </div>
        `);
        // 打开信息窗口
        infoWindow.open(map, [store.longitude, store.latitude]);
      }
    });
    
    // 将标记添加到地图
    map.add(marker);
    markers.push(marker);
  });
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
  display: flex;
  flex-direction: column;
  gap: 10px;
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