<template>
    <div class="space">
        <br>
        <div style="margin-left: 30px">
            <el-button @click="playAll">播放全部</el-button>
        </div>

        <el-table :data="tableData"
                  style="width: 100%;margin-top: 20px;">
            <el-table-column width="50" align="center" type="selection" @selection-change="handleSelectChange"/>
            <el-table-column width="38">
                <template #default="scope">
                    <div style="cursor: pointer">
                        <el-icon v-if="isPlay.status===3 && isPlay.name===scope.row.name" @click="closeSong" class="iconPlay"><VideoPause /></el-icon>
                        <el-icon v-else @click="addAlong(scope.row)" class="iconPlay"><VideoPlay /></el-icon>
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="name" label="歌曲姓名" width="250" />
            <el-table-column width="200" label="时长">
                <template #default="scope">
                    <div @mouseover="showDetails=scope.row.name" @mouseleave="showDetails=''">
                        <div v-if="showDetails!=scope.row.name">
                            <span>{{ scope.row.songTime }}</span>
                        </div>
                        <div v-else-if="showDetails===scope.row.name">
                            <el-tooltip
                                    class="box-item"
                                    effect="dark"
                                    content="添加到播放列表"
                                    placement="top"
                            >
                                <el-icon class="icon" @click="addSong(scope.row)"><Plus /></el-icon>
                            </el-tooltip>
                            <el-tooltip
                                    class="box-item"
                                    effect="dark"
                                    content="收藏"
                                    placement="top"
                            >
                                <span v-if="scope.row.liked" @click="likeSong(scope)">
                                    <IconHeartFill class="icon" :style="{ color: '#f53f3f' }"  />
                                </span>
                                <span v-else @click="likeSong(scope)">
                                    <IconHeart  class="icon" />
                                </span>

                                <!--                                <el-icon class="icon" @click="likes(scope.row.id)" style="background-color: #36f800"><FolderAdd /></el-icon>-->
                            </el-tooltip>
                            <el-tooltip
                                    class="box-item"
                                    effect="dark"
                                    content="下载"
                                    placement="top"
                            >
                                <el-icon class="icon" @click="downloadSong(scope.row)"><Download /></el-icon>
                            </el-tooltip>
                            <el-tooltip
                                    class="box-item"
                                    effect="dark"
                                    content="添加到歌单"
                                    placement="top"
                            >

                                <!--                                <el-icon class="icon"><Download /></el-icon>-->
                                <el-icon class="icon" @click="addToSongList(scope.row)"><FirstAidKit /></el-icon>
                            </el-tooltip>

                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column>
                <template #default="scope">
                    <div style="cursor: pointer" @click="toSinger(scope)">
                        <span>{{scope.row.singerName}}</span>
                    </div>
                </template>
            </el-table-column>
            <el-table-column>
                <template #default="scope">
                    <!--                    <span v-if="scope.row.isDownLoad">{{scope.row.isDownLoadNum}}%</span>-->
                    <el-progress v-if="scope.row.isDownLoad" :percentage="scope.row.isDownLoadNum" />
                </template>
            </el-table-column>
<!--            <el-table-column prop="singerId" label="歌手姓名" />-->
        </el-table>
        <el-dialog
                v-model="isAddSongList"
                title="Tips"
                width="50%"
        >
            <el-table :data="songListName" style="width: 100%" @selection-change="handleSelectChange1">
                <el-table-column prop="title" label="歌单标题" width="330" />
                <el-table-column width="50" align="center" type="selection">
                </el-table-column>
            </el-table>
            <template #footer>
      <span class="dialog-footer">
        <el-button @click="isAddSongList = false">取消</el-button>
        <el-button type="primary" @click="addSubmit"
        >确定</el-button
        >
      </span>
            </template>
        </el-dialog>

    </div>
</template>

