import { createStore } from 'vuex'

export default createStore({
    state: {
        songList:{

        },
        isVip:false,
        httpUrl:'http://120.25.161.31',
        // httpFileUrl:'http://120.25.161.31:9000/music/',
        httpFileUrl:'http://120.25.161.31/music/',
        consumer:{
            id:null,
            roles:null,
        },
        firstLetter:['全部','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z']
    },
    getters:{

    },
    mutations: {
    },
    actions: {
    },
    modules: {
    }
})

