import Vue from 'vue'
import App from './App'
import router from './router/index'
import ElementUI from 'element-ui'
import store from './store/index'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/css/main.css'
import 'babel-polyfill'
import echarts from 'echarts'
import infiniteScroll from 'vue-infinite-scroll'
Vue.use(infiniteScroll)

Vue.use(ElementUI)
Vue.prototype.$echarts = echarts

/**
 * 重写路由的push方法
 */
// const routerPush = router.prototype.push
// router.prototype.push = function push (location) {
//   return routerPush.call(this, location).catch(error => error)
// }
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
