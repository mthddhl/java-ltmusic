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
      <el-button @click="dialogIsShown=true" type="primary" size="mini">添加歌手</el-button>
      <el-button @click="restore" type="primary" size="mini">恢复</el-button>
    </div>
  </div>
  <el-table size="mini" border style="width:100%" height="1200px" :data="singerListSelect" @selection-change="handleSelectChange">
    <el-table-column width="50" align="center" type="selection">
    </el-table-column>
    <el-table-column label="歌手图片" width="110" align="center">
      <template slot-scope="scope">
        <div class="singer-img">
          <img :src="getUrl(scope.row.pic)" style="width:100%">
        </div>
      </template>
    </el-table-column>
    <el-table-column prop="name" label="歌手姓名" width="90" align="center"></el-table-column>
    <el-table-column label="性别" width="50" align="center">
      <template slot-scope="scope">
        {{selectSex(scope.row.sex)}}
      </template>
    </el-table-column>
    <el-table-column prop="birth" label="歌手生日" width="90" align="center"></el-table-column>
    <el-table-column prop="location" label="歌手出生地" width="90" align="center"></el-table-column>
    <el-table-column prop="introduction" label="歌手简介" align="center"></el-table-column>
    <el-table-column label="歌曲管理" align="center" width="150">
      <template slot-scope="scope">
        <el-button size="mini" @click="songEdit(scope.row.id,scope.row.name)">歌曲管理</el-button>
        <el-button size="mini" @click="toCarousel(scope.row)" style="margin-top: 15px;margin-left: -6px">添加到走马灯</el-button>
      </template>
    </el-table-column>
    <el-table-column label="操作" width="100" align="center">
      <template slot-scope="scope">
        <el-button @click="getShowSinger(scope.row)" size="mini" class="updateBtn">修改</el-button>
        <el-button @click="deleteSinger(scope.row.id,scope.$index)" size="mini" class="deleteBtn">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <div class="page">
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page.sync="currentPage"
      :page-size="pageSize"
      layout="prev, pager, next"
      :total="total">
    </el-pagination>
  </div>
  <el-dialog title='添加歌手' :visible.sync="dialogIsShown" width="400px" center>
    <el-form
      ref="singerInput" label-width="80px">
      <el-form-item label="歌手姓名">
        <el-input v-model="singerInput.name" placeholder="歌手名字" size="mini"></el-input>
      </el-form-item>
      <el-form-item label="性别" size="mini">
        <el-radio-group v-model="singerInput.sex">
          <el-radio :label="0">女</el-radio>
          <el-radio :label="1">男</el-radio>
          <el-radio :label="2">组合</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="生日/成立日期" size="mini">
        <el-date-picker type="date" placeholder="填写日期" v-model="singerInput.birth" style="width: 100%"></el-date-picker>
      </el-form-item>
      <el-form-item label="地区">
        <el-input placeholder="请输入地区" v-model="singerInput.location"></el-input>
      </el-form-item>
      <el-form-item label="简介">
        <el-input placeholder="请输入简介" v-model="singerInput.introduction" type="textarea"></el-input>
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
      <el-button size="mini" @click="submit">确认</el-button>
      <el-button size="mini" @click="dialogIsShown=false">取消</el-button>
    </span>
  </el-dialog>
<!--歌手修改-->
  <el-dialog title='歌手修改' :visible.sync="updateDialogIsShown" width="400px" center>
    <el-form
      ref="singerInput" label-width="80px">
      <el-form-item label="歌手姓名">
        <el-input v-model="singerUpdate.name" placeholder="歌手名字" size="mini"></el-input>
      </el-form-item>
      <el-form-item label="性别" size="mini">
        <el-radio-group v-model="singerUpdate.sex">
          <el-radio :label="0">女</el-radio>
          <el-radio :label="1">男</el-radio>
          <el-radio :label="2">组合</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="生日/成立日期" size="mini">
        <el-date-picker type="date" placeholder="填写日期" v-model="singerUpdate.birth" style="width: 100%"></el-date-picker>
      </el-form-item>
      <el-form-item label="地区">
        <el-input placeholder="请输入地区" v-model="singerUpdate.location"></el-input>
      </el-form-item>
      <el-form-item label="简介">
        <el-input placeholder="请输入简介" v-model="singerUpdate.introduction" type="textarea"></el-input>
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
      <el-button size="mini" @click="updateSinger">确认</el-button>
      <el-button size="mini" @click="updateDialogIsShown=false">取消</el-button>
    </span>
  </el-dialog >

