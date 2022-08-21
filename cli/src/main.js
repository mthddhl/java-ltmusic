import { createApp } from 'vue'
import store from './store/store'
import App from './App.vue'
import router from "./router/router"
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import ArcoVue from '@arco-design/web-vue';
import '@arco-design/web-vue/dist/arco.css';
import ArcoVueIcon from '@arco-design/web-vue/es/icon'
import '@arco-design/web-vue/dist/arco.css';
// import jwtDecode from 'jwt-decode'
// import elTableInfiniteScroll from 'el-table-infinite-scroll';
const app=createApp(App);
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {app.component(key, component)
}
// app.use(jwtDecode)
// app.use(elTableInfiniteScroll);
app.use(router);
app.use(ElementPlus);
app.use(store)
app.use(ArcoVue)
app.use(ArcoVueIcon);
app.mount('#App')
