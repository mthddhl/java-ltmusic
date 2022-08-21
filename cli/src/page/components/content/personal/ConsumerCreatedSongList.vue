<template>
    <div style="margin-top: 20px;margin-left: 30px" v-if="isPerson">
        <el-button @click="isCreated=true">创建歌单</el-button>
    </div>
    <div class="space">
        <el-space :fill="false" wrap :fill-ratio="50" :size="40">
            <div v-for="(i,index) in songList" :key="i" class="box-card">
                <div class="box" :style="{ backgroundImage: `url(${this.$store.state.httpFileUrl+i.pic})` }"
                >
                    <div class="link" @click="jump(i.id)"></div>
                    <div class="content">
                        <el-row>
                            <el-col :span="4"><el-icon class="headset"><Headset/></el-icon></el-col>
                            <el-col :span="17" style="margin-top: 4px;margin-left: -5px"><span class="count">{{initNum(i.count)}}</span></el-col>
                            <el-col :span="3" style="margin-top: 8px">
                                <el-dropdown trigger="click">
                                    <el-icon class="playIcon"><MoreFilled /></el-icon>
                                    <template #dropdown>
                                        <el-dropdown-menu>
                                            <el-dropdown-item @click="play(i)">播放</el-dropdown-item>
                                            <el-dropdown-item @click="deleteSongList(i,index)" v-if="this.isPerson">删除歌单</el-dropdown-item>
                                            <el-dropdown-item @click="update(i)" v-if="this.isPerson">修改歌单</el-dropdown-item>
                                            <el-dropdown-item @click="managerSongs(i)" v-if="this.isPerson">管理歌曲</el-dropdown-item>
                                        </el-dropdown-menu>
                                    </template>
                                </el-dropdown>
                            </el-col>
                        </el-row>
                    </div>
                </div><div style="height:15px"></div>
                <div>
                    <span style="color: #000000">{{i.title}}</span>
                </div>
            </div>
        </el-space>
    </div>
<!--    创建歌单-->
    <el-dialog
            v-model="isCreated"
            title="Tips"
            width="30%"
    >
        <el-form
                ref="SongListInput" label-width="80px">
            <el-form-item label="歌单名">
                <el-input v-model="SongListInput.title" nameplaceholder="歌单标题" size="mini"></el-input>
            </el-form-item>
            <el-form-item label="歌单介绍">
                <el-input placeholder="歌单介绍" v-model="SongListInput.introduction" type="textarea"></el-input>
            </el-form-item>
            <el-form-item label="歌单风格">
                <el-input placeholder="请输入歌单风格" v-model="SongListInput.style" type="textarea"></el-input>
            </el-form-item>
            <el-upload
                    class="upload-demo"
                    action=""
                    multiple
                    :on-change="changeFile"
                    :before-upload="beforeAvatorUpload"
                    :limit="1"
                    :auto-upload="false"
            >
                <el-button type="primary">上传图片</el-button>
                <template #tip>
                    <div class="el-upload__tip">
                        jpg/png 大小不能超过2M
                    </div>
                </template>
            </el-upload>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="isCreated = false">取消</el-button>
        <el-button type="primary" @click="submit"
        >提交</el-button
        >
      </span>
        </template>
    </el-dialog>
