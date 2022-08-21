<template>
  <div>
<!--    头部-->
    <div class="container">
      <div class="hand-box" >
        <el-button @click="deleteSelect" size="mini" type="primary">批量删除</el-button>
        <el-autocomplete
          @keyup.en.enter.native="updateDate"
          clearable
          class="hand-input"
          v-model="likeNameOne"
          :fetch-suggestions="querySearch"
          placeholder="请输入歌曲名或专辑"
          :trigger-on-focus="false"
          @select="handleSelect"
        ></el-autocomplete>
        <el-button @click="dialogIsShown=true" type="primary" size="mini">添加歌曲</el-button>
        <el-button @click="restore" type="primary" size="mini">恢复</el-button>
      </div>
    </div>
<!--    添加歌曲-->
    <el-table size="mini" border style="width:100%" height="1500px" :data="songListSelect" @selection-change="handleSelectChange">
      <el-table-column width="50" align="center" type="selection">
      </el-table-column>
      <el-table-column label="歌曲图片" width="110" align="center">
        <template slot-scope="scope">
          <div class="singer-img">
            <img :src="getUrl(scope.row.pic)" style="width:100%">
          </div>
          <div class="play" @click="setSongUrl(scope.row.url,scope.row.name)">
            <div v-if="toggle!=scope.row.name">
              <svg class="icon">
                <use xlink:href="#icon-bofanganniu"></use>
              </svg>
            </div>
            <div v-else>
              <svg class="icon">
                <use xlink:href="#icon-zanting"></use>
              </svg>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="歌手-歌曲" width="90" align="center"></el-table-column>
      <el-table-column prop="introduction" label="专辑" align="center" width="100"></el-table-column>
      <el-table-column label="VIP专属" align="center" width="80">
        <template slot-scope="scope">
          {{scope.row.isVip? '是':'否'}}
          <el-button size="mini" @click="convertVip(scope.row.id)">转换</el-button>
        </template>
      </el-table-column>
      <el-table-column label="歌词" align="textarea">
        <template slot-scope="scope">
          <ul style="height:150px;overflow:scroll">
            {{scope.row.lyric}}
          </ul>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100" align="center">
        <template slot-scope="scope">
          <el-button @click="getShowSinger(scope.row)" size="mini" class="updateBtn">修改</el-button>
          <el-button @click="deleteSinger(scope.row.id)" size="mini" class="deleteBtn">删除</el-button>
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
    <el-dialog title='添加歌曲' :visible.sync="dialogIsShown" width="400px" center>
      <el-form
        ref="SongInput" label-width="80px">
        <el-form-item label="歌曲名">
          <el-input v-model="SongInput.name" placeholder="歌曲名字" size="mini"></el-input>
        </el-form-item>
        <el-form-item label="专辑">
          <el-input placeholder="专辑" v-model="SongInput.introduction" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="歌词">
          <el-input placeholder="请输入歌词" v-model="SongInput.lyric" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="歌曲图片">
          <el-upload action="" :on-success="handleImgSuccess"
                     :before-upload="beforeAvatorUpload"
                     :on-change="changePicFile"
                     :auto-upload="false"
                     :limit="1"
          >
            <el-button size="mini">添加图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="歌曲文件">
          <el-upload action=""
                     :on-success="handleMusicSuccess"
                     :before-upload="beforeMusicUpload"
                     :on-change="changeMusicFile"
                     :auto-upload="false"
                     :limit="1"
          >
            <el-button size="mini">添加歌曲</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <span slot="footer">
      <el-button size="mini" @click="uploadSong">确认</el-button>
      <el-button size="mini" @click="dialogIsShown=false">取消</el-button>
    </span>
    </el-dialog>
