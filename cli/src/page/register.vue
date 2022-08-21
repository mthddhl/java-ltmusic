<template>
<div class="register1">
    <div class="register">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="80px"
                 style="width: 300px;margin-left: auto;margin-right: auto"><br>
            <div style="font-size: 30px;color: #000000;text-align: center">注册</div>
            <br>
            <el-form-item label="用户名" prop="username">
                <el-input v-model="ruleForm.username"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
                <el-input v-model="ruleForm.password" type="password"></el-input>
            </el-form-item>
            <el-form-item label="性别">
                <el-radio-group v-model="ruleForm.sex">
                    <el-radio label="男" />
                    <el-radio label="女" />
                </el-radio-group>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
                <el-input v-model="ruleForm.email"></el-input>
            </el-form-item>
            <el-form-item label="手机号" prop="phoneNum">
                <el-input v-model="ruleForm.phoneNum"></el-input>
            </el-form-item>
            <el-form-item label="生日" prop="birth">
                <el-date-picker
                        v-model="ruleForm.birth"
                        type="date"
                        placeholder="Pick a date"
                        style="width: 100%"
                />
            </el-form-item>
            <el-form-item label="地区" prop="location">
                <el-cascader
                        size="large"
                        :options="options"
                        v-model="ruleForm.location"
                >
                </el-cascader>
            </el-form-item>
        </el-form>
        <div class="registerBtn" style="margin-left: 160px;height: 50px">
            <el-button type="primary" @click="register">注册</el-button>
            <br>
        </div>
    </div>
</div>
</template>

<script>
    import { provinceAndCityData } from 'element-china-area-data'
    import {consumerInsert} from '@/ajax/getAndPost'
    import {ElMessage} from "element-plus";
    import {CodeToText} from "element-china-area-data/dist/app";
    export default {
        name: "",
        data(){
            return{
                error: "",
                options:provinceAndCityData,
                ruleForm:{
                    username:'',
                    password:'',
                    sex:'男',
                    email:'',
                    phoneNum:'',
                    birth:'',
                    location:"",

                },
                rules:{
                    username: [
                        {required:true,message:"请输入用户名",trigger:"blur"},
                        {max:12,message:"用户名不能超过12字符",trigger:"blur"},
                        {min:6,message:"用户名不能少于6字符",trigger:"blur"}
                    ],
                    password: [
                        {required:true,message:"请输入密码",trigger:"blur"},
                        {max:12,message:"密码不能超过12字符",trigger:"blur"},
                        {min:6,message:"密码不能少于6字符",trigger:"blur"}
                    ],
                    email: [
                        {required:true,message:"请输入邮箱",trigger:"blur"},
                        {pattern:/^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]+$/,message:'邮箱格式不正确',trigger:"blur"}
                    ],
                    phoneNum:[
                        {required:true,message:"请输入手机号",trigger:"blur"},
                        {pattern:/^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\d{8}$/
                        ,message: '手机号格式不正确',trigger:'blur'}
                    ],
                    birth:[
                        {required:true,message:"请选择生日",trigger:"blur"},
                    ],
                    location:[
                        {required:true,message:"请选择地区",trigger:"blur"},
                    ]
                },
                consumer:{}
            }
        },
        methods:{
            register(){
                this.$refs['ruleForm'].validate((y)=>{
                    if(y){
                        let date=this.ruleForm.birth
                        let year = date.getFullYear();
                        let mon = (date.getMonth()+1) < 10 ? "0"+(date.getMonth()+1) : date.getMonth()+1;
                        let data = date.getDate()  < 10 ? "0"+(date.getDate()) : date.getDate();
                        this.ruleForm.birth=year +"-"+ mon +"-"+ data
                        this.ruleForm.sex= this.ruleForm.sex==='男'? 1:0;
                        this.ruleForm.location=CodeToText[this.ruleForm.location[0]]+','+CodeToText[this.ruleForm.location[1]]
                        consumerInsert(this.ruleForm).then(res=>{
                            if(res.success){
                                ElMessage.success('注册成功');
                                this.$router.push({path:'/login',query:{username:this.ruleForm.username,
                                    password:this.ruleForm.password}})
                            }
                        })
                    }else {
                        ElMessage.error("不合符规则")
                    }
                })

            }
        }
    }
</script>

<style scoped>
 .register{
     width: 380px;
     margin-left: 500px;
     margin-top: 150px;
     background-color: #ffffff;
     border-radius: 5px;
 }
 .register1{
     position: absolute;
     background-image: url("../img/preview.jpg");
     background-size: 100%,100%;
     /*height: 753px;*/
     width: 100%;
     height: 100%;
 }
</style>