<script>
    import {
        consumerAddSongGetSongList,
        consumerAddSongToSongList,
        consumerLikeSong,
        fileIsExist,
        getConsumerLikeSong
    } from '@/ajax/getAndPost'
    import {ElMessage} from "element-plus";
    import axios from "axios";
    import fileDownload from "js-file-download";
    import {mixin} from '@/mixin/mixin'
    export default {
        name: "",
        mixins:[mixin],
        data(){
            return{
                isPerson:false,
                showDetails:'',
                isPlay:{
                    status:-1,
                    name:'',
                    song:{}
                },
                tableData:[
                ],
                consumerId:-1,
                currentPage:1,
                pageSize:2,
                isAddSongList:false,
                addSongId:-1,
                songListName:[
                ],
                addSongListIds:[],

            }
        },
        methods:{
            playAll(){
                let songs=[];
                this.tableData.forEach(each=>{
                    if(!each.isVip || this.$store.state.isVip) {
                        songs.push({
                            name: each.name,
                            artist: each.singerName,
                            url: this.$store.state.httpFileUrl + each.url,
                            cover: this.$store.state.httpFileUrl+each.pic,
                            lrc:each.lyric
                        })
                    }
                })
                let r={
                    status:4,
                    song:songs,
                }
                this.$store.state.songList=r;
            },
            toSinger(scope){
                let id=scope.row.singerId;
                this.$router.push({path:'/singer',query:{id}})
            },
            handleSelectChange(){

            },
            handleSelectChange1(val){
                this.addSongListIds=[]
                val.map(a=>{
                    this.addSongListIds.push(a.id)
                })
            },
            addToSongList(row){
                this.addSongId=row.id;
                if(localStorage.getItem("token")!==null &&localStorage.getItem("token")!==''){
                    if(localStorage.getItem("consumer")!==null && localStorage.getItem("consumer")!==''){
                        let consumer=JSON.parse(localStorage.getItem("consumer"));
                        this.consumerId=consumer.id;
                    }else {
                        ElMessage.error('未登录，无法添加到歌单')
                        return
                    }
                }else {
                    ElMessage.error('未登录，无法添加到歌单')
                    return;
                }
                let param=new URLSearchParams()
                param.append("consumerId",this.consumerId)
                param.append("songId",this.addSongId);
                consumerAddSongGetSongList(param).then(res=>{
                    this.songListName=res.data;
                })
                this.isAddSongList=true
            },


            addSubmit(){
                if(this.addSongListIds.length===0){
                    ElMessage.error("未选择歌单")
                    return
                }
                let params=new URLSearchParams();
                params.append("songId",this.addSongId)
                params.append("songListIds",this.addSongListIds)
                consumerAddSongToSongList(params).then(res=>{
                    if(res.success){
                        ElMessage.success(res.data)
                    }else {
                        ElMessage.error(res.errorMsg)
                    }
                })
                this.addSongListIds=[]
                this.isAddSongList=false
            },


            likeSong(scope){
                if(localStorage.getItem("token")===null  || localStorage.getItem("token")===''){
                    if(localStorage.getItem("consumer")===null || localStorage.getItem("consumer")===''){
                        ElMessage.error("您还未登录，无法收藏")
                        return;
                    }
                }
                let param=new URLSearchParams();
                let songId=scope.row.id
                param.append("songId",songId)
                param.append("isLiked",scope.row.liked)
                consumerLikeSong(param).then(res=>{
                    if(res.success){
                        if(this.isPerson){
                            if(scope.row.liked){
                                this.tableData.splice(scope.$index,1)
                            }
                        }
                        scope.row.liked=!scope.row.liked
                        ElMessage.success(res.data)
                    }else {
                        // console.log(res)
                        ElMessage.error(res.errorMsg)
                    }
                })
            },
            init(){
                if(localStorage.getItem("token")!==null &&localStorage.getItem("token")!==''){
                    if(localStorage.getItem("consumer")!==null && localStorage.getItem("consumer")!==''){
                        let consumer=JSON.parse(localStorage.getItem("consumer"));
                        if(this.consumerId+""===consumer.id+""){
                            this.isPerson=true;
                        }
                    }
                }
                getConsumerLikeSong({"id":this.consumerId}).then(res=>{
                    if(res.success){
                        this.tableData=res.data;
                    }
                })
            }
        },
        watch: {
            '$store.state.songList':{
                handler(newNum,oldNum){
                    this.isPlay=newNum;
                }
            },
            $route(to, from) {
                if (to.query.id !== from.query.id) {
                    this.consumerId = to.query.id;
                    this.init();
                }
            },
        },
        created() {
            this.consumerId=this.$route.query.id
            this.init()

        }
    }
</script>

<style scoped>
    .space{
        width: 90%;
        margin:auto;
    }
    .box-item{
        transform: scale(2);
    }
    .icon{
        transform: scale(1.5);
        margin-left: 10px;
        cursor:pointer;
    }
    .iconPlay{
        transform: scale(1.5);
        top: 3px;
        left: -2px;
    }
</style>