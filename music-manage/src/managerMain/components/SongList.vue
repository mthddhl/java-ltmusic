<template>
<div>
  <div class="container">
    <div class="hand-box" >
      <el-button @click="deleteSelect" size="mini" type="primary">批量删除</el-button>
      <el-autocomplete
        @keyup.en.enter.native="updateDate"
        clearable
        class="hand-input"
        v-model="likeNameOne"
        :fetch-suggestions="querySearch"
        placeholder="请输入内容"
        :trigger-on-focus="false"
        @select="handleSelect"
      ></el-autocomplete>
      <el-button @click="dialogIsShown=true" type="primary" size="mini">添加歌单</el-button>
      <el-button @click="restore" type="primary" size="mini">恢复</el-button>
    </div>
  </div>
  <el-table size="mini" border style="width:100%" height="700px" :data="songListSelect" @selection-change="handleSelectChange">
    <el-table-column width="50" align="center" type="selection">
    </el-table-column>
    <el-table-column label="歌单图片" width="110" align="center">
      <template slot-scope="scope">
        <div class="singer-img">
          <img :src="getUrl(scope.row.pic)" style="width:100%">
        </div>
      </template>
    </el-table-column>
    <el-table-column prop="title" label="歌单标题" width="90" align="center"></el-table-column>
    <el-table-column prop="introduction" label="歌单简介" type="textarea"></el-table-column>
    <el-table-column prop="style" label="歌单风格" width="90" align="center"></el-table-column>
    <el-table-column label="歌单管理" align="center" width="130">
      <template slot-scope="scope">
        <el-button size="mini" @click="songListEdit(scope.row.id,scope.row.title)">歌单管理</el-button>
        <el-button size="mini" @click="toCarousel(scope.row)" style="margin-top: 15px;margin-left: -6px">添加到走马灯</el-button>
      </template>
    </el-table-column>
    <el-table-column label="操作" width="100" align="center">
      <template slot-scope="scope">
        <el-button @click="getShowSongList(scope.row)" size="mini" class="updateBtn">修改</el-button>
        <el-button @click="deleteSongList(scope.row.id)" size="mini" class="deleteBtn">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <div class="page">
    <el-pagination
      @current-change="handleCurrentChange"
      :current-page.sync="currentPage"
      :page-size="pageSize"
      layout="prev, pager, next"
      :total="total">
    </el-pagination>
  </div>
  <el-dialog title='添加歌单' :visible.sync="dialogIsShown" width="400px" center>
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
      <el-form-item label="歌单图片">
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
          <template>
            <div class="el-upload__tip">
              jpg/png 大小不能超过2M
            </div>
          </template>
        </el-upload>
      </el-form-item>
    </el-form>
    <span slot="footer">
      <el-button size="mini" @click="uploadSongList">确认</el-button>
      <el-button size="mini" @click="dialogIsShown=false">取消</el-button>
    </span>
  </el-dialog>
<!--  修改歌单-->
  <el-dialog title='歌单修改' :visible.sync="updateDialogIsShown" width="400px" center>
    <el-form
      ref="singerInput" label-width="80px">
      <el-form-item label="歌单标题">
        <el-input v-model="SongListUpdate.title" placeholder="歌单标题" size="mini"></el-input>
      </el-form-item>
      <el-form-item label="歌单简介">
        <el-input placeholder="请输入歌单简介" v-model="SongListUpdate.introduction"></el-input>
      </el-form-item>
      <el-form-item label="歌单风格">
        <el-input placeholder="请输入歌单风格" v-model="SongListUpdate.style"></el-input>
      </el-form-item>
      <el-form-item label="歌单图片">
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
          <template>
            <div class="el-upload__tip">
              jpg/png 大小不能超过2M
            </div>
          </template>
        </el-upload>
      </el-form-item>
    </el-form>
      <span slot="footer">
      <el-button size="mini" @click="updateSongList">确认</el-button>
      <el-button size="mini" @click="updateDialogIsShown=false">取消</el-button>
    </span>
  </el-dialog >
  <el-dialog title='歌手删除' :visible.sync="deleteDialogIsShown" width="400px" center>
    <span slot="footer">
      <el-button size="mini" @click="deleteSongListById">确认</el-button>
    <el-button size="mini" @click="deleteDialogIsShown=false">取消</el-button>
    </span>
  </el-dialog>
  <el-dialog title="确认删除选中全部" :visible.sync="deleteDialogIsShownIndexs" width="400px" center>
    <span slot="footer">
      <el-button size="mini" @click="deleteSongListByIds">确认？</el-button>
    <el-button size="mini" @click="deleteDialogIsShownIndexs=false">取消</el-button>
    </span>
  </el-dialog>
</div>
</template>

