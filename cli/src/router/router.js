import {createRouter, createWebHashHistory, createWebHistory} from 'vue-router'
const routerHistory = createWebHistory()


const routers=[
    {
        path: '/',
        component: () => import('../page/Home'),
        redirect: 'info',
        children:[
            {
                path: 'info',
                component: () => import('../page/components/content/Info/Info'),
            },
            {
                path: 'info/songList',
                component: () => import('../page/components/content/songList/songList'),
            },
            {
                path: 'info/singer',
                component: () => import('../page/components/content/singer/singer'),
            },
            {
                path: 'info/MV',
                component: () => import('../page/components/content/MV/MV'),
            },
            {
                path: 'info/myMusic',
                component: () => import('../page/components/content/myMusic/myMusic'),
            },
            {
                path: 'info/VIP',
                component: () => import('../page/components/content/VIP/main'),
                redirect: '/VIP/content',
                children: [
                    {
                        path: '/VIP/content',
                        component: () => import('../page/components/content/VIP/content'),
                    },
                    {
                        path: '/VIP/BuyAndCost',
                        component: () => import('../page/components/content/VIP/BuyAndCost')
                    },
                ]
            },
            {
                path: 'songList',
                component: () => import('../page/components/content/commList/main'),
            },
            {
                path: 'singer',
                component: () => import('../page/components/content/commSinger/main'),
            },
            {
                path: 'search',
                component: () => import('../page/components/content/search/main'),
            },
            {
                path: 'consumer',
                component: () => import('../page/components/content/personal/PersonMain'),
                redirect: '/personal/LikeSingList',
                children:[
                    {
                        path: '/personal/LikeSingList',
                        component: () => import('../page/components/content/personal/ConsumerLikeSingList'),
                    },
                    {
                        path: '/personal/LikeSongs',
                        component: () => import('../page/components/content/personal/ConsumerLikeSongs'),
                    },
                    {
                        path: '/personal/LikeSinger',
                        component: () => import('../page/components/content/personal/ConsumerLikeSinger'),
                    },
                    {
                        path: '/personal/ConsumerCreatedSongList',
                        component: () => import('../page/components/content/personal/ConsumerCreatedSongList'),
                    },
                ]
            },

        ]
    },
    {
        path: "/login",
        component:()=>import("../page/login")
    },
    {
        path: "/register",
        component:()=>import("../page/register")
    }
]
const router= createRouter({
    history:createWebHashHistory(),
    routes:routers,
})
export default router;