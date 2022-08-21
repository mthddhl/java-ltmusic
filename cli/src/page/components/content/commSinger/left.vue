<template>
<div>
    <div class="information">
        <div class="img" :style="{ backgroundImage: `url(${this.$store.state.httpFileUrl+singer.pic})` }">
        </div>
        <div class="content">
            <div style="margin-left: 10px"><span style="font-family:微软雅黑;font-size: 22px">{{singer.name}}</span></div>
            <div style="margin-top:20px;">
<!--                <img src="../../../../img/va.jpg" alt="" class="imgIV">-->
                <div class="created">
                    <el-icon style="transform:scale(1.5)"><Location /></el-icon>
                    <span style="margin-left: 10px;">{{singer.location}}</span>
                    <span style="margin-left: 30px"> {{singer.birth}}</span>
                </div>
            </div>
            <div style="margin-top: 25px;margin-left: 10px">
                <el-button @click="songListPlay">
                    <el-icon class="icon"><VideoPlay /></el-icon>播放
                </el-button>
                <el-button @click="singerIsLiked" :type="isLike">
                    <el-icon class="icon"><Star /></el-icon>{{singer.likes===null? 0:singer.likes}}
                </el-button>
<!--                <el-button>-->
<!--                    <el-icon class="icon"><Download /></el-icon>下载-->
<!--                </el-button>-->
                <el-button @click="enjoy">
                    <el-icon class="icon"><Share /></el-icon>分享
                </el-button>
            </div>
            <div style="margin-top: 20px;margin-left: 10px;color: black">
                <el-space :fill="false" wrap :fill-ratio="50" :size="1" style="font-size: 15px">
                    性别:  {{sex}}
                </el-space>
            </div>
            <div style="margin-top: 20px;margin-left: 10px;margin-right: 10px">
                介绍：<span style="word-break:break-all;">{{singer.introduction}}</span>
            </div>
        </div>
    </div>



    <div class="songs" style="text-align:left;">
        <div class="songHead">
            <span style="margin-left: 10px;font-size: 25px">歌曲列表</span>
            <span style="margin-left: 20px;font-size: 13px">{{songsTotal}}首歌</span>
        </div>
        <el-divider style="margin-top: 1px; width:95%;margin-left: 10px" />
        <el-table :data="songs" style="width: 100%">
            <el-table-column width="50" align="center" type="selection" />
            <el-table-column width="38">
                <template #default="scope">
                    <div style="cursor: pointer">
                        <el-icon v-if="isPlay.status===3 && isPlay.name===scope.row.name" @click="closeSong" class="iconPlay"><VideoPause /></el-icon>
                        <el-icon v-else @click="addAlong(scope.row)" class="iconPlay"><VideoPlay /></el-icon>
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="name" label="歌曲名" width="150" />
            <el-table-column width="200" label="时长">
                <template #default="scope">
                    <div @mouseover="showDetails=scope.row.id" @mouseleave="showDetails=-1">
                        <div v-if="showDetails!=scope.row.id">
                            <span>{{ scope.row.songTime }}</span>
                        </div>
                        <div v-else-if="showDetails===scope.row.id">
                            <el-tooltip
                                    class="box-item"
                                    effect="dark"
                                    content="添加到播放列表"
                                    placement="top"
                            >
                                <el-icon class="icon1" @click="addSong(scope.row)"><Plus /></el-icon>
                            </el-tooltip>
                            <el-tooltip
                                    class="box-item"
                                    effect="dark"
                                    content="收藏"
                                    placement="top"
                            >
                                <span v-if="scope.row.liked" @click="likeSong(scope)">
                                    <IconHeartFill class="icon1" :style="{ color: '#f53f3f' }"  />
                                </span>
                                <span v-else @click="likeSong(scope)">
                                    <IconHeart  class="icon1" />
                                </span>
