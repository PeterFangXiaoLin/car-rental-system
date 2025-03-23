import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

import ElementPlus from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn' // 国际化
import 'element-plus/dist/index.css'
import 'dayjs/locale/zh-cn' // 日期和时间本地化
import dayjs from 'dayjs'
import 'virtual:uno.css' // unocss
import VueCropper from 'vue-cropper' // 图片编辑
import 'vue-cropper/dist/index.css'

import VueAMap, { initAMapApiLoader } from '@vuemap/vue-amap' // 地图
import '@vuemap/vue-amap/dist/style.css'
import '@vuemap/amap-jsapi-types'

import '@/access' // 权限控制

const app = createApp(App)

initAMapApiLoader({
  key: '057c2b362166596ed5265b3eb0ff78f1',
  securityJsCode: '2b380a75669690008ab9dd509b86237f', // 新版key需要配合安全密钥使用
  plugins: ['AMap.Geocoder'], // 全局注册插件
})

dayjs.locale('zh-cn')

app.use(createPinia())
app.use(router)
app.use(ElementPlus, {
  locale: zhCn,
})
app.use(VueCropper)
app.use(VueAMap)

app.mount('#app')
