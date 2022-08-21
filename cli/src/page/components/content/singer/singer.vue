<template>
<div class="singer">
    <div class="type">
        <div>
            <el-space :fill="false" wrap :fill-ratio="10" :size="10" style="margin-left: 15px">
                <div class="firstLetter" v-for="i in firstLetter" :key="i" :style="letter===i? type:type1" @click="change(i)">
                    <span style="font-size: 15px">{{i}}</span>
                </div>
            </el-space>
            <br><br>
            <el-space :fill="false" wrap :fill-ratio="10" :size="10" style="margin-left: 15px">
                <div class="firstLetter" v-for="i in singerSex" :key="i" :style="singerSexIndex===i? type:type1" @click="changeSex(i)">
                    <span style="font-size: 15px">{{i}}</span>
                </div>
            </el-space>
            <br><br>
            <el-space :fill="false" wrap :fill-ratio="10" :size="10" style="margin-left: 15px">
                <div class="firstLetter" v-for="i in singerLocation" :key="i" :style="singerLocationIndex===i? type:type1" @click="changeLocation(i)">
                    <span style="font-size: 15px">{{i}}</span>
                </div>
            </el-space>
            <el-divider />
            <div style="height: 1200px">
                <div v-if="!hasData" style="text-align: center">无数据</div>
                <el-space v-if="hasData" :fill="false" wrap :fill-ratio="50" :size="35" style="margin-left:80px">
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
        </div>
    </div>
    <el-pagination v-if="hasData" layout="prev, pager, next"
                   :total="singerCount"
                   :page-size="pageSize"
                   @current-change="HanderCurrentPageComment"
                   v-model:current-page="currentPage"
                   style="margin-left: 45%; transform: scale(1.5);"

    />
    <br><br><br><br>
</div>
</template>

<script>
    import {
        getPartSinger,
        songAllList,
        singerGetByLetter,
        singerGetCountByLetter,
    } from "@/ajax/getAndPost";
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
                singers:[],
                singerCount:50,
                pageSize:20,
                currentPage:1,
                singerSex:['全部','男','女','组合'],
                singerSexIndex:'全部',
                singerLocation:["全部",'内地','港澳台','国外'],
                singerLocationIndex:'全部',
            }
        },
        methods:{
            changeLocation(i){
              this.singerLocationIndex=i;
              this.changeInit();
            },
            changeSex(i){
                this.singerSexIndex=i;
                this.changeInit()
            },
            HanderCurrentPageComment(page){
                let params=new URLSearchParams();
                params.append("letter",this.letter);
                let sex=this.initSex()
                params.append('sex',sex)
                params.append("currentPage",page);
                params.append("pageSize",this.pageSize)
                params.append("location",this.singerLocationIndex)
                singerGetByLetter(params).then(res=>{
                    if(res.success){
                        this.singers=res.data;
                    }else{
                        ElMessage.error(res.errorMsg)
                    }
                })

            },
            change(i){
                this.letter=i
                this.changeInit()
            },
            changeInit(){
                let param=new URLSearchParams();
                param.append("letter",this.letter)
                let sex=this.initSex()
                param.append("sex",sex)
                param.append('location',this.singerLocationIndex)
                singerGetCountByLetter(param).then(res=>{
                    if(res.success){
                        this.singerCount=res.data
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
            initSex(){
                if(this.singerSexIndex==='全部'){
                    return 3;
                }else if(this.singerSexIndex==='男'){
                    return 1;
                }else if(this.singerSexIndex==='女'){
                    return 0;
                }else {
                    return 2;
                }
            },
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
            this.change("全部")
        }
    }
</script>

<style scoped>
.singer{
    margin-top: 20px;
}
.type{
    text-align: left;
}
.firstLetter{
    width: 50px;
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