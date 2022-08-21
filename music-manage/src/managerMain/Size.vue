<template>
<div class="size">
  <el-menu
            class="sidebar-el-menu"
            :default-active="onRouter"
            :collapse="collapse"
            background-color="darkslategray"
            text-color="#ffffff"
            active-text-color="blue"
            router
            v-if="items.length>0">
    <template v-if="items" v-for="item in items">
      <el-menu-item :index="item.index" :key="item.index">
          <i :class="item.icon"></i>
          <span :title="item.title">{{item.title}}</span>
      </el-menu-item>
    </template>
    <el-submenu index="1">
      <template slot="title">
        <i :class="VIP.icon"></i>
        <span>{{VIP.title}}</span>
      </template>
      <el-menu-item-group style="margin-top: -20px">
        <el-menu-item v-for="item in VIP.index" :index="item.index" :key="item.index">
          {{item.title}}
        </el-menu-item>
      </el-menu-item-group>
      </el-submenu>

  </el-menu>
</div>
</template>

<script>
  import bus from "../assets/js/bus";
  export default {
    data(){
      return{
        collapse:false,
        items:[
          {
            icon:'el-icon-document',
            index:'Info',
            title:'系统首页'
          },
          {
            icon:'el-icon-document',
            index:'Consumer',
            title:'用户管理'
          },
          {
            icon:'el-icon-document',
            index:'Singer',
            title:'歌手管理'
          },
          {
            icon:'el-icon-document',
            index:'SongList',
            title:'歌单管理'
          },
          {
            icon:'el-icon-document',
            index:'Carousel',
            title:'走马灯管理'
          },
          {
            icon:'el-icon-document',
            index:'MV',
            title:'MV管理'
          },
        ],
        VIP:{
          title:"会员管理",
          icon:'el-icon-document',
          index:[{
            icon:'el-icon-document',
            index:'CouponsAndPrice',
            title:'会员购买'
          },
          {
            icon:'el-icon-document',
            index:'BuyManager',
            title:'购买记录'
          }]
        }
      }
    },
    computed:{
      onRouter(){
        return this.$route.path.replace('/','')
      }
    },
    created() {
      bus.$on('collapse',mes=>{
        this.collapse=mes
      })
    }
  }
</script>

<style scoped>
.size{
  position: absolute;
  display: block;
  left: 0;
  top: 70px;
  bottom: 0;
  background-color: darkslategray;
  overflow-y: scroll;
  /*width: 150px;*/
}
.size::-webkit-scrollbar{
  width: 0;
}
.sidebar-el-menu:not(.el-menu--collapse){
  width: 150px;
}
.size >ul{
  height: 100%;
}
</style>