<!--    修改歌曲-->
    <el-dialog title='修改歌曲' :visible.sync="updateSongIsShown" width="400px" center>
      <el-form
        ref="SongInput" label-width="80px">
        <el-form-item label="歌曲名">
          <el-input v-model="SongUpdate.name" placeholder="歌曲名字" size="mini"></el-input>
        </el-form-item>
        <el-form-item label="歌曲介绍">
          <el-input placeholder="专辑" v-model="SongUpdate.introduction" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="歌词">
          <el-input placeholder="请输入歌词" v-model="SongUpdate.lyric" type="textarea"></el-input>
        </el-form-item>

        <el-form-item label="歌曲图片">
          <el-upload action="" :on-success="handleImgSuccess"
                     :before-upload="beforeAvatorUpload"
                     :on-change="changePicFile"
                     :auto-upload="false"
                     :limit="1"
          >
            <el-button size="mini">添加图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="歌曲文件">
          <el-upload action=""
                     :on-success="handleMusicSuccess"
                     :before-upload="beforeMusicUpload"
                     :on-change="changeMusicFile"
                     :auto-upload="false"
                     :limit="1"
          >
            <el-button size="mini">添加歌曲</el-button>
          </el-upload>
        </el-form-item>



      </el-form>
      <span slot="footer">
      <el-button size="mini" @click="updateSong">确认</el-button>
      <el-button size="mini" @click="updateSongIsShown=false">取消</el-button>
    </span>
    </el-dialog>
    <!--  删除歌曲-->
    <el-dialog title='歌手删除' :visible.sync="deleteDialogIsShown" width="400px" center>
    <span slot="footer">
      <el-button size="mini" @click="deleteSingerById">确认</el-button>
    <el-button size="mini" @click="deleteDialogIsShown=false">取消</el-button>
    </span>
    </el-dialog>
    <el-dialog title="确认删除选中全部" :visible.sync="deleteDialogIsShownIndexs" width="400px" center>
    <span slot="footer">
      <el-button size="mini" @click="deleteSingerByIndexs">确认？</el-button>
    <el-button size="mini" @click="deleteDialogIsShownIndexs=false">取消</el-button>
    </span>
    </el-dialog>
  </div>
</template>