<!--                                <el-icon class="icon1" @click="likes(scope.row.id)"><FolderAdd /></el-icon>-->
                            </el-tooltip>
                            <el-tooltip
                                    class="box-item"
                                    effect="dark"
                                    content="下载"
                                    placement="top"
                            >
                                <el-icon class="icon1" @click="downloadSong(scope.row)"><Download /></el-icon>
                            </el-tooltip>
                            <el-tooltip
                                    class="box-item"
                                    effect="dark"
                                    content="添加到歌单"
                                    placement="top"
                            >

                                <!--                                <el-icon class="icon"><Download /></el-icon>-->
                                <el-icon class="icon1" @click="addToSongList(scope.row)"><FirstAidKit /></el-icon>
                            </el-tooltip>

                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="歌手姓名" >{{singer.name}}</el-table-column>
            <el-table-column>
                <template #default="scope">
                    <!--                    <span v-if="scope.row.isDownLoad">{{scope.row.isDownLoadNum}}%</span>-->
                    <el-progress v-if="scope.row.isDownLoad" :percentage="scope.row.isDownLoadNum" />
                </template>
            </el-table-column>
        </el-table>
        <el-pagination layout="prev, pager, next"
                       :total="songsTotal"
                       :page-size="pageSize"
                       @current-change="HanderCurrentPage"
                       v-model:current-page="songsCurrentPage"
                       style="margin-left:auto;margin-right:auto"

        />
    </div>
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
        getSingerById,
        getSongPage,
        getAllList,
        consumerLikeSinger, consumerLikeSong, fileIsExist, consumerAddSongToSongList, consumerAddSongGetSongList
    } from '@/ajax/getAndPost'
    import {ElMessage} from "element-plus"
    import axios from "axios";
    import fileDownload from "js-file-download";
    import {mixin} from "@/mixin/mixin";
    export default {
        name: "",
        mixins:[mixin],
        data(){
            return{
                singerId:-1,
                isLike:'default',
                singer:{
                    pic:'',
                    name:'歌手名',
                    location:"歌手籍贯",
                    birth:'歌手生日',
                    likes:20,
                    sex:1,
                    introduction: '歌手介绍',
                    count:100,
                },
                songsTotal:0,
                pageSize:5,
                songsCurrentPage:1,
                songs:[],
                isPlay: {
                    status: -1,
                    name: '',
                    song: {}
                },
                showDetails:-1,
                songAll:[],
                isAddSongList:false,
                addSongId:-1,
                songListName:[
                ],
                addSongListIds:[],
                consumerId:-1,

            }
        },
        methods:{
            toSinger(scope){
                let id=scope.row.singerId;
                this.$router.push({path:'/singer',query:{id}})
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
                    this.songListName=res.data
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
                        scope.row.liked=!scope.row.liked
                        ElMessage.success(res.data)
                    }else {
                        ElMessage.error(res.data.errorMsg)
                    }
                })
            },
            songListPlay(){
                let isSongPlay=[]
                if(this.songAll.length===0){
                    ElMessage.error('歌手无歌曲')
                    return
                }
                this.songAll.forEach(each=>{
                    if(!each.isVip || this.$store.state.isVip) {
                        isSongPlay.push({
                            name: each.name,
                            artist: each.singerName,
                            url: this.$store.state.httpFileUrl + each.url,
                            cover: this.$store.state.httpFileUrl + each.pic,
                            lrc:each.lyric
                        })
                    }
                })
                let r={
                    status:1,
                    song:isSongPlay,
                }
                this.$store.state.songList=r;
            },
            singerIsLiked(){
                if(localStorage.getItem("token")===null || localStorage.getItem("token")===''){
                    ElMessage.success("您还未登录，不能关注")
                    return;
                }
                if(this.isLike==='success'){
                    ElMessage.success("您已关注此歌手，请勿重复操作")
                    return;
                }
                consumerLikeSinger({"id":this.singerId}).then(result=>{
                    if(result.success){
                        ElMessage.success("关注成功")
                        this.isLike="success"
                        this.singer.likes+=1;
                    }else{
                        ElMessage.error(result.errorMsg)
                    }
                })
            },
            enjoy(){
                let cInput = document.createElement("input");
                cInput.value = window.location.href;
                document.body.appendChild(cInput)
                cInput.select();
                document.execCommand("copy");
                ElMessage.success("链接已复制到粘贴板，快去分享吧")
                document.body.removeChild(cInput);
            },
            HanderCurrentPage(currentPage){
                let params=new URLSearchParams;
                params.append('currentPage', currentPage)
                params.append('pageSize', this.pageSize);
                params.append('singerId',this.singerId);
                getSongPage(params).then(res=>{
                    if(res.success){
                        this.songs=res.data;
                    }
                })
            },
            // likes(id) {
            //     ElMessage.success('收藏成功' + id)
            // },
            init(){
                getSingerById({"id":this.singerId}).then(res=>{
                    if(res.success){
                        this.singer=res.data;
                        this.isLike=res.data.isLike
                        this.singer.pic=res.data.pic
                    }
                })
                getAllList({'singerId':this.singerId}).then(res=>{
                    if(res.success){
                        this.songsTotal=res.data.length;
                        this.songAll=res.data
                    }
                })
                this.HanderCurrentPage(1);
            }
        },
        computed:{
            sex(){
                if(this.singer.sex===1) return '男'
                else if(this.singer.sex===0) return '女'
                else if(this.singer.sex===2) return '组合乐队'
                else return  '未知'
            }
        },
        watch: {
            '$store.state.songList': {
                handler(newNum, oldNum) {
                    this.isPlay = newNum;
                }
            },
            $route(to, from) {
                if (to.query.id !== from.query.id) {
                    this.singerId = this.$route.query.id
                    this.init()
                }
            }
        },
        created() {

            this.singerId=this.$route.query.id
            this.init()
        }
    }
</script>

<style scoped>
    .information{
        margin-top: 40px;
        height: 350px;
    }
    .img{
        float:left;
        height: 220px;
        width: 220px;
        margin-left:40px;
        background-color: #003b60;
        background-size: cover;
        border:1px solid #000000;
    }
    .content{
        text-align:left;
        float:right;
        width: 450px;
        height: 280px;
        margin-right:50px;
        /*background-color: #7bafcc*/
    }
    .created{
        /*margin-left: 5px;*/
        /*margin-top: 1px;*/
        font-family:微软雅黑;
        font-size: 15px;
        color: #000000;
        margin-left: 10px;
        /*margin-right: 190px*/

    }
    .icon{
        transform: scale(1.5);
        right: 7px;
        bottom: 1px;
    }
    .box-item{
        transform: scale(2);
    }
    .icon1{
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