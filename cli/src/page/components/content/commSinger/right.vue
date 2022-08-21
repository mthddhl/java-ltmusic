<template>
    <div>
        <div class="likes">
            <div style="text-align:left;
                    margin-left:10px">
            <span>
            关注这个歌手的还有
            </span>
            </div>
            <el-divider style="margin-top:2px"/>
            <el-space :fill="false" wrap :fill-ratio="10" :size="25" style="margin-left: 40px">
                <div v-for="i in likeThisSinger" :key="i" style="width: 60px;height: 60px;cursor: pointer" @click="toConsumer(i.id)">
                    <a-avatar style="height: 60px;width: 60px">
                        <img
                                alt="avatar"
                                :src="this.$store.state.httpFileUrl+i.avator"
                        />
                    </a-avatar>
                </div>
            </el-space>
        </div>
        <div style="margin-top: 50px;">
            <div style="text-align:left;
                    margin-left:10px">
            <span>
            推荐歌手
            </span>
            </div>
            <el-divider style="margin-top:2px"/>
            <el-space :fill="false" wrap :fill-ratio="10" :size="15" style="margin-left: 40px">
                <div v-for="i in songListPart" :key="i" style="cursor: pointer;width: 330px;height: 80px;text-align:left;"
                     @click="change(i)">
                    <el-avatar shape="square" :size="60" :src='this.$store.state.httpFileUrl+i.pic' style="margin-top: 10px;float: left" />
                    <div style="float: left;margin-top:10px;margin-left: 10px;font-size: 17px">
                        <div style="position:absolute;width: 240px;">
                            <span style="word-break:break-all;">{{i.name}}</span>
                        </div>
                        <div style="position:relative;top: 45px;font-size: 13px">
                            <span>粉丝:{{i.likes===null? 0:i.likes}}</span>
                        </div>
                    </div>
                </div>
            </el-space>
        </div>
    </div>
</template>

<script>
    import {getPartSinger,getLikeSinger} from '@/ajax/getAndPost'
    export default {
        name: "",
        data(){
            return{
                songListPart:[{}],
                likeThisSinger:[],
                singerId:-1,
            }
        },
        methods:{
            change(i){
                let id=i.id
                this.$router.push({path:'/singer',query:{id}})
            },
            toConsumer(id){
                this.$router.push({path:'/consumer',query:{id}})
            },
            init(){
                getPartSinger().then(res=>{
                    if(res.success){
                        this.songListPart=res.data.slice(0,5);
                    }
                })
                getLikeSinger({'id':this.singerId}).then(res=>{
                    if(res.success){
                        this.likeThisSinger=res.data
                    }
                })
            }
        },
        created() {
            this.singerId=this.$route.query.id
            this.init()
        },
        watch: {
            $route(to, from) {
                if (to.query.id !== from.query.id) {
                    this.singerId=this.$route.query.id
                    this.init();
                }
            }
        },
    }
</script>

<style scoped>
    .likes{
        margin-top:20px;
        height: 300px;
    }
</style>