<!--    更新歌单-->
    <el-dialog
            v-model="isUpdate"
            title="Tips"
            width="30%"
    >
        <el-form
                ref="SongListInput" label-width="80px">
            <el-form-item label="歌单名">
                <el-input v-model="updateSongList.title" nameplaceholder="歌单标题" size="mini"></el-input>
            </el-form-item>
            <el-form-item label="歌单介绍">
                <el-input placeholder="歌单介绍" v-model="updateSongList.introduction" type="textarea"></el-input>
            </el-form-item>
            <el-form-item label="歌单风格">
                <el-input placeholder="请输入歌单风格" v-model="updateSongList.style" type="textarea"></el-input>
            </el-form-item>
            <el-upload
                    class="upload-demo"
                    action=""
                    multiple
                    :on-change="changeFile"
                    :before-upload="beforeAvatorUpload"
                    :limit="1"
                    :auto-upload="false"
            >
                <el-button type="primary">上传图片</el-button>
                <template #tip>
                    <div class="el-upload__tip">
                        jpg/png 大小不能超过2M
                    </div>
                </template>
            </el-upload>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="isUpdate = false">取消</el-button>
        <el-button type="primary" @click="updateSubmit"
        >提交</el-button
        >
      </span>
        </template>
    </el-dialog>
    <el-dialog
            v-model="isUpdateSongs"
            title="Tips"
            width="50%"
    >
        <el-table :data="songs" style="width: 100%" @selection-change="handleSelectChange1">
            <el-table-column prop="name" label="歌曲名" width="330" />
            <el-table-column width="50" align="center" type="selection">
            </el-table-column>
        </el-table>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="isUpdateSongs = false">取消</el-button>
        <el-button type="primary" @click="updateSongs"
        >确定</el-button
        >
      </span>
        </template>
    </el-dialog>
</template>

