import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    Host: 'http://xxxx',
    isPlay: false,
    url: '',
    httpFileUrl: 'http://xxxxx:xxx/music/'
  },
  getters: {
    isPlay: state => state.isPlay,
    url: state => state.url
  },
  mutations: {
    setIsplay: (state, isPlay) => {
      state.isPlay = isPlay
    },
    setUrl: (state, url) => {
      state.url = url
    }
  }
}
)

export default store
