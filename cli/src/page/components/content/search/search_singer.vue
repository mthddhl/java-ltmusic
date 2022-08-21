<template>
    <div class="space">
        <span v-if="singers.length===0">无数据</span>
        <el-space v-else :fill="false" wrap :fill-ratio="50" :size="40">
            <div v-for="i in singers" :key="i" class="box-card">
                <div class="box" :style="{ backgroundImage: `url(${this.$store.state.httpFileUrl+i.pic})` }">
                    <div class="link" @click="jump(i.id)"></div>
                    <div class="content">
                        <el-row>
                            <el-col :span="4"> <el-icon class="headset"><Star /></el-icon></el-col>
                            <el-col :span="17" style="margin-top: 4px;margin-left: -5px"><span class="count">{{initNum(i.likes)}}</span></el-col>
                            <el-col :span="3"><el-icon class="playIcon" @click="singerPlay(i.id)"><VideoPlay /></el-icon></el-col>
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
    import {mixin} from '@/mixin/mixin'
    export default {
        name: "",
        mixins:[mixin],
        data(){
            return{

            }
        },
        props:{
            text:{
                type: String,
                default: ""
            },
            singers:{
                type:Array,
                default(){
                    return []
                }
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
        },
        created() {

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
        margin-top: 8px;
        transform: scale(1.8);
        /*right: -50px;*/
        color: #f60808;
        cursor:pointer;
    }

</style>