<script>
    import {
        getPartSongList,
        consumerGetLikeSingList,
        songforSongListGetAll,
        songListGetSongsCountIncr,
        consumerDeleteLikeSongList,
        getTogetLikedSongList,
        consumerCreatedSongList,
        consumerGetCreatedSongList, songListUpdate, consumerUpdateSongListSongs
    } from "@/ajax/getAndPost";
    import {ElMessage} from "element-plus";
    import axios from "axios";

    export default {
        name: "",
        data(){
            return{
                isCreated:false,
                songList:[],
                consumerId:-1,
                isPerson:false,
                loginAndP:false,
                currentState:"全部",
                type:{
                    'color':'rgba(255,0,0,0.97)',
                },
                SongListInput:{},
                file:null,
                updateSongList:{},
                isUpdate:false,
                isUpdateSongs:false,
                songs:[],
                songsHasSelectIds:[],
                updateSongListId:-1
            }
        },
        methods:{
            handleSelectChange1(val){
                this.songsHasSelectIds=[]
                val.map(a=>{
                    this.songsHasSelectIds.push(a.songId)
                })
            },
            updateSongs(){
                let params=new URLSearchParams();
                params.append("songListId",this.updateSongListId)
                params.append("songs",this.songsHasSelectIds)
                consumerUpdateSongListSongs(params).then(res=>{
                    if(res.success){
                        ElMessage.success(res.data)
                        this.isUpdateSongs=false
                    }else {
                        ElMessage.error(res.errorMsg)
                    }
                })
            },
            managerSongs(i){
                if(!this.isPerson){
                    ElMessage.error("未登录，不能管理歌单")
                    return
                }
                songforSongListGetAll({"songListId":i.id}).then(res=>{
                    if(res.success){
                        this.songs=res.data;
                        this.updateSongListId=i.id
                        this.isUpdateSongs=true
                    }
                })
            },
            updateSubmit(){
                let head='application/json'
                let params=new URLSearchParams();
                if(this.file!==null){
                    params=new FormData()
                    head='multipart/form-data'
                    params.append('file',this.file.raw)
                }
                params.append("id",this.updateSongList.id)
                params.append("title",this.updateSongList.title);
                params.append("introduction",this.updateSongList.introduction);
                params.append("style",this.updateSongList.style);
                params.append("pic",this.updateSongList.pic);
                songListUpdate(params).then(res=>{
                    if(res.success){
                        ElMessage.success(res.data)
                        this.init()
                    }else {
                        ElMessage.error(res.errorMsg)
                    }
                })
                this.isUpdate=false
            },
            update(i){
                if(!this.isPerson){
                    ElMessage.error("未登录，不能修改歌单")
                    return
                }
                this.updateSongList.id=i.id
                this.updateSongList.title=i.title
                this.updateSongList.introduction=i.introduction
                this.updateSongList.style=i.style
                this.updateSongList.pic=i.pic
                this.isUpdate=true
            },
            submit(){
                let head='application/x-www-form-urlencoded;charset=utf-8'
                let params=new URLSearchParams();
                if(this.file!==null){
                    params=new FormData()
                    head='multipart/form-data'
                    params.append('file',this.file.raw)
                }
                params.append("title",this.SongListInput.title);
                params.append("introduction",this.SongListInput.introduction);
                params.append("style",this.SongListInput.style);
                consumerCreatedSongList(params).then(res=>{
                    if(res.success){
                        ElMessage.success(res.data)
                        consumerGetCreatedSongList({"id":this.consumerId}).then(res=>{
                            if(res.success){
                                this.songList=res.data;
                            }
                        })
                    }else {
                        ElMessage.success(res.errorMsg)
                    }
                })
                this.file=null
                this.SongListInput={}
                this.isCreated=false;

            },
            changeFile(file){
                this.file=file
            },
            beforeAvatorUpload (file) {
                if (!(file.type === 'image/jpeg')) {
                    this.$notify.error('格式错误')
                    return false
                }
                if (!((file.size / 1024 / 1024) < 2)) {
                    this.$notify.error('文件大小错误')
                    return false
                }
                return true
            },
            jump(id){
                this.$router.push({path:'/songList',query:{id}})
            },
            deleteSongList(i,index){
                let id=i.id
                axios.get(
                    '/songList/songListDeleteById',
                    {
                        params:{
                            "id":id
                        }
                    }
                ).then(res=>{
                    if(res.data.success){
                        ElMessage.success("删除成功")
                        this.songList.splice(index,1)
                    }else{
                        ElMessage.error(res.data)
                    }
                })
            },
            play(i){
                songforSongListGetAll({"songListId":i.id}).then(res=>{
                    if(res.success){
                        if(res.data.length===0){
                            ElMessage.error('歌单无歌曲')
                            return
                        }
                        let songs=[];
                        res.data.forEach(each=>{
                            if(!each.isVip || this.$store.state.isVip) {
                                songs.push({
                                    name: each.name,
                                    artist: each.singerName,
                                    url: this.$store.state.httpFileUrl + each.url,
                                    cover: each.pic,
                                    lrc:each.lyric
                                })
                            }
                        })
                        let r={
                            status:1,
                            song:songs,
                        }
                        this.$store.state.songList=r;
                        if(!i.isplay){
                            if(!this.isSongListPlay){
                                songListGetSongsCountIncr({"id":i.id}).then(res=>{
                                    if(res.success){
                                        i.count+=1;
                                        i.isplay=true;
                                        ElMessage.success("播放成功")
                                    }
                                })
                            }
                        }
                    }
                })
            },
            initNum(num){
                if(num>=10000){
                    return (num/10000).toFixed(2)+'w';
                }else {
                    return num;
                }
            },
            init(){
                this.isPerson=false
                if(localStorage.getItem("token")!==null &&localStorage.getItem("token")!==''){
                    if(localStorage.getItem("consumer")!==null && localStorage.getItem("consumer")!==''){
                        let consumer=JSON.parse(localStorage.getItem("consumer"));
                        if(this.consumerId+""===consumer.id+""){
                            this.isPerson=true;
                        }
                        if(this.consumerId+""!==consumer.id+""){
                            this.loginAndP=true
                        }
                    }
                }
                consumerGetCreatedSongList({"id":this.consumerId}).then(res=>{
                    if(res.success){
                        this.songList=res.data;
                    }
                })
            }
        },
        watch: {
            $route(to, from) {
                if (to.query.id !== from.query.id) {
                    this.consumerId = to.query.id;
                    this.init();
                }
            },
        },
        created() {
            this.consumerId=this.$route.query.id
            this.init();
        }
    }
</script>

<style scoped>
    .space{
        margin-top:20px;
        margin-left: 60px;
        height: 1000px;
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