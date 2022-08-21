<template>
    <div class="login-wrap">
        <div class="login">
<!--/*            <span style="color: #f60808">123{{error}}</span>*/-->
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" class="form">
                <div style="font-size: 30px;color: #000000;text-align: center">登录</div>
                <br>
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
    import {getLoginStatus} from '@/ajax/getAndPost'
    import jwtDecode from "jwt-decode";
    export default {
        name: "",
        data(){
            return{
                error: "",
                ruleForm:{
                    username:'zsanli',
                    password:'123',
                },
                rules:{
                    username: [
                        {required:true,message:"请输入用户名",trigger:"blur"}
                    ],
                    password: [
                        {required:true,message:"请输入密码",trigger:"blur"}
                    ]
                },
                consumer:{}
            }
        },
        methods:{
            submitForm(){
                this.$refs['ruleForm'].validate((vaild)=>{
                    if(vaild){
                        let params =new URLSearchParams();
                        params.append("username",this.ruleForm.username)
                        params.append("password",this.ruleForm.password)
                        getLoginStatus(params).then((res)=>{
                            if(res.success){
                                localStorage.setItem("token",res.data)
                                let consumer=jwtDecode(res.data)
                                console.log(consumer)
                                if(new Date().getTime()<new Date(consumer.vipExpireTime).getTime()){
                                    this.$store.state.isVip=true
                                }else {
                                    this.$store.state.isVip=false
                                }
                                localStorage.setItem("consumer",JSON.stringify(consumer))
                                this.$router.push("/info")
                            }else {
                                this.error=res.errorMsg
                            }
                        })
                    }
                })

            }
        },
        created() {
            this.ruleForm.username=this.$route.query.username;
            this.ruleForm.password=this.$route.query.password
        }
    }
</script>

<style scoped>
    .login-wrap {
        position: absolute;
        background-image: url("../img/img.png");
        background-size: 100%,100%;
        /*height: 753px;*/
        width: 100%;
        height: 100%;
    }
    .login{
        position: absolute;
        left: 34%;
        top:35%;
        width: 400px;
        height: 230px;
        /*padding: 40px;*/
        border-radius: 5px;
        background: #ffffff;
    }
    .loginBtn{
        text-align: center;
        width: 100%;
        height:36px;
    }
    .form{
        position: absolute;
        width: 300px;
        margin-left: 50px;
        margin-top: 30px;
    }
</style>