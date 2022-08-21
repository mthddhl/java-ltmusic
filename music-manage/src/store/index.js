import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    Host: 'http://120.25.161.31',
    isPlay: false,
    url: '',
    httpFileUrl: 'http://120.25.161.31:9000/music/'
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
