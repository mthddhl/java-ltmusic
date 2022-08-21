<template>
    <div class="information">
      <el-upload
              :show-file-list="false"
              class="upload-demo"
              :action="changeAvator(consumerId)"
              :before-upload="checkFile"
              :on-success="uploadSuccess"
              :on-error="uploadError"
              limit=1
              v-if="isPerson"
              style="width: 400px;float: left"
      >
          <el-tooltip
                  class="box-item"
                  effect="dark"
                  content="更换头像"
                  placement="left-start"
                  style="cursor: pointer"
                  @click="changeAvator(consumerId)"
          >
              <el-avatar :size="160" :src="this.$store.state.httpFileUrl+consumer.avator" class="img" />
          </el-tooltip>
      </el-upload>

      <el-avatar v-if="!isPerson" :size="160" :src="this.$store.state.httpFileUrl+consumer.avator" class="img" />
      <div class="content">
          <div style="margin-left: 10px"><span style="font-family:微软雅黑;font-size: 22px">{{consumer.username}}</span></div>
          <div style="margin-top:20px;">
              <div class="created">
                  <el-icon style="transform:scale(1.5);float: left"><Location /></el-icon>
                  <el-row>
                      <el-col :span="10"><span style="margin-left: 10px">{{consumer.location}}</span></el-col>
                      <el-col :span="10"><span style=""> {{consumer.birth}}</span></el-col>
                  </el-row>
              </div>
          </div>
          <div style="margin-top: 20px;margin-left: 10px;color: black">
              <el-space :fill="false" wrap :fill-ratio="50" :size="1" style="font-size: 15px">
                  性别:  {{this.consumer.sex===1? '男':'女'}}
              </el-space>
          </div>
          <div style="margin-top: 20px;margin-left: 10px;color: black">
              手机号：{{phoneNum}}
              <br><br>
              邮件：{{email}}
          </div>
          <div style="margin-top: 20px;margin-left: 10px;margin-right: 10px">
              介绍：<span style="word-break:break-all;">{{consumer.introduction}}</span>
          </div>
       </div>
        <div style="width: 200px;float: left;margin-left: -250px;margin-top: 10px;cursor: pointer" @click="showUpdate">
            <el-tooltip
                    class="box-item"
                    effect="dark"
                    content="修改信息"
                    placement="top-start"
                    v-if="isPerson"
            >
                <el-icon style="transform: scale(1.7)"><Edit /></el-icon>
            </el-tooltip>
        </div>
        <el-dialog
                v-model="isUpdate"
                title="Tips"
                width="30%"
        >
            <el-form :model="consumerUpdate" label-width="80px">
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="consumerUpdate.username"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="consumerUpdate.password" type="password"></el-input>
                </el-form-item>
                <el-form-item label="性别">
                    <el-radio-group v-model="consumerUpdate.sex">
                        <el-radio label="女" />
                        <el-radio label='男' />
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="consumerUpdate.email"></el-input>
                </el-form-item>
                <el-form-item label="手机号" prop="phoneNum">
                    <el-input v-model="consumerUpdate.phoneNum"></el-input>
                </el-form-item>
                <el-form-item label="生日" prop="birth">
                    <el-date-picker
                            v-model="consumerUpdate.birth"
                            type="date"
                            placeholder="Pick a date"
                            style="width: 100%"
                    />
                </el-form-item>
                <el-form-item label="地区" prop="location">
                    <el-cascader
                            size="large"
                            :options="options"
                            v-model="consumerUpdate.location"
                    >
                    </el-cascader>
                </el-form-item>
                <el-form-item label="介绍" prop="introduction">
                    <el-input v-model="consumerUpdate.introduction" type="textarea"
                    ></el-input>
                </el-form-item>
            </el-form>
            <div class="registerBtn">
                <el-button type="primary" @click="update">更新</el-button>
                <el-button type="primary" @click="isUpdate=false">取消</el-button>
            </div>
        </el-dialog>
    </div>

</template>