<!--  删除歌手-->
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
  import {
    insertSinger,
    getSingerLikeName,
    getSingerByName,
    getSingerPage,
    updateSinger,
    deleteSingerById,
    deleteSingerByIds,
    SingerRestore,
    addSingerToCarousel
  } from "../../api";
  import {getSingerList} from "../../api";
  import {mixin} from "../../mixins";
  export default {
    name: "",
    mixins:[mixin],
    data(){
      return{
        deleteDialogIsShownIndexs:false,
        deleteDialogIsShown:false,
        updateDialogIsShown:false,
        dialogIsShown: false,
        singerInput:{
          name:'刘帅',
          sex:'',
          birth:'',
          location:'zx',
          introduction:'zx'
        },
        singerUpdate:{
          id:-1,
          name:'',
          sex:'',
          birth:'',
          location:'',
          introduction:''
        },
        select:[],
        deleteId:-1,
        singerList:[],
        likeNameOne:'',
        pageSize: 10,
        currentPage:1,
        singerListSelect:[],
        total:-1,
        deleteIndex:-1,
        selectIndexs:[],
        file:null,
      }
    },
    methods:{
      changeFile(file){
        this.file=file
      },
      toCarousel(row){
        let params=new URLSearchParams();
        params.append("type","singer")
        params.append("toId",row.id);
        params.append("title",row.name)
        params.append("pic",row.pic)
        addSingerToCarousel(params).then(res=>{
          if(res.success){
            this.$notify.success(res.data)
          }else {
            this.$notify.error(res.errorMsg)
          }
        })
      },
      songEdit(id,name){
        this.$router.push({path:'/SongListForSinger',query:{id,name}})
      },
      deleteSelect(){
        this.deleteDialogIsShownIndexs=true;
      },
      deleteSingerByIndexs(){
        let indexIds=[]
        this.selectIndexs.forEach(each=>{
          indexIds.push(each.id);
        })
        let params =new URLSearchParams();
        params.append("ids",indexIds);
        deleteSingerByIds(params).then(res=>{
          if(res.success){
            this.$notify.success(res.data);
            this.handleCurrentChange(this.currentPage);
            this.total=this.total-indexIds.length
            this.deleteDialogIsShownIndexs=false;
          }else {
            this.$notify.error(res.errorMsg)
            this.deleteDialogIsShownIndexs=false;
          }
        })
      },
      handleSelectChange(val){
        this.selectIndexs=val;
        console.log(this.selectIndexs)
      },
      getShowSinger(singer){
        this.singerUpdate.id=singer.id;
        this.singerUpdate.name=singer.name;
        this.singerUpdate.birth=singer.birth;
        this.singerUpdate.sex=singer.sex;
        this.singerUpdate.introduction=singer.introduction;
        this.singerUpdate.location=singer.location;
        this.updateDialogIsShown=true;
      },
      updateSinger(){
        let date=this.singerUpdate.birth;
        if(date instanceof Date){
          let year = date.getFullYear();
          let mon = (date.getMonth()+1) < 10 ? "0"+(date.getMonth()+1) : date.getMonth()+1;
          let day = date.getDate()  < 10 ? "0"+(date.getDate()) : date.getDate();
          this.singerInput.birth=year +"-"+ mon +"-"+ day
        }
        let params =new URLSearchParams();
        if(this.file!==null){
          params=new FormData()
          params.append('file',this.file.raw)
        }
        params.append('id',this.singerUpdate.id)
        params.append('name',this.singerUpdate.name);
        params.append('sex',this.singerUpdate.sex);
        params.append('birth',this.singerInput.birth);
        params.append('location',this.singerUpdate.location);
        params.append('introduction',this.singerUpdate.introduction);
        updateSinger(params).then(res=>{
          if(res.success){
            this.$notify.success(res.data);
            this.handleCurrentChange(this.currentPage)
          }else {
            this.$notify.error(res.errorMsg)
          }
        })
        this.updateDialogIsShown=false;
      },
      deleteSinger(id,index){
        this.deleteIndex=index
        this.deleteId=id;
        this.deleteDialogIsShown=true;
      },
      deleteSingerById(){
        let id=this.deleteId;
        let params =new URLSearchParams();
        params.append("id",id);
        deleteSingerById(params).then(res=>{
          if(res.success){
            this.$notify.success(res.data)
            this.handleCurrentChange(this.currentPage)
          }else {
            this.$notify.error(res.errorMsg)
          }
        })
        this.deleteDialogIsShown=false
      },
      submit(){
        let params=new URLSearchParams();
        if(this.file!==null){
          params=new FormData()
          params.append('file',this.file.raw)
        }
        let date=this.singerInput.birth
        if(date instanceof Date){
          let year = date.getFullYear();
          let mon = (date.getMonth()+1) < 10 ? "0"+(date.getMonth()+1) : date.getMonth()+1;
          let day = date.getDate()  < 10 ? "0"+(date.getDate()) : date.getDate();
          this.singerInput.birth=year +"-"+ mon +"-"+ day
        }
        params.append('name',this.singerInput.name);
        params.append('sex',this.singerInput.sex);
        params.append('birth',this.singerInput.birth)
        params.append('location',this.singerInput.location);
        params.append('introduction',this.singerInput.introduction);
        params.append('logicDelete',1);
        insertSinger(params).then((res)=>{
          if(res.success){
            this.notify(res.data,'success');
            this.getAllSingerList();
          }else {
            this.notify(res.errorMsg,'error');
          }
        }).catch((err)=>{
          console.log(err)
        })
        this.dialogIsShown=false;
      },
      selectSex(sex){
        if(sex===1){
          return '男'
        }else if(sex==2) {
          return '组合乐队'
        }else {
          return "女";
        }
      },
      getAllSingerList(){
        getSingerList().then(res=>{
          this.total=res.data
        })
      },
      querySearch(queryString,cb){
        let params=new URLSearchParams();
        params.append("name",queryString);
        let result=[];
        getSingerLikeName(params).then(res=>{
          res.data.map(each=>{
            result.push({'value' : each})
          })
        })
        cb(result)
      },
      updateDate(){
        // alert(this.likeNameOne)
        if(this.likeNameOne===''){
          this.getAllSingerList();
          this.handleCurrentChange(1)
        }else {
          let pa={'value':this.likeNameOne}
          this.handleSelect(pa)
        }
      },
      handleSelect(item){
        let params=new URLSearchParams();
        params.append("name",item.value);
        // console.log(item)
        getSingerByName(params).then(result => {
          this.total=result.data.length;
          this.singerListSelect=result.data;
        })
        this.$notify.success("查询成功")
      },
      handleCurrentChange(val){
        let likeName=this.likeNameOne;
        let pageSize=this.pageSize;
        let params=new URLSearchParams();
        params.append("likeName",likeName);
        params.append("currentPage",val)
        params.append("pageSize",pageSize);
        getSingerPage(params).then(res=>{
          if(res.success){
            if(typeof (likeName)==='undefined' || likeName===''){
              this.singerListSelect = res.data;
            }else {
              this.singerListSelect = res.data.singers;
              this.total=res.data.length;
            }
          }
        })
        // console.log(val);
      },
      handleSizeChange(){

      },
      restore(){
        SingerRestore().then(res=>{
          if(res.success){
            this.getAllSingerList();
            this.total=this.singerList.length;
            this.handleCurrentChange(1);
            this.$notify.success(res.data)
            localStorage.setItem('singerTotal',this.total);
          }else {
            this.$notify.error(res.errorMsg)
          }
        })
      }
    },
    created() {
      this.getAllSingerList();
      this.handleCurrentChange(1)
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
