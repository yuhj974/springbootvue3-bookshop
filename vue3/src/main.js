import { createApp } from 'vue'

import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import { createPinia } from 'pinia'
import './assets/main.css'
import ECharts from 'vue-echarts';
import { use } from 'echarts/core';
import { CanvasRenderer } from 'echarts/renderers';
import { PieChart, BarChart } from 'echarts/charts';  // 按需引入图表类型
import Toast from 'vue-toastification'
import 'vue-toastification/dist/index.css' // 必须引入样式文件
import { TitleComponent, TooltipComponent, LegendComponent, GridComponent } from 'echarts/components';
// 配置 ECharts 核心组件
use([
  CanvasRenderer,
  PieChart,
  BarChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
]);
const app = createApp(App)
const pinia = createPinia()
app.component('v-chart', ECharts);  // 注册全局组件
app.use(router)
app.use(pinia)
app.use(ElementPlus, {locale: zhCn})
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
app.use(Toast, {
  timeout: 3000, // 默认显示时长
  closeOnClick: true,
  pauseOnFocusLoss: true,
})
app.mount('#app')
