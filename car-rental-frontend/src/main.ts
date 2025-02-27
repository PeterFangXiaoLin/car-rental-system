import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

import ElementPlus from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn' // 国际化
import 'element-plus/dist/index.css'
import 'dayjs/locale/zh-cn' // 日期和时间本地化
import dayjs from "dayjs"
import 'virtual:uno.css' // unocss

import '@/access' // 权限控制

const app = createApp(App)

dayjs.locale("zh-cn")

app.use(createPinia())
app.use(router)
app.use(ElementPlus, {
  locale: zhCn,
})

app.mount('#app')
