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
        <el-button @click="dialogIsShown=true" type="primary" size="mini">添加用户</el-button>
        <el-button @click="restore" type="primary" size="mini">恢复</el-button>
      </div>
    </div>
    <el-table size="mini" border style="width:100%" height="700px" :data="consumerListSelect" @selection-change="handleSelectChange">
      <el-table-column width="50" align="center" type="selection">
      </el-table-column>
      <el-table-column label="用户图片" width="110" align="center">
        <template slot-scope="scope">
          <div class="consumer-img">
            <img :src="getUrl(scope.row.avator)" style="width:100%">
          </div>
          <el-upload :action="uploadUrl(scope.row.id)" :show-file-list="false" :on-success="handleAvatorSuccess" :before-upload="beforeAvatorUpload">
            <el-button size="mini">更新图片</el-button>
          </el-upload>
        </template>
      </el-table-column>
      <el-table-column prop="username" label="用户名" width="90" align="center"></el-table-column>
      <el-table-column label="性别" width="50" align="center">
        <template slot-scope="scope">
          {{selectSex(scope.row.sex)}}
        </template>
      </el-table-column>
      <el-table-column prop="phoneNum" label="用户手机号" width="100" align="center"></el-table-column>
      <el-table-column prop="email" label="用户电子邮箱" width="130" align="center"></el-table-column>
      <el-table-column label="用户生日" align="center" width="90">
        <template  slot-scope="scope">
          {{scope.row.birth.slice(0,10)}}
        </template>
      </el-table-column>
      <el-table-column prop="location" label="用户地区" align="center" width="150"></el-table-column>
      <el-table-column prop="introduction" label="用户签名" align="center" type="textarea"></el-table-column>
      <el-table-column label="操作" width="100" align="center">
        <template slot-scope="scope">
          <el-button @click="getShowConsumer(scope.row)" size="mini" class="updateBtn">修改</el-button>
          <el-button @click="deleteConsumer(scope.row.id)" size="mini" class="deleteBtn">删除</el-button>
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
    <el-dialog title='添加用户' :visible.sync="dialogIsShown" width="400px" center>
      <el-form :model="consumerInput"
        ref="consumerInput" label-width="80px" :rules="rules">
        <el-form-item label="用户姓名" prop="username">
          <el-input v-model="consumerInput.username" placeholder="用户名字" size="mini"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="consumerInput.password" placeholder="用户密码" size="mini"></el-input>
        </el-form-item>
        <el-form-item label="性别" size="mini">
          <el-radio-group v-model="consumerInput.sex">
            <el-radio :label="0">女</el-radio>
            <el-radio :label="1">男</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机号" prop="phoneNum">
          <el-input placeholder="请输入手机号" v-model="consumerInput.phoneNum"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input placeholder="请输入邮箱" v-model="consumerInput.email"></el-input>
        </el-form-item>
        <el-form-item label="生日" size="mini" prop="birth">
          <el-date-picker type="date" placeholder="填写日期" v-model="consumerInput.birth" style="width: 100%"
                          value-format="yyyy-MM-dd"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="地区" prop="location">
          <el-cascader
            size="large"
            :options="options"
            v-model="consumerInput.location"
          >
          </el-cascader>
        </el-form-item>
        <el-form-item label="签名" prop="introduction">
          <el-input placeholder="请输入签名" v-model="consumerInput.introduction" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
      <el-button size="mini" @click="submit">确认</el-button>
      <el-button size="mini" @click="dialogIsShown=false">取消</el-button>
    </span>
    </el-dialog>
    <!--用户修改-->
    <el-dialog title='用户修改' :visible.sync="updateDialogIsShown" width="400px" center>
      <el-form :model="consumerUpdate" :rules="rules"
        ref="consumerUpdate" label-width="80px">
        <el-form-item label="用户姓名" prop="username">
          <el-input v-model="consumerUpdate.username" placeholder="请输入新用户名" size="mini"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="consumerUpdate.password" placeholder="用户密码" size="mini"></el-input>
        </el-form-item>
        <el-form-item label="性别" size="mini">
          <el-radio-group v-model="consumerUpdate.sex">
            <el-radio :label="0">女</el-radio>
            <el-radio :label="1">男</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机号" prop="phoneNum">
          <el-input placeholder="请输入新手机号" v-model="consumerUpdate.phoneNum"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input placeholder="请输入邮箱" v-model="consumerUpdate.email"></el-input>
        </el-form-item>
        <el-form-item label="生日" size="mini">
          <el-date-picker type="date" placeholder="填写日期" v-model="consumerUpdate.birth" style="width: 100%" value-format="yyyy-MM-dd"></el-date-picker>
        </el-form-item>
        <el-form-item label="地区" prop="location">
          <el-input placeholder="请输入地区" v-model="consumerUpdate.location"></el-input>
        </el-form-item>
        <el-form-item label="签名" prop="introduction">
          <el-input placeholder="请输入签名" v-model="consumerUpdate.introduction" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
      <el-button size="mini" @click="updateConsumer">确认</el-button>
      <el-button size="mini" @click="updateDialogIsShown=false">取消</el-button>
    </span>
    </el-dialog >
    <!--  删除用户-->
    <el-dialog title='用户删除' :visible.sync="deleteDialogIsShown" width="400px" center>
    <span slot="footer">
      <el-button size="mini" @click="deleteConsumerById">确认</el-button>
    <el-button size="mini" @click="deleteDialogIsShown=false">取消</el-button>
    </span>
    </el-dialog>
    <el-dialog title="确认删除选中全部" :visible.sync="deleteDialogIsShownIndexs" width="400px" center>
    <span slot="footer">
      <el-button size="mini" @click="deleteConsumerByIndexs">确认？</el-button>
    <el-button size="mini" @click="deleteDialogIsShownIndexs=false">取消</el-button>
    </span>
    </el-dialog>
  </div>
