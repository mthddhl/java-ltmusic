<template>
    <div class="space">

        <div style="margin-left: 10px;margin-top: 10px">
            <span class="allLike" :style="this.currentState==='全部'? type:''" @click="getAll">全部<el-icon><CaretBottom /></el-icon></span>
            <span class="togetLike" :style="this.currentState==='共同关注'? type:''" v-if="loginAndP" @click="togetLike">共同关注<el-icon><CaretBottom /></el-icon></span>
        </div>
        <el-divider style="margin-top: 1px;margin-bottom: 15px; width:95%;margin-left: -5px;background-color: #51626e"/>

        <el-space :fill="false" wrap :fill-ratio="50" :size="40">
            <div v-for="(i,index) in singers" :key="i" class="box-card">
                <div class="box" :style="{ backgroundImage: `url(${this.$store.state.httpFileUrl+i.pic})` }">
                    <div class="link" @click="jump(i.id)"></div>
                    <div class="content">
                        <el-row>
                            <el-col :span="4"> <el-icon class="headset"><Star /></el-icon></el-col>
                            <el-col :span="17" style="margin-top: 4px;margin-left: -5px"><span class="count">{{initNum(i.likes)}}</span></el-col>
                            <el-col :span="3" style="margin-top: 4px">
                                <el-dropdown trigger="click">
                                    <el-icon class="playIcon"><MoreFilled /></el-icon>
                                    <template #dropdown>
                                        <el-dropdown-menu>
                                            <el-dropdown-item @click="singerPlay(i.id)">播放</el-dropdown-item>
                                            <el-dropdown-item @click="deleteLike(i,index)" v-if="this.isPerson">取消关注</el-dropdown-item>
                                        </el-dropdown-menu>
                                    </template>
                                </el-dropdown>
                            </el-col>
                        </el-row>
                    </div>
                </div><div style="height:15px"></div>
                <div>
                    <span style="color: #000000">{{i.name}}</span>
                </div>
            </div>

        </el-space>
    </div>

</template>

<script>
    import {consumerDeleteLikeSinger, consumerGetLikeSinger, getTogetLikedSingers, songAllList} from '@/ajax/getAndPost'
    import {ElMessage} from "element-plus";
    import {mixin} from '@/mixin/mixin'
    export default {
        name: "",
        mixins:[mixin],
        data(){
            return{
                singers:[],
                consumerId:-1,
                isPerson:false,
                loginAndP:false,
                currentState:"全部",
                type:{
                    'color':'rgba(255,0,0,0.97)',
                },

            }
        },
        methods:{
            jump(id){
                this.$router.push({path:'/singer',query:{id}})
            },
            initNum(num){
                if(num===null) {
                    return 0
                }
                if(num>=10000){
                    return (num/10000).toFixed(2)+'w';
                }else {
                    return num;
                }
            },
            deleteLike(i,index){
                let id=i.id
                consumerDeleteLikeSinger({"id":id}).then(res=>{
                    if(res.success){
                        ElMessage.success("取消关注成功")
                        this.singers.splice(index,1)
                    }else{
                        ElMessage.error(res.data)
                    }
                })
            },
            getAll(){
                this.currentState='全部'
                consumerGetLikeSinger({"id":this.consumerId}).then(res=>{
                    if(res.success){
                        this.singers=res.data
                    }
                })
            },
            togetLike(){
                this.currentState='共同关注'
                getTogetLikedSingers({"id":this.consumerId}).then(res=>{
                    if(res.success){
                        this.singers=res.data
                    }
                })
            }
        },
        created() {
            this.consumerId=this.$route.query.id;

            if(localStorage.getItem("token")!==null &&localStorage.getItem("token")!==''){
                if(localStorage.getItem("consumer")!==null && localStorage.getItem("consumer")!==''){
                    let consumer=JSON.parse(localStorage.getItem("consumer"));
                    if(this.consumerId+""===consumer.id+""){
                        this.isPerson=true;
                    }
                }
            }
            if(localStorage.getItem("token")!==null &&localStorage.getItem("token")!==''){
                if(localStorage.getItem("consumer")!==null && localStorage.getItem("consumer")!==''){
                    let consumer=JSON.parse(localStorage.getItem("consumer"));
                    if(this.consumerId+""!==consumer.id+""){
                        this.loginAndP=true;
                    }
                }
            }
            consumerGetLikeSinger({"id":this.consumerId}).then(res=>{
                if(res.success){
                    this.singers=res.data
                }
            })
            // getPartSinger().then(res=>{
            //     if(res.success){
            //         this.singers=res.data;
            //     }
            // })
        }
    }
</script>

<style scoped>
    .space{
        margin-top:20px;
        margin-left: 60px;
    }
    .box{
        width:175px;
        height: 200px;
        background-size: cover;
        transition: all 0.2s;
        text-align: left;
        border-radius:6px;
        box-shadow: 1px 1px;
    }
    .box-card{
        width:175px;
        height: 250px;
    }
    .box-card:hover{
        transform: scale(1.1,1.1);
    }
    .content{
        height: 30px;
        /*margin-top:140px;*/
        /*bottom:0;*/
        width:175px;
        background-color: rgba(33, 37, 38, 0.94);
    }
    .link{
        height:170px;
        width:175px;
        cursor:pointer;
    }
    .headset{
        transform: scale(1.5);
        margin-left:3px;
        margin-top: 7px;
        color: #2aa3ef
    }
    .count{
        font-size: 20px;
        /*margin-left:10px;*/
        color: #f8dcdc
    }
    .playIcon{
        margin-top: 4px;
        transform: scale(1.8);
        /*right: -50px;*/
        color: #f60808;
        cursor:pointer;
    }
    .allLike{
        font-size: 14px;
        cursor:pointer;
    }
    .togetLike{
        font-size: 14px;
        margin-left: 40px;
        cursor:pointer;
    }
</style>