<script>
    import {consumerGetInfo, consumerUpdate} from '@/ajax/getAndPost'
    import {ElMessage} from "element-plus";
    import jwtDecode from "jwt-decode";
    import {CodeToText, provinceAndCityData} from "element-china-area-data";
    export default {
        name: "",
        data(){
            return{
                isUpdate:false,
                isPerson:false,
                options:provinceAndCityData,
                consumerId:-1,
                consumer:{
                    avator:'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
                    phoneNum:'1234567891',
                    email:'132@qq.com',
                    sex:1
                },
                consumerUpdate:{
                    sex:'男',
                }
            }
        },
        computed:{
            phoneNum(){
                return this.consumer.phoneNum.substring(0,3)+'*****'+this.consumer.phoneNum.substring(8)
            },
            email(){
                return '******@'+this.consumer.email.split('@')[1]
            }
        },
        methods:{
            update(){
                let date=this.consumerUpdate.birth
                if(date instanceof Date){
                    let year = date.getFullYear();
                    let mon = (date.getMonth()+1) < 10 ? "0"+(date.getMonth()+1) : date.getMonth()+1;
                    let data = date.getDate()  < 10 ? "0"+(date.getDate()) : date.getDate();
                    this.consumerUpdate.birth=year +"-"+ mon +"-"+ data
                }
                this.consumerUpdate.sex= this.consumerUpdate.sex==='男'? 1:0;
                if(!(typeof this.consumerUpdate.location==='undefined') ){
                    this.consumerUpdate.location=CodeToText[this.consumerUpdate.location[0]]+','+CodeToText[this.consumerUpdate.location[1]]
                }
                consumerUpdate(this.consumerUpdate).then(res=>{
                    if(res.success){
                        consumerGetInfo({"id":this.consumerId}).then(res=>{
                            if(res.success){
                                this.consumer=res.data
                            }
                        })
                        ElMessage.success(res.data)
                    }else {
                        ElMessage.error(res.errorMsg)
                    }
                })
                this.isUpdate=false
            },
            showUpdate(){
                this.consumerUpdate.id=this.consumerId
                this.consumerUpdate.username=this.consumer.username
                this.consumerUpdate.sex=this.consumer.sex===1? '男':'女'
                this.consumerUpdate.email=this.consumer.email
                this.consumerUpdate.phoneNum=this.consumer.phoneNum
                this.consumerUpdate.birth=this.consumer.birth
                this.consumerUpdate.introduction=this.consumer.introduction
                this.isUpdate=true
            },
            changeAvator(id){
                if(localStorage.getItem("token")===null  || localStorage.getItem("token")===''){
                    if(localStorage.getItem("consumer")===null || localStorage.getItem("consumer")===''){
                        ElMessage.error("您还未登录，无法更换")
                        return ""
                    }
                }
                return this.$store.state.httpUrl+'/consumer/changeImg?id='+id
            },
            uploadError(error){
                let json=JSON.parse(error.message)
                ElMessage.error('上传失败,'+json.errorMsg)
                localStorage.setItem("token",'')
                localStorage.setItem("consumer",'')
            },
            uploadSuccess(res,file){
                if(res.success){
                    ElMessage.success('更新成功')
                    this.init()
                    // console.log(this.$store.state.httpUrl+res.data.avator)
                }else {
                    ElMessage.error(res.errorMsg)
                }

            },
            checkFile(file){
                if (!file.type === 'image/jpeg' && !file.type === 'image/png') {
                    this.$notify.error('格式错误')
                    return false
                }
                if (!((file.size / 1024 / 1024) < 2)) {
                    this.$notify.error('文件大小错误')
                    return false
                }
                return true
            },
           init(){
              consumerGetInfo({"id":this.consumerId}).then(res=>{
                  if(res.success){
                      this.consumer=res.data
                  }
              })
              if(localStorage.getItem("token")!==null &&localStorage.getItem("token")!==''){
                  if(localStorage.getItem("consumer")!==null && localStorage.getItem("consumer")!==''){
                      let consumer=JSON.parse(localStorage.getItem("consumer"));
                      if(this.consumerId+""===consumer.id+""){
                          this.isPerson=true;
                      }
                  }
              }
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
            this.consumerId=this.$route.query.id;
            this.init();
        }
    }
</script>

<style scoped>
    .information{
        margin-top: 40px;
        height: 300px;
    }
    .img{
        float:left;
        /*height: 220px;*/
        /*width: 220px;*/
        margin-left:150px;
        /*background-color: #003b60;*/
        background-size: cover;
        border:1px solid #000000;
    }
    .content{
        text-align:left;
        float:left;
        width: 400px;
        height: 280px;
        margin-left: 50px;
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
    .register{
        width: 400px;
        margin-left: 530px;
        margin-top: 30px;
    }
</style>