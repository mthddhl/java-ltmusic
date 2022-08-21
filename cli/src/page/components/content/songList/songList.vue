<template>
<div class="songList">
    <div class="type">
        <div>
            <el-space :fill="false" wrap :fill-ratio="10" :size="10" style="margin-left: 15px">
                <div class="firstLetter" v-for="i in firstLetter" :key="i" :style="letter===i? type:type1" @click="change(i)">
                    <span style="font-size: 15px">{{i}}</span>
                </div>
            </el-space>
        </div>
    </div>
    <el-divider />
    <div style="height: 1200px">
        <div v-if="!hasData">无数据</div>
        <el-space v-if="hasData" :fill="false" wrap :fill-ratio="50" :size="40" style="margin-left: 80px">
            <div v-for="i in songList" :key="i" class="box-card">
                <div class="box" :style="{ backgroundImage: `url(${this.$store.state.httpFileUrl+i.pic})` }"
                >
                    <div class="link" @click="jump(i.id)"></div>
                    <div class="content">
                        <el-row>
                            <el-col :span="4"><el-icon class="headset"><Headset/></el-icon></el-col>
                            <el-col :span="17" style="margin-top: 4px;margin-left: -5px"><span class="count">{{initNum(i.count)}}</span></el-col>
                            <el-col :span="3" style="margin-top: 8px"><el-icon class="playIcon" @click="songListPlay(i)"><VideoPlay /></el-icon></el-col>
                        </el-row>
                    </div>
                </div><div style="height:15px"></div>
                <div>
                    <span style="color: #000000">{{i.title}}</span>
                </div>
            </div>
        </el-space>
    </div>
    <br>
    <el-pagination v-if="hasData" layout="prev, pager, next"
                   :total="songListCount"
                   :page-size="pageSize"
                   @current-change="HanderCurrentPageComment"
                   v-model:current-page="currentPage"
                   style="margin-left: 45%; transform: scale(1.5);"

    />
    <br><br>
</div>
</template>

<script>
    import {songListGetByLetter,songListGetCountByLetter} from "@/ajax/getAndPost";
    import {ElMessage} from "element-plus";
    import {mixin} from '@/mixin/mixin'
    export default {
        name: "",
        mixins:[mixin],
        data(){
            return{
                hasData:false,
                firstLetter: this.$store.state.firstLetter,
                letter:'全部',
                type:{
                    'background-color':'rgba(0,0,0,0.08)',
                    'color':'rgba(16,255,0,0.97)',
                },
                type1:{
                    'color':'rgb(0,0,0)',
                },
                songList:[],
                songListCount:50,
                pageSize:20,
                currentPage:1,
            }
        },
        methods:{
            HanderCurrentPageComment(page){
                let params=new URLSearchParams();
                params.append("letter",this.letter);
                params.append("currentPage",page);
                params.append("pageSize",this.pageSize)
                songListGetByLetter(params).then(res=>{
                    if(res.success){
                        this.songList=res.data;
                    }else{
                        ElMessage.error(res.errorMsg)
                    }
                })
            },
            change(i){
                this.letter=i;
                songListGetCountByLetter({"letter":this.letter}).then(res=>{
                    if(res.success){
                        this.songListCount=res.data
                        if(res.data===0){
                            this.hasData=false;
                        }else {
                            this.hasData=true;
                        }
                    }else{
                        ElMessage.error(res.errorMsg)
                    }
                })
                this.HanderCurrentPageComment(1);
            },
            jump(id){
                this.$router.push({path:'/songList',query:{id}})
            },
            initNum(num){
                if(num>=10000){
                    return (num/10000).toFixed(2)+'w';
                }else {
                    return num;
                }
            }
        },
        created() {
            this.change("全部")
        }
    }
</script>

<style scoped>
.songList{
    height: 1000px;
    margin-top: 20px;
    /*color: #0840f6;*/
    /*width: 800px;*/
}
.type{
    text-align: left;
}
.content{

}
.firstLetter{
    width: 33px;
    height: 18px;
    text-align:center;
    cursor:pointer;
    line-height:18px
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
    margin-left:10px;
    margin-top: 7px;
    color: #2aa3ef
}
.count{
    font-size: 20px;
    margin-left:10px;
    color: #f8dcdc
}
.playIcon{
    transform: scale(1.8);
    /*right: -50px;*/
    color: #f60808;
    cursor:pointer;
}
</style>