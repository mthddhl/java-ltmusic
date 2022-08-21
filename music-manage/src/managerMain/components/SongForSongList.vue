<template>
<div>
  <div class="container">
    <div class="hand-box" >
      <h3>歌单：{{songListTitle}}</h3>
      <el-button @click="deleteSelect" size="mini" type="primary">批量移除</el-button>
      <el-autocomplete
        clearable
        class="hand-input"
        v-model="likeNameOne"
        :fetch-suggestions="querySearch"
        placeholder="请输入歌曲名"
        :trigger-on-focus="false"
        @select="handleSelect"
      ></el-autocomplete>
    </div>
  </div>
  <el-table size="mini" border style="width:100%" height="700px" :data="songListSelect" @selection-change="handleSelectChange">
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
    <el-table-column label="歌手" width="150" align="center">
      <template slot-scope="scope">
        <div class="singer" @click="toSinger(scope.row.singerId,scope.row.singer)">{{scope.row.singer}}</div>
      </template>
    </el-table-column>
    <el-table-column prop="name" label="歌曲名" width="150" align="center"></el-table-column>
    <el-table-column prop="introduction" label="专辑" align="center" width="150"></el-table-column>
    <el-table-column prop="isVip" label="VIP专属" align="center" width="150"></el-table-column>
    <el-table-column label="操作" width="150" align="center">
      <template slot-scope="scope">
        <el-button @click="removeSongList(scope.row.id)" size="mini" class="updateBtn">移除歌单</el-button>
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
  <el-dialog title='歌曲移除？' :visible.sync="deleteDialogIsShown" width="400px" center>
    <span slot="footer">
      <el-button size="mini" @click="deleteSingerById">确认</el-button>
    <el-button size="mini" @click="deleteDialogIsShown=false">取消</el-button>
    </span>
  </el-dialog>
  <el-dialog title='歌曲移除选中？' :visible.sync="deleteDialogIsShownIndexs" width="400px" center>
    <span slot="footer">
      <el-button size="mini" @click="deleteSingerByIds">确认</el-button>
    <el-button size="mini" @click="deleteDialogIsShownIndexs=false">取消</el-button>
    </span>
  </el-dialog>
  <el-dialog title='添加歌曲' :visible.sync="insertSongDialog" width="600px" center>

    <template>
      <el-select v-model="selectTool" placeholder="请选择" style="width: 200px">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
    </template>
    <el-autocomplete
      clearable
      class="hand-input"
      v-model="likeNameOneSong"
      :fetch-suggestions="querySearchSong"
      placeholder="请输入歌曲名"
      :trigger-on-focus="false"
      @select="handleSelectSong"
    ></el-autocomplete>
<!--    <template>-->
<!--        <ul class="infinite-list" v-infinite-scroll="load" style="overflow:auto"  infinite-scroll-distance="10">-->
<!--          <li v-for="i in count" class="infinite-list-item">{{ i }}</li>-->
<!--        </ul>-->
<!--    </template>-->
    <span slot="footer">
      <el-button size="mini" @click="insertSong">确认</el-button>
    <el-button size="mini" @click="insertSongDialog=false">取消</el-button>
    </span>
  </el-dialog>
</div>
</template>

