import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      component: resolve => require(['../pages/Login.vue'], resolve)
    },
    {
      path: '/home',
      component: resolve => require(['../managerMain/Home'], resolve),
      redirect: '/Info',
      children: [
        {
          path: '/Info',
          component: resolve => require(['../managerMain/components/Info.vue'], resolve)
        },
        {
          path: '/Consumer',
          component: resolve => require(['../managerMain/components/Consumer'], resolve)
        },
        {
          path: '/Singer',
          component: resolve => require(['../managerMain/components/Singer.vue'], resolve),
          meta: {
            keepAlive: true,
            isBack: true
          }
        },
        {
          path: '/Songlist',
          component: resolve => require(['../managerMain/components/SongList.vue'], resolve)
        },
        {
          path: '/MV',
          component: resolve => require(['../managerMain/components/MV'], resolve)
        },
        {
          path: '/SongListForSinger',
          name: 'SongListForSinger',
          component: resolve => require(['../managerMain/components/SongListForSinger'], resolve)
        },
        {
          path: '/SongForSongList',
          component: resolve => require(['../managerMain/components/SongForSongList'], resolve)
        },
        {
          path: '/Carousel',
          component: resolve => require(['../managerMain/components/Carousel'], resolve)
        },
        {
          path: '/CouponsAndPrice',
          component: resolve => require(['../managerMain/components/VIP/CouponsAndPrice'], resolve)
        },
        {
          path: '/BuyManager',
          component: resolve => require(['../managerMain/components/VIP/BuyManager'], resolve)
        }
      ]
    }

  ]
})
