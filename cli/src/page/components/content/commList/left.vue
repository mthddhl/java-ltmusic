<template>
<div>
    <div class="information">
        <div class="img" :style="{ backgroundImage: `url(${this.$store.state.httpFileUrl+songList.pic})` }">
        </div>
        <div class="content">
            <div style="margin-left: 10px"><span style="font-family:微软雅黑;font-size: 22px">{{songList.title}}</span></div>
            <div style="margin-top:20px;">
                <img :src="this.$store.state.httpFileUrl+songList.createdAvator" alt="" class="imgIV" style="cursor: pointer" @click="toConsumer(songList.createdConId)">
                <div class="created">
                    <span style="cursor: pointer" @click="toConsumer(songList.createdConId)">{{songList.createdName}}</span>
                    <span style="margin-left: 30px"> {{songList.createdTime}}</span>
                </div>
            </div>
            <div style="margin-top: 25px;margin-left: 10px">
                <el-button @click="songListPlay">
                    <el-icon class="icon"><VideoPlay /></el-icon>播放
                </el-button>
                <el-button @click="songListIsLiked" :type="isLike">
                    <el-icon class="icon"><FolderAdd /></el-icon>{{songList.likes}}
                </el-button>
<!--                <el-button>-->
<!--                    <el-icon class="icon"><Download /></el-icon>下载-->
<!--                </el-button>-->
                <el-button @click="enjoy">
                    <el-icon class="icon"><Share /></el-icon>  分享
                </el-button>
                <el-button @click="scroller">
                    <el-icon class="icon"><ChatSquare /></el-icon>{{commentCount}}
                </el-button>
            </div>
            <div style="margin-top: 20px;margin-left: 10px;color: black">
                <el-space :fill="false" wrap :fill-ratio="50" :size="1" style="font-size: 15px">
                    标签:
                    <el-button  class="box-card" text>{{songList.style}}</el-button>
                </el-space>
            </div>
            <div style="margin-top: 20px;margin-left: 10px;margin-right: 10px">
                介绍：<span style="word-break:break-all;">{{songList.introduction}}</span>
            </div>
        </div>
    </div>

    <div class="songs" style="text-align:left;">
        <div class="songHead">
            <span style="margin-left: 10px;font-size: 25px">歌曲列表</span>
            <span style="margin-left: 20px;font-size: 13px">{{songsTotal}}首歌</span>
            <span style="margin-left: 500px;font-size: 13px">播放{{songList.count}}次</span>
        </div>
        <el-divider style="margin-top: 1px; width:95%;margin-left: 10px" />

        <el-table :data="songs" style="width: 100%">
            <el-table-column width="50" align="center" type="selection" @selection-change="handleSelectChange"/>
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
                                <span v-if="!scope.row.liked" @click="likeSong(scope)">
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
        <el-pagination layout="prev, pager, next"
                       :total="songsTotal"
                       :page-size="pageSize"
                       @current-change="HanderCurrentPage"
                       v-model:current-page="songsCurrentPage"
                       style="margin-left:auto;margin-right:auto"

        />
    </div>
    <br><br><br>
    <div id="comment" style="text-align:left;">
        <div class="songHead">
            <span style="margin-left: 10px;font-size: 25px">评论</span>
            <span style="margin-left: 20px;font-size: 13px">共{{commentCount}}条评论</span>
        </div>
        <el-divider style="margin-top: 1px; width:95%;margin-left: 10px;background-color: #2aa3ef"/>
        <el-avatar :size="50" :src="consumer.consumer.avator" style="margin-left: 10px" />
        <el-input v-model="comment" type="textarea" style="width:80%;margin-left:25px;"
                  :autosize="{ minRows: 2, maxRows: 4 }"
                  placeholder="评论。。。。。"
                  @blur="blurEvent"
        ></el-input>
        <el-button style="float:left;margin-top:7px;margin-left: 90px;width: 50px" @click="isShowEmoji=!isShowEmoji">
            表情<el-icon><ArrowDown /></el-icon>
        </el-button>
        <div style="margin-top:13px;margin-left: 550px">
            <span style="color:#80111a">字数：{{comment.length}}</span>
        </div>
        <el-button type="primary" style="margin-top:-49px;margin-left: 640px" @click="submitComment">提交评论</el-button>
        <vuemoji-picker class="email" @emojiClick="handleEmojiClick" v-show="isShowEmoji"
        ></vuemoji-picker>
        <div>
            <div style="margin-left: 50px;margin-top: 10px">
                <span :style="this.type==='hot'? this.hotOrTime1:this.hotOrTime" @click="changeCommentShowByHot">按热度<el-icon><CaretBottom /></el-icon></span>&emsp;&emsp;&emsp;
                <span :style="this.type==='time'? this.hotOrTime1:this.hotOrTime" @click="changeCommentShowByTime">按时间<el-icon><CaretBottom /></el-icon></span>
            </div>
            <el-divider style="margin-top: 1px;margin-bottom: 15px; width:88%;margin-left: 50px;background-color: #51626e"/>
            <div>

                <a-comment v-for="i in commentList" :key="i" :author="i.consumerId" :datetime="initTime(i.createdTime)" align="right" style="width: 86%;margin-left: 45px">
                    <template #actions>
                        <span class="action" key="heart" @click="onLikeChange(i)">
                            <span v-if="i.liked">
                                 <IconHeartFill :style="{ color: '#f53f3f' }" />
                             </span>
                            <span v-else>
                              <IconHeart />
                            </span>
                           {{ i.likes }}
                        </span>
                        <span class="action" key="reply" v-if="this.consumerId===i.consumerId || this.consumerId===songList.createdConId" @click="deleteComment(i.id)">
                            <icon-delete />delete
                        </span>
                        <span v-else>
                            <icon-stop  />delete
                        </span>
                    </template>
                    <template #avatar>
                        <a-avatar>
                            <img
                                    alt="avatar"
                                    :src="this.$store.state.httpFileUrl+i.avator"
                            />
                        </a-avatar>
                    </template>
                    <template #content>
                        <div>
                            {{i.content}}
                        </div>
                    </template>
                    <el-divider style="margin-top: -15px;margin-bottom: 0px; width:110%;margin-left: -50px;background-color: rgba(81,98,110,0.09)"/>
                </a-comment>
            </div>
            <el-pagination layout="prev, pager, next"
                           :total="commentCount"
                           :page-size="pageSize"
                           @current-change="HanderCurrentPageComment"
                           v-model:current-page="commentCurrentPage"
                           style="margin-left: 200px"

            />
        </div>
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

    <br><br>
