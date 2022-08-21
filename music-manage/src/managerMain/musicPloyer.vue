<template>
  <div>
    <audio id="player"
           :src="url"
           preload="false"
           :controls="isPlay"
           @canplay="startPlay"
           @ended="ended"
      >

    </audio>

  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  export default {
    name: "",
    data(){
      return{
      }
    },
    computed:{
      ...mapGetters([
        'url',
        'isPlay'
      ])
    },
    watch:{
      isPlay:function () {
        this.togglePlay()
      }
    },
    methods:{
      startPlay(){
        let player=document.getElementById("player")
        player.play();
      },
      ended(){
        this.isPlay=false
      },
      togglePlay(){
        let player=document.getElementById("player")
        if(this.isPlay){
          player.play()
        }else {
          player.pause()
        }
      }
    }
  }
</script>

<style scoped>

</style>
