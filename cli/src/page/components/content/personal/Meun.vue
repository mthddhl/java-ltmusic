<template>
<div>
<!--    <el-menu-->
<!--            active-text-color="#ffd04b"-->
<!--            background-color="#545c64"-->
<!--            class="el-menu-vertical-demo"-->
<!--            :default-active="activeIndex"-->
<!--            text-color="#fff"-->
<!--            @open="handleOpen"-->
<!--            @close="handleClose"-->
<!--            style="width: 150px;position: absolute;height: 100%"-->
<!--            router-->
<!--    >-->
<!--        <el-menu-item v-for="i in meun" :index="i.index" :key="i.index">-->
<!--            <span>{{i.name}}</span>-->
<!--        </el-menu-item>-->
<!--    </el-menu>-->
    <el-menu
            :default-active="activeIndex"
            class="el-menu-demo"
            mode="horizontal"
            @select="handleSelect"
    >
     <el-menu-item v-for="i in meun" :index="i.index" :key="i.index" class="menu">
         {{i.name}}
     </el-menu-item>
    </el-menu>
</div>
</template>

<script>
    export default {
        name: "",
        props:{
          'consumerId':Number
        },
        data(){
          return{
              meun:[
                  {index:'/personal/LikeSingList',name:'收藏的歌单'},
                  {index:'/personal/LikeSongs',name:'关注的歌曲'},
                  {index:'/personal/LikeSinger',name:'喜欢的歌手'},
                  {index:'/personal/ConsumerCreatedSongList',name:'创建的歌单'},
              ]
          }
        },
        methods:{
            handleSelect(key, keyPath) {
                let id=this.consumerId
                this.$router.push({path:key,query:{id}})
            },
        },
        computed:{
            activeIndex(){
                return this.$route.path.replace('//','')
            }
        },

    }
</script>

<style scoped>
.el-menu-demo{

}
.menu{
    margin-left: auto;
    margin-right: auto;
}
</style>