</div>
</template>

<script>
    import {ElMessage} from "element-plus";
    import { VuemojiPicker, EmojiClickEventDetail } from 'vuemoji-picker'
    import {mixin} from '@/mixin/mixin'
    import {
        songListGetById,
        getSongBySongListIdPage,
        subComments,
        getCommentsBySongListId,
        songListGetSongsCountIncr,
        consumerLikeSongList,
        consumerLikeComment,
        songforSongListGetAll,
        getCommentCount,
        consumerLikeSong,
        getLikeSongList,
        fileIsExist,
        consumerAddSongToSongList,
        consumerAddSongGetSongList, consumerDeleteComment
    } from '@/ajax/getAndPost'
    import axios from "axios";
    import fileDownload from "js-file-download";
    export default {
        name: "",
        mixins:[mixin],
        data() {
            return {
                isDelete:true,
                isLike:"default",
                isSongListPlay:false,
                consumer:{
                    login:false,
                    consumer:{
                        id:null,
                        avator:'../../../../img/va.jpg'
                    },
                },
                likeComment:false,
                start:false,
                songListId:-1,
                songList:{},
                isShowEmoji: false,
                comment: '',
                labelType: ['标签111'],
                isPlay: {
                    status: -1,
                    name: '',
                    song: {}
                },
                commentList:[
                    {
                        liked:false,
                        avator:'',
                    }
                ],
                commentCount:0,
                songs: [],
                songsAll:[],
                showDetails:-1,
                songsCurrentPage: 1,
                songsTotal: 500,
                blurIndex: null,
                pageSize:5,
                commentCurrentPage:1,
                isAddSongList:false,
                addSongId:-1,
                songListName:[
                ],
                addSongListIds:[],
                consumerId:-1,
                consumerIsVip:false,
                hotOrTime:{
                    'font-size': '14px',
                    'cursor':'pointer',
                    'color':'#000000'
                },
                hotOrTime1:{
                    'font-size': '14px',
                    'cursor':'pointer',
                    'color':'#ff0000'
                },
                type:"hot"
            }
        },
        components: {
            VuemojiPicker
        },
        methods: {
            deleteComment(id){
                console.log(id)
                let params=new URLSearchParams();
                params.append("commentId",id)
                params.append("songListId",this.songListId)
                consumerDeleteComment(params).then(res=>{
                    if(res.success){
                        ElMessage.success(res.data)
                        this.HanderCurrentPageComment(1)
                    }else {
                        ElMessage.error(res.errorMsg);
                    }
                })
            },
            changeCommentShowByHot(){
                this.type="hot"
                this.HanderCurrentPageComment(1)
            },
            changeCommentShowByTime(){
                this.type="time"
                this.HanderCurrentPageComment(1)
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
            handleSelectChange1(val){
                this.addSongListIds=[]
                val.map(a=>{
                    this.addSongListIds.push(a.id)
                })
            },


            songListPlay(){
                let isSongPlay=[]
                if(this.songsAll.length===0){
                    ElMessage.error('歌单无歌曲')
                    return
                }
                this.songsAll.forEach(each=>{
                    if(!each.isVip || this.consumerIsVip) {
                        isSongPlay.push({
                            name: each.name,
                            artist: each.singer,
                            url: this.$store.state.httpFileUrl + each.url,
                            cover:this.$store.state.httpFileUrl +  each.pic,
                            lrc:each.lyric
                        })
                    }
                })

                let r={
                    status:1,
                    song:isSongPlay,
                }
                this.$store.state.songList=r;
                if(!this.isSongListPlay){
                    songListGetSongsCountIncr({"id":this.songListId}).then(res=>{
                        if(res.success){
                            this.songList.count+=1;
                            this.isSongListPlay=true;
                        }
                    })
                }

            },
            addToSongList(row){
                this.addSongId=row.id;
                if(this.consumerId<1){
                    ElMessage.error("未登录，无法收藏")
                }
                let param=new URLSearchParams()
                param.append("consumerId",this.consumerId)
                param.append("songId",this.addSongId);
                consumerAddSongGetSongList(param).then(res=>{
                    this.songListName=res.data
                })
                this.isAddSongList=true
            },
            toConsumer(id){
                this.$router.push({path:'/consumer',query:{id}})
            },
            toSinger(scope){
                let id=scope.row.singerId;
                this.$router.push({path:'/singer',query:{id}})
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
                        ElMessage.error(res.errorMsg)
                    }
                })
            },
            enjoy(){
                // console.log(123)
                let cInput = document.createElement("input");
                cInput.value = window.location.href;
                document.body.appendChild(cInput);
                cInput.select();
                document.execCommand("copy");
                ElMessage.success("链接已复制到粘贴板，快去分享吧")
                document.body.removeChild(cInput);
            },
            scroller(){
                setTimeout(()=>{
                    let target=document.getElementById("comment");
                    target.scrollIntoView({block: 'end',
                        behavior: 'smooth'})
                },50)
            },
            songListIsLiked(){
                if(localStorage.getItem("token")===null || localStorage.getItem("token")===''){
                    ElMessage.success("您还未登录，不能收藏")
                    return;
                }
                if(this.isLike==='success'){
                    ElMessage.success("您已收藏此歌单，请勿重复操作")
                    return;
                }
                consumerLikeSongList({"id":this.songListId}).then(result=>{
                    if(result.success){
                        ElMessage.success("收藏成功")
                        this.isLike="success"
                        this.songList.likes+=1;
                    }else{
                        ElMessage.error(result.errorMsg)
                    }
                })
            },
            HanderCurrentPageComment(page){
                this.commentCurrentPage=page
                let params=new URLSearchParams();
                params.append("currentPage",page)
                params.append("pageSize",this.pageSize)
                params.append("songListId",this.songListId)
                params.append("type",this.type)
                getCommentsBySongListId(params).then(res=>{
                    if(res.success){
                        this.commentList=res.data;
                    }
                })
            },
            initTime(time){
                time=new Date(time).getTime()
                if(Date.now()-time>1000*60*60*24){
                    console.log(time)
                    let date= new Date(time)
                    console.log(date)
                    return date.getFullYear()+'-'+date.getMonth()+'-'+date.getDate();
                }else {
                    time=Date.now()-time+2;
                    if(time>60*60*1000){
                        return (time/3600000).toFixed(0)+'hour'
                    }else if(time>60000){
                        return (time/60000).toFixed(0)+'min'
                    }else {
                        return (time/1000).toFixed(0)+'sec'
                    }
                }

            },
            onLikeChange(i){
                if(localStorage.getItem("token")===null || localStorage.getItem("token")===''){
                    ElMessage.success("您还未登录，不能点赞")
                    localStorage.setItem("token","");
                    return;
                }
                let params=new URLSearchParams();
                params.append("commentId",i.id)
                params.append("liked",i.liked)
                consumerLikeComment(params).then(res=>{
                    if(res.success){
                        if(i.liked){
                            i.likes-=1;
                        }else {
                            i.likes+=1;
                        }
                        i.liked=!i.liked
                    }else {
                        ElMessage.error(res.errorMsg)
                    }
                })
            },
            submitComment(){
                if(localStorage.getItem("token")===null || localStorage.getItem("token")===''){
                    ElMessage.success("您还未登录，不能评论")
                    return;
                }
                let params=new URLSearchParams();
                if(this.comment.length>150){
                    ElMessage.error("字数不能超过150")
                    return;
                }
                params.append("songListId",this.songListId);
                params.append("content",this.comment);
                subComments(params).then(res=>{
                    if(res.success){
                        ElMessage.success("评论成功");
                        this.comment=''
                        this.isShowEmoji=false
                        this.commentCount+=1
                        this.HanderCurrentPageComment(1)
                    }else {
                        ElMessage.error(res.errorMsg)
                    }
                })
            },
            blurEvent(e) {
                this.blurIndex = e.srcElement.selectionStart;
            },
            handleSelectChange() {

            },
            likes(id) {
                ElMessage.success('关注成功' + id)
            },
            HanderCurrentPage(page) {
                if(this.songListId===null){
                    return
                }
                let params=new URLSearchParams();
                params.append("currentPage",page)
                params.append("pageSize",this.pageSize)
                params.append("songListId",this.songListId)
                getSongBySongListIdPage(params).then(res=>{
                    if(res.success){
                        this.songs=res.data;
                    }
                })
            },
            handleEmojiClick(detail) {
                let index = this.blurIndex
                let str = this.comment
                this.comment = str.slice(0, index) + detail.unicode + str.slice(index);
                this.blurIndex+=2;

            },
            init(){
               if(localStorage.getItem("consumer")!==null && localStorage.getItem("consumer")!=='') {
                   let consumer = JSON.parse(localStorage.getItem("consumer"));
                   this.consumerId = consumer.id;
               }
                songListGetById({"id":this.songListId}).then(res=>{
                    if(res.success) {
                        this.songList=res.data;
                        this.isLike=res.data.isLike;
                    }
                })
                songforSongListGetAll({"songListId":this.songListId}).then(res=>{
                    if(res.success){
                        this.songsTotal=res.data.length;
                        this.songsAll=res.data;
                    }
                })
                this.HanderCurrentPage(1);
                this.HanderCurrentPageComment(1);
                if(localStorage.getItem("consumer")!==null && localStorage.getItem("consumer")!==""){
                    let consumerAvator=JSON.parse(localStorage.getItem("consumer")).avator;
                    this.consumer.consumer.avator=this.$store.state.httpFileUrl+consumerAvator
                }
                getCommentCount({"songListId":this.songListId}).then(res=>{
                    this.commentCount=res.data;
                })
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
                    this.songListId = to.query.id;
                    this.init();
                }
            },
        },
        created() {
            this.consumerIsVip=this.$store.state.isVip
            this.songListId=this.$route.query.id;
            this.init();
            console.log(this.consumerId)
        },
    }
</script>

<style scoped>
.information{
    margin-top: 40px;
    height: 350px;


    /*background-color: #2aa3ef;*/
}
.email{
    margin-top: -5px;margin-left: 100px;
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
.imgIV{
    height:30px;
    width:30px;
    margin-left: 10px;
    margin-top:-5px
}
.created{
    /*margin-left: 5px;*/
    margin-top: -27px;
    font-family:微软雅黑;
    font-size: 13px;
    color: #000000;
    margin-left: 50px;
    /*margin-right: 190px*/

}
.box-card{
    font-size: 15px;
    margin-top: 4px;
    width:auto;
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
.action {
    display: inline-block;
    padding: 0 4px;
    color: var(--color-text-1);
    line-height: 24px;
    background: transparent;
    border-radius: 2px;
    cursor: pointer;
    transition: all 0.1s ease;
}

.action:hover {
    background: var(--color-fill-3);
}
</style>