</template>

<script>
  import {consumerListGetAll,consumerListGetPage,consumerGetLikeName,consumerInsert,consumerUpdate
  ,consumerDelete,consumerDeleteIds,consumerRestore} from "../../api";
  import { provinceAndCityData } from 'element-china-area-data'
  import {mixin} from "../../mixins";
  import {CodeToText} from "element-china-area-data/dist/app";
  export default {
    name: "",
    mixins:[mixin],
    data(){
      return{
        deleteDialogIsShownIndexs:false,
        deleteDialogIsShown:false,
        updateDialogIsShown:false,
        dialogIsShown: false,
        options:provinceAndCityData,
        consumerInput:{
          username:'',
          password:"",
          sex:1,
          location:'',
          introduction:'',
          phoneNum:'',
          email:"",
        },
        consumerUpdate:{
          id:-1,
          username:'',
          sex:'',
          birth:'',
          location:'',
          introduction:'',
          phoneNum:'',
          email:"",
        },
        select:[],
        deleteId:-1,
        consumerList:[],
        likeNameOne:'',
        pageSize: 5,
        currentPage:1,
        consumerListSelect:[],
        total:-1,
        selectIndexs:[],
        pic:"",
        rules:{
          username:[
            {required:true,message:'请输入用户名',trigger:['blur','change']}
          ],
          password:[
            {required:true,message:'请输入密码',trigger:'blur'},
          ],
          location:[
            {required:true,message:'请输入地址',trigger:'blur'},
          ],
          introduction:[
            {required:true,message:'请输入签名',trigger:'blur'},
          ],
          phoneNum:[
            {required:true,message:"请输入手机号",trigger:"blur"},
            {pattern:/^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\d{8}$/
              ,message: '手机号格式不正确',trigger:'blur'}
          ],
          email:[
            {required:true,message:"请输入邮箱",trigger:"blur"},
            {pattern:/^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]+$/,message:'邮箱格式不正确',trigger:"blur"}
          ],
        },
      }
    },
    methods:{
      songEdit(id,name){
        this.$router.push({path:'/SongListForConsumer',query:{id,name}})
      },
      deleteSelect(){
        this.deleteDialogIsShownIndexs=true;
      },
      deleteConsumerByIndexs(){
        let indexIds=[]
        this.selectIndexs.forEach(each=>{
          indexIds.push(each.id);
        })
        let params =new URLSearchParams();
        params.append("ids",indexIds);
        consumerDeleteIds(params).then(res=>{
          if(res.success){
            this.$notify.success(res.data);
            this.handleCurrentChange(this.currentPage);
          }else {
            this.$notify.error(res.errorMsg)
          }
        })
        this.deleteDialogIsShownIndexs=false;
      },
      handleSelectChange(val){
        this.selectIndexs=val;
        console.log(this.selectIndexs)
      },
      getShowConsumer(consumer){
        this.consumerUpdate=consumer
        this.updateDialogIsShown=true;
      },
      updateConsumer(){
        consumerUpdate(this.consumerUpdate).then(res=>{
          if(res.success){
            this.$notify.success(res.data);
          }else {
            this.$notify.error(res.errorMsg)
          }
        })
        this.updateDialogIsShown=false;
      },
      deleteConsumer(id){
        this.deleteId=id
        this.deleteDialogIsShown=true;
      },
      deleteConsumerById(){
        consumerDelete({"id":this.deleteId}).then(res=>{
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
        this.$refs['consumerInput'].validate(valid=>{
          if(valid){
            if(this.consumerInput.sex===''){
              alert("请选择性别");
              return;
            }
            this.consumerInput.location=CodeToText[this.consumerInput.location[0]]+','+CodeToText[this.consumerInput.location[1]]
            consumerInsert(this.consumerInput).then((res)=>{
              if(res.success){
                this.notify(res.data,'success');
                this.handleCurrentChange(this.currentPage);
              }else {
                this.notify(res.errorMsg,'error');
              }
            }).catch((err)=>{
              console.log(err)
            })
            this.dialogIsShown=false;
          }
        })

      },
      selectSex(sex){
        if(sex===1){
          return '男'
        }else {
          return '女'
        }
      },
      uploadUrl(id){
        return this.$store.state.Host+'/consumer/changeImg?id='+id;
      },
      getAllConsumerList(){
        this.consumerList=[];
        consumerListGetAll().then(res=>{
          if(res.success){
            this.total=res.data;
          }else {
            this.$notify.error("总数查询失败")
          }

        })
      },
      querySearch(queryString,cb){
        let params=new URLSearchParams();
        params.append("name",queryString);
        let result=[];
        consumerGetLikeName(params).then(res=>{
          res.data.map(each=>{
            result.push({'value' : each.username})
          })
        })
        cb(result)
      },
      updateDate(){
        // alert(this.likeNameOne)
        if(this.likeNameOne===''){
          this.getAllConsumerList();
          this.handleCurrentChange(1)
        }else {
          let pa={'value':this.likeNameOne}
          this.handleSelect(pa)
        }
      },
      handleSelect(item){
        this.handleCurrentChange(1);
      },
      handleCurrentChange(val){
        let likeName=this.likeNameOne;
        let pageSize=this.pageSize;
        let params=new URLSearchParams();
        params.append("likeName",likeName);
        params.append("currentPage",val);
        params.append("pageSize",pageSize);
        consumerListGetPage(params).then(res=>{
          if(res.success){
            this.consumerListSelect = res.data.consumers;
            this.total=res.data.count;
          }else {
            this.$notify.error("查询失败")
          }
        })
        // console.log(val);
      },
      handleSizeChange(){

      },
      restore(){
        consumerRestore().then(res=>{
          if(res.success){
            this.handleCurrentChange(1);
          }else {
            this.$notify.error(res.errorMsg)
          }
        })
      }
    },
    created() {
      this.getAllConsumerList();
      this.handleCurrentChange(1)
    }
  }
</script>

<style scoped>
  .hand-box{
    margin-bottom: 20px;
    /*margin-top: 0px;*/
  }
  .consumer-img{
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
