<template>
  <div id="app">
    <MusicPloyer></MusicPloyer>
    <router-view></router-view>

  </div>
</template>

<script>
  import MusicPloyer from './managerMain/musicPloyer'
export default {
  name: 'App',
  components:{
    MusicPloyer
  },
  created() {

  }
}
</script>

<style>
</style>