<script>
  import {
    songListGetAll, songListGetPage, consumerCreatedSongList, songListGetLikeName,
    songListUpdate, songListdeleteById, songListdeleteByIds,
    songListRestart, addSingerToCarousel
  } from "../../api";

  export default {
    name: "",
    data(){
      return{
        deleteDialogIsShownIndexs:false,
        deleteDialogIsShown:false,
        updateDialogIsShown:false,
        dialogIsShown:false,
        songListSelect:[],
        currentPage:1,
        pageSize:10,
        total:10,
        likeNameOne:"",
        SongListInput:{
          id:"",
          title:"",
          introduction:'',
          style:''
        },
        SongListUpdate:{
          id:"",
          title:"",
          // pic:"",
          introduction:'',
          style:''
        },
        file:null,
        deleteOneId:-1,
        deleteIds:[]
      }
    },
    methods:{
      toCarousel(row){
        let params=new URLSearchParams();
        params.append("type","songList")
        params.append("toId",row.id);
        params.append("title",row.title);
        params.append("pic",row.pic)
        addSingerToCarousel(params).then(res=>{
          if(res.success){
            this.$notify.success(res.data)
          }else {
            this.$notify.error(res.errorMsg)
          }
        })
      },
      handleSelectChange(val){
        val.forEach(each=>{
          this.deleteIds.push(each.id)
        })
        console.log(this.deleteIds)
      },
      deleteSelect(){
        this.deleteDialogIsShownIndexs=true;
      },
      deleteSongListByIds(){
        let ids=[]
          this.deleteIds.forEach(each=>{ids.push(each)})
        let params=new URLSearchParams();
        params.append("ids",ids)
        songListdeleteByIds(params).then(res=>{
          if(res.success){
            this.$notify.success(res.data);
            this.handleCurrentChange(this.currentPage)
          }else {
            this.$notify.error(res.errorMsg);
          }
        })
        this.deleteDialogIsShownIndexs=false;
      },
      updateDate(){
        if(this.likeNameOne===''){
          this.handleCurrentChange(1);
        }
      },
      querySearch(queryString,cb){
        let params=new URLSearchParams();
        params.append("name",queryString);
        let result=[];
        songListGetLikeName(params).then(res=>{
          res.data.map(each=>{
            result.push({'value' : each})
          })
        })
        cb(result)
      },
      handleSelect(temp){
        this.handleCurrentChange(this.currentPage);
      },
      restore(){
        songListRestart().then(result=>{
          if(result.success){
            this.handleCurrentChange(1);
          }
        })
      },
      changeFile(file){
        this.file=file
      },
      uploadSongList(){
        if(this.SongListInput.style.length>10){
          this.$notify.error("风格字数长度过长，请修改")
        }else{

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
              this.$notify.success(res.data)
              this.handleCurrentChange(1)

            }else {
             this.$notify.error(res.errorMsg)
            }
          })
          this.file=null
          this.SongListInput={}
          this.dialogIsShown=false;
        }
      },
      getUrl(pic){
        return this.$store.state.httpFileUrl+pic
      },
      uploadImg(){
        return this.$store.state.Host+'/songList/changeImg'
      },
      handleImgSuccess(res,file){
        this.SongListInput.pic=res.data;
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
      songListEdit(id,name){
        this.$router.push({path:'/SongForSongList',query:{id,name}})
      },
      getShowSongList(row){
        this.SongListUpdate.title=row.title;
        this.SongListUpdate.introduction=row.introduction;
        this.SongListUpdate.style=row.style;
        this.SongListUpdate.id=row.id;
        this.updateDialogIsShown=true;
      },
      updateSongList(){
        let head='application/json'
        let params=new URLSearchParams();
        if(this.file!==null){
          params=new FormData()
          head='multipart/form-data'
          params.append('file',this.file.raw)
        }
        params.append("id",this.SongListUpdate.id)
        params.append("title",this.SongListUpdate.title);
        params.append("introduction",this.SongListUpdate.introduction);
        params.append("style",this.SongListUpdate.style);
        songListUpdate(params).then(res=>{
          if(res.success){
            this.$notify.success(res.data)
            this.handleCurrentChange(this.currentPage)
            this.file=null
          }else {
            this.$notify.error(res.errorMsg)
          }
        })
        this.updateDialogIsShown=false
      },
      deleteSongList(id){
        this.deleteOneId=id;
        this.deleteDialogIsShown=true;
      },
      deleteSongListById(){
        songListdeleteById({"id":this.deleteOneId}).then(res=>{
          if(res.success){
            this.$notify.success(res.data);
            this.handleCurrentChange(this.currentPage)
          }else {
            this.$notify.error(res.errorMsg);
          }
        })
        this.deleteDialogIsShown=false;
      },

      handleCurrentChange(page){
        let params =new URLSearchParams();
        params.append('currentPage',page);
        params.append('pageSize',this.pageSize)
        params.append("likeNameOne",this.likeNameOne)
        songListGetPage(params).then(res=>{
          this.songListSelect=res.data.data;
          this.total=res.data.count;
        })
      },
    },
    created() {
      songListGetAll().then(res=>{
        this.total=res.data.length;
      })
      this.handleCurrentChange(1);
    }
  }
</script>

<style scoped>
  .hand-box{
    margin-bottom: 20px;
    /*margin-top: 0px;*/
  }
  .singer-img{
    width: 100%;
    height:80px;
    border-radius:5px;
    margin-bottom: 5px;
    overflow: hidden;
  }
  .hand-input{
    width: 300px;
    display: inline-block;
  }
  .page{
    /*position: absolute;*/
    display: inline-block;
    margin-bottom: 100px;
    /*justify-content: center;*/
    margin-left: 450px;
    margin-top:10px;
  }
  .updateBtn{
    margin-bottom:8px
  }
  .deleteBtn{
    margin-top: 8px;
    margin-right: 9px;
  }
</style>
