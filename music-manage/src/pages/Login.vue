<template>
  <div class="login-wrap">
    <div class="ms-title">music 后台管理登录</div>
    <div class="login">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm">
        <el-form-item prop="username">
          <el-input v-model="ruleForm.username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" v-model="ruleForm.password" placeholder="密码"></el-input>
        </el-form-item>
        <div class="loginBtn">
          <el-button type="primary" @click="submitForm">登录</el-button>
        </div>
      </el-form>
    </div>
  </div>

</template>
<script>
  import {mixin} from '../mixins/index'
  import {getLoginStatus} from '../api/index'
  import jwtDecode from "jwt-decode";
  export default {
    mixins:[mixin],
    data(){
      return{
        ruleForm:{
          username:'',
          password:'',
        },
        rules:{
          username: [
            {required:true,message:"请输入用户名",trigger:"blur"}
          ],
          password: [
            {required:true,message:"请输入密码",trigger:"blur"}
          ]
        }
      }
    },
    methods:{
      submitForm(){
        let hasAuti=false;
        let params =new URLSearchParams();
        params.append("username",this.ruleForm.username)
        params.append("password",this.ruleForm.password)
        getLoginStatus(params).then((res)=>{
            if(res.success){
              localStorage.setItem("token",res.data)
              let consumer=jwtDecode(res.data)
              console.log(consumer)
              localStorage.setItem("consumer",JSON.stringify(consumer))
              consumer.roles.forEach(a=>{
                if(a==='admin'){
                  hasAuti=true
                  this.$router.push("/home");
                  return
                }

              })
              if(!hasAuti) {
                this.$notify.error("您没有权限登录后台")
              }
            }else {
              this.$notify.error(res.errorMsg)
            }
        })
      }
    }
  }
</script>
<style scoped>
.login-wrap {
  position: relative;
  background: url("../assets/img/background.jpg") fixed center;
  background-size: cover;
  width: 100%;
  height: 100%;
}
.ms-title {
  position: absolute;
  top: 50%;
  width: 100%;
  margin-top: -230px;
  text-align: center;
  font-size: 30px;
  font-weight: 600;
  color: #fff;
}
.login{
  position: absolute;
  left: 50%;
  top: 50%;
  width: 300px;
  height: 160px;
  margin-left: -190px;
  margin-top: -150px;
  padding: 40px;
  border-radius: 5px;
  background: #ffffff;
}
.loginBtn{
  text-align: center;
  width: 100%;
  height:36px;
}
</style>