<script>
  import {mixin} from "../../mixins";
  import {mapGetters} from 'vuex';
  import '@/assets/js/iconfont.js';
  import {
    songUpload,
    songAllList,
    songGetPage,
    songGetLikes,
    songGetNameAndIntro,
    songUpdate,
    songDeleteById,
    songDeleteByIds,
    restoreSong,
    convertVipStatus
  } from '../../api/index'
  import axios from "axios";
  export default {
    name: "",
    mixins:[mixin],
    data(){
      return{
        deleteDialogIsShownIndexs:false,
        updateSongIsShown:false,
        dialogIsShown:false,
        deleteDialogIsShown:false,
        likeNameOne:'',
        singerId:'',
        singerName:'',
        SongInput:{
          name:'',
          introduction:'',
          pic:'',
          lyric:'',
          url:''
        },
        SongUpdate:{
          id:-1,
          name:'',
          pic:'',
          introduction:'',
          lyric:''
        },
        songAllList:[],
        songListSelect:[],
        currentPage:1,
        pageSize:20,
        total:1,
        deleteId:-1,
        deleteSongIds:[],
        toggle:'aaa',
        picFile:null,
        musicFile:null,
      }
    },
    methods:{
      convertVip(id){
        convertVipStatus({"id":id}).then(res=>{
          if(res.success){
            this.handleCurrentChange(this.currentPage)
            this.$notify.success(res.data.message)
          }else {
            this.$notify.error(res.errorMsg)
          }
        })
      },
      changePicFile(file){
        this.picFile=file;
      },
      changeMusicFile(file){
        this.musicFile=file;
      },
      setSongUrl(url,name){
        this.toggle=name;
        this.$store.state.url=this.$store.state.httpFileUrl+url;
        if(this.isPlay){
          this.$store.state.isPlay=false
          this.toggle=''
        }else {
          this.$store.state.isPlay=true
        }
      },
      handleCurrentChange(currentPage){
        let params=new URLSearchParams();
        params.append('currentPage', currentPage);
        params.append('pageSize', this.pageSize);
        params.append('singerId',this.singerId);
        songGetPage(params).then(res=>{
          if(res.success){
            this.songListSelect=res.data;
            this.$notify.success("查询成功")
          }else {
            this.$notify.error(res.errorMsg)
          }
        })
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
      deleteSinger(id){
        this.deleteId=id;
        this.deleteDialogIsShown=true;
      },
      deleteSingerById(){
        songDeleteById({"id":this.deleteId}).then(res => {
            if(res.success){
              this.$notify.success(res.data);
              this.handleCurrentChange(this.currentPage);
              this.total=this.total-1
            }else {
              this.$notify.error(res.errorMsg)
            }
        })
        this.deleteDialogIsShown=false;
      },
      getShowSinger(item){
        this.SongUpdate.id=item.id;
        this.SongUpdate.lyric=item.lyric;
        this.SongUpdate.introduction=item.introduction;
        this.SongUpdate.name=item.name;
        this.updateSongIsShown=true;
      },
      updateSong(){
        let params =new URLSearchParams();
        if(this.musicFile!==null || this.picFile!==null){
          params=new FormData()
          if(this.musicFile!==null){
            params.append('musicFile',this.musicFile.raw)
          }
          if(this.picFile!==null){
            params.append('picFile',this.picFile.raw)
          }
        }
        params.append('id',this.SongUpdate.id)
        params.append('name',this.SongUpdate.name);
        params.append('introduction',this.SongUpdate.introduction);
        params.append('lyric',this.SongUpdate.lyric);

        songUpdate(params).then(res=>{
          if(res.success){
            this.$notify.success(res.data)
            this.handleCurrentChange(this.currentPage)
          }else {
            this.$notify.error(res.errorMsg)
          }
        })
        this.updateSongIsShown=false;
      },
      handleSelectChange(val){
        this.deleteSongIds=val;
      },
      deleteSelect(){
        this.deleteDialogIsShownIndexs=true;
      },
      deleteSingerByIndexs(){
        let indexs=[]
        this.deleteSongIds.forEach(each=>{
          indexs.push(each.id)
        })
        console.log(indexs)
        let params=new URLSearchParams();
        params.append("ids",indexs)
        songDeleteByIds(params).then(res=>{
          if(res.success){
            this.$notify.success(res.data);
            let params1=new URLSearchParams;
            params1.append('singerId',this.singerId);
            songAllList(params1).then(res=>{
              if(res.success){
                if(res.data.length>0){
                  this.songAllList=res.data;
                  this.total=res.data.length;
                }
              }
            })
            this.handleCurrentChange(1)
            this.deleteSongIds=[]
          }else {
            this.$notify.error(res.errorMsg);
          }
        })
        this.deleteDialogIsShownIndexs=false;
      },
      updateDate(){

      },
      querySearch(queryString,cb){
        let params=new URLSearchParams();
        params.append("name",queryString);
        params.append('singerId',this.singerId)
        let result=[];
        songGetNameAndIntro(params).then(res=>{
          res.data.map(each=>{
            result.push({'value' : each})
          })
        })
        cb(result)
      },
      handleSelect(item){
        let params =new URLSearchParams();
        params.append('name',item.value)
        songGetLikes(params).then(res=>{
          if(res.success){
            this.songListSelect=res.data;
            this.total=res.data.length;
          }
        })
      },
      uploadSong(){
        let params =new URLSearchParams();
        if(this.musicFile!==null || this.picFile!==null){
          params=new FormData()
          if(this.musicFile!==null){
            params.append('musicFile',this.musicFile.raw)
          }
          if(this.picFile!==null){
            params.append('picFile',this.picFile.raw)
          }
        }
        params.append('singerId',this.singerId)
        params.append('name',this.singerName+'-'+this.SongInput.name);
        params.append('introduction',this.SongInput.introduction);
        params.append('lyric',this.SongInput.lyric);

        axios.post(
            'http://localhost/song/upload',
          params,
          {
            headers: { "Content-Type": head }
          }
        ).then(res=>{
          if(res.success){
            this.$notify.success(res.data);
            this.total=this.total+1
          }else {
            this.$notify.error(res.errorMsg);
          }
        })
        this.dialogIsShown=false
      },
      restore(){
        restoreSong().then(res=>{
          if(res.success){
            this.$notify.success(res.data);
            let params=new URLSearchParams;
            params.append('singerId',this.singerId);
            songAllList(params).then(res=>{
              if(res.success){
                if(res.data.length>0){
                  this.songAllList=res.data;
                  this.total=res.data.length;
                }
              }
            })
            this.handleCurrentChange(1)
          }else {
            this.$notify.error(res.errorMsg);
          }
        })
      },
      uploadImg(){
        return this.$store.state.Host+'/song/uploadImg'
      },
      handleImgSuccess(res, file){
        if(this.updateSongIsShown){
          if(res.success){
            this.SongUpdate.pic=res.data.url
            this.$notify.success(res.data.message)
          }else {
            this.$notify.error(res.errorMsg)
          }
        }
        if(this.dialogIsShown){
          if(res.success){
            this.SongInput.pic=res.data.url
            this.$notify.success(res.data.message)
          }else {
            this.$notify.error(res.errorMsg)
          }
        }

      },
      uploadMusic(){
        return this.$store.state.Host+'/song/uploadMusic'
      },
      beforeMusicUpload(file){
        if (!(file.type === 'audio/mpeg') && !(file.type === 'audio/mp4') && !(file.type === 'audio/x-aiff')) {
          this.$notify.error('格式错误')
          return false
        }
        if (!((file.size / 1024 / 1024) < 20)) {
          this.$notify.error('文件大小不能超过20M')
          return false
        }
        return true
      },
      handleMusicSuccess(res, file){
        if(res.success){
          if(typeof (res.data)!='string'){
            this.SongInput.url=res.data.url
            this.$notify.success(res.data.message)
          }else {
            this.$notify.success(res.data)
          }

        }else {
          this.$notify.error(res.errorMsg)
        }
      }
    },
    computed:{
      ...mapGetters([
        'url',
        'isPlay'
      ])
    },
    created() {
      this.singerId=this.$route.query.id
      this.singerName=this.$route.query.name;
      console.log(this.singerId+"   "+this.singerName)
      let params=new URLSearchParams;
      params.append('singerId',this.singerId);
      songAllList(params).then(res=>{
        if(res.success){
          if(res.data.length>0){
            this.songAllList=res.data;
            this.total=res.data.length;
          }
        }else {
          this.$notify.error("未查到数据")
        }
      })
      this.handleCurrentChange(1)
    },
    beforeRouteEnter(to,from,next){
      if(from.name==='SongListForSinger'){
        to.meta.isBack=true
      }
      next();
    }

  }
</script>

<style scoped>
  .hand-box{
    margin-bottom: 20px;
    /*margin-top: 0px;*/
  }
  .uploadImg{
    margin-left: 20px;
  }
  .updateBtn{
    margin-bottom:8px
  }
  .deleteBtn{
    margin-top: 8px;
    margin-right: 9px;
  }
  .updatePic{
    margin-bottom: 20px;
  }
  .updateMusic{
    margin-top: 20px;
  }
  .play{
    position: absolute;
    z-index: 100;
    width: 40px;
    height: 40px;
    display: flex;
    align-items:center;
    justify-content: center;
    cursor: pointer;
    top:40px;
    left: 18px;
  }
  .icon{
    width:1.5em;
    height:1.5em;
    color: #178d00;
    fill: currentColor;
    overflow: hidden;
  }
</style>
