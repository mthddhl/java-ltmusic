<template>
<div>
    <div class="comm" style="margin-top:0px;height:40px;">
        <span style="position:absolute;margin-left: 10px;margin-top: 20px">精彩推荐</span>
    </div>
    <el-divider style="margin-top: 3px;margin-bottom:10px;height: 1px"/>
    <Carousel></Carousel>
<!--    <div class="comm">-->
<!--        <span style="margin-left: 10px">热门歌单推荐</span>-->
<!--    </div>-->
<!--    <el-divider style="margin-top: 5px"/>-->
    <comment_song-list></comment_song-list>
<!--    <div class="comm">-->
<!--        <span style="margin-left: 10px">热门歌手推荐</span>-->
<!--    </div>-->
<!--    <el-divider style="margin-top: 5px"/>-->
    <comm_singer></comm_singer>
    <div class="comm">
        <span style="margin-left: 10px">热门歌曲推荐</span>
    </div>
    <el-divider style="margin-top: 5px;margin-bottom:10px"/>
    <comm_song></comm_song>
</div>
</template>

<script>
    import Carousel from './Carousel'
    import comment_songList from './comm_songList'
    import comm_song from './comm_song'
    import comm_singer from './comm_singer'
    export default {
        name: "",
        components:{
            Carousel,comment_songList,comm_song,comm_singer
        },
        mounted() {
          setTimeout(()=>{},200)
        }
    }
</script>

<style scoped>
    .comm{
        /*position:absolute;*/
        text-align: left;
        margin-top: 30px;
        color: #2aa3ef;
    }
</style>