<script>
  import {mixin} from "../../mixins";
  import '@/assets/js/iconfont.js';
  import {mapGetters} from 'vuex';
  import {songforSongListGetAll,songforSongListGetPage,songforSongListGetLikeName,
          songforSongListdeleteById,songforSongListdeleteByIds} from "../../api";
  export default {
    mixins:[mixin],
    name: "",
    data(){
      return{
        options:[{
          value: '按歌手查询',
          label: '按歌手查询'
        },
        {
          value: '按歌曲查询',
          label: '按歌曲查询'
        }
        ],
        selectTool:"",
        insertSongDialog:false,
        deleteDialogIsShownIndexs:false,
        deleteDialogIsShown:false,
        songListid:1,
        likeNameOne:"",
        songListTitle:'',
        songListSelect:[],
        currentPage:0,
        pageSize:5,
        total:-1,
        deleteId:0,
        deleteIds:[],
        toggle:'aaa',
        likeNameOneSong:"",
        count:10,
      }
    },
    methods:{
      load(){
        alert(1111)
        this.count+=2
      },
      insertSong(){
        this.count+=2
      },
      querySearchSong(queryString,cb){

      },
      handleSelectSong(item){

      },
      setSongUrl(url,name){
        this.toggle=name;
        this.$store.state.url=this.$store.state.httpFileUrl+url;
        if(this.isPlay){
          this.$store.state.isPlay=false
          this.toggle='aaa'
          console.log(this.toggle)
        }else {
          this.$store.state.isPlay=true
        }
      },
      toSinger(id,name){
        this.$router.push({path:'/SongListForSinger',query:{id,name}})
      },
      removeSongList(id){
        this.deleteId=id;
        this.deleteDialogIsShown=true;
      },
      deleteSingerByIds(){
        let ids=[]
        this.deleteIds.forEach(each =>{ids.push(each)})
        console.log(ids)
        let params=new URLSearchParams();
        params.append("ids",ids)
        songforSongListdeleteByIds(params).then(res=>{
          if(res.success){
            this.$notify.success(res.data)
            this.handleCurrentChange(this.currentPage);
            this.total=this.total-ids.length
          }else {
            this.$notify.error(res.errorMsg)
          }
        })
        this.deleteDialogIsShownIndexs=false
      },
      deleteSingerById(){
        songforSongListdeleteById({"id":this.deleteId}).then(res=>{
          if(res.success){
            this.$notify.success(res.data)
            this.handleCurrentChange(this.currentPage);
          }else {
            this.$notify.error(res.errorMsg)
          }
        })
        this.deleteDialogIsShown=false
      },
      handleCurrentChange(page){
        let params=new URLSearchParams();
        params.append("currentPage",page)
        params.append("pageSize",this.pageSize);
        params.append('likeNameOne',this.likeNameOne);
        params.append('songListId',this.songListid)
        songforSongListGetPage(params).then(res=>{
          if(res.success){
            this.songListSelect=res.data;
            this.$notify.success("查询成功")
          }else {
            this.$notify.error(res.errorMsg)
          }
        })
      },
      deleteSelect(){
        this.deleteDialogIsShownIndexs=true
      },
      querySearch(queryString,cb){
        let params=new URLSearchParams();
        params.append("name",queryString);
        params.append('songListid',this.songListid)
        let result=[];
        songforSongListGetLikeName(params).then(res=>{
          res.data.map(each=>{
            result.push({'value' : each})
          })
        })
        cb(result)
      },
      handleSelect(item){
        this.likeNameOne=item.value
        this.handleCurrentChange(1);
      },
      restore(){

      },
      handleSelectChange(val){
        this.deleteIds=[]
        val.forEach(each=>{
          this.deleteIds.push(each.id)
        })
      },
      getUrl(url){
        return this.$store.state.httpFileUrl +url
      },
    },
    created() {
      this.songListid=this.$route.query.id
      this.songListTitle=this.$route.query.name
      songforSongListGetAll({'songListId':this.songListid}).then(res=>{
        // this.songListSelect=res.data
        this.total=res.data.length
        this.currentPage=1;
      })
      this.handleCurrentChange(1);
    },
    computed:{
      ...mapGetters([
        'url',
        'isPlay'
      ])
    },

  }
</script>

<style scoped>
  .hand-box{
    /*margin-bottom: 5px;*/
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
    margin-top: 5px;
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
  .singer{
    cursor:pointer;
  }
  .hand-box{
    margin-bottom: 20px;
    /*margin-top: 0px;*/
  }
  .updateBtn{
    margin-bottom:8px
  }
  .play{
    position: absolute;
    z-index: 100;
    width: 80px;
    height: 80px;
    display: flex;
    align-items:center;
    justify-content: center;
    cursor: pointer;
    top:10px;
    left: 18px;
  }
  .icon{
    width:2em;
    height:2em;
    color: #00de46;
    fill: currentColor;
    overflow: hidden;
  }
  .infinite-list{
    height:100px;
    overflow-y:auto;
    padding: 10px;
  }
</style>
