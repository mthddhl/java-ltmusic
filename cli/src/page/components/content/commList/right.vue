<template>
<div>
    <div class="likes">
        <div style="text-align:left;
                    margin-left:10px">
            <span>
            喜欢这个歌单的人
            </span>
        </div>
        <el-divider style="margin-top:2px"/>
        <el-space :fill="false" wrap :fill-ratio="10" :size="25" style="margin-left: 40px">
            <div v-for="i in likeThisList" :key="i" style="width: 60px;height: 60px;cursor: pointer" @click="toConsumer(i.id)">
                <a-avatar style="height: 60px;width: 60px">
                    <img
                            alt="avatar"
                            :src="this.$store.state.httpFileUrl+i.avator"
                    />
                </a-avatar>
            </div>
        </el-space>
    </div>
    <div >
        <div style="text-align:left;
                    margin-left:10px">
            <span>
            热门歌单
            </span>
        </div>
        <el-divider style="margin-top:2px"/>
        <el-space :fill="false" wrap :fill-ratio="10" :size="15" style="margin-left: 40px">
            <div v-for="i in songListPart" :key="i" style="cursor: pointer;width: 330px;height: 80px;text-align:left;"
                @click="change(i)">
                <el-avatar shape="square" :size="60" :src='this.$store.state.httpFileUrl+i.pic' style="margin-top: 10px;float: left" />
                <div style="float: left;margin-top:18px;margin-left: 10px;font-size: 17px">
                    <div style="position:absolute;width: 240px;">
                        <span style="word-break:break-all;">{{i.title}}</span>
                    </div>
                </div>
            </div>
        </el-space>
    </div>
</div>
</template>

<script>
    import {getLikeSongList, getPartSongList} from '@/ajax/getAndPost'
    export default {
        name: "",
        data(){
            return{
                songListPart:[{}],
                likeThisList:[],
                songListId:-1,
            }
        },
        methods:{
            change(i){
               let id=i.id
               this.$router.push({path:'/songList',query:{id}})
            },
            toConsumer(id){
                this.$router.push({path:'/consumer',query:{id}})
            },
            init(){
                getPartSongList().then(res=>{
                    if(res.success){
                        this.songListPart=res.data.slice(0,5);
                    }
                })
                getLikeSongList({"id":this.songListId}).then(res=>{
                    if(res.success){
                        this.likeThisList=res.data
                    }
                })
            }
        },
        created() {
            this.songListId=this.$route.query.id
            this.init()
        },
        watch:{
            $route(to, from) {
                if (to.query.id !== from.query.id) {
                    this.songListId=this.$route.query.id
                    this.init()
                }
            },
        },
    }
</script>

<style scoped>
.likes{
    margin-top:20px;
    height: 300px;
}
</style>