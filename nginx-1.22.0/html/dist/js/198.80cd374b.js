"use strict";(self["webpackChunkcli"]=self["webpackChunkcli"]||[]).push([[198],{8198:function(e,r,l){l.r(r),l.d(r,{default:function(){return V}});var o=l(6252);const u=e=>((0,o.dD)("data-v-0fc3cf06"),e=e(),(0,o.Cn)(),e),a={class:"register1"},t={class:"register"},m=u((()=>(0,o._)("br",null,null,-1))),s=u((()=>(0,o._)("div",{style:{"font-size":"30px",color:"#000000","text-align":"center"}},"注册",-1))),i=u((()=>(0,o._)("br",null,null,-1))),d={class:"registerBtn",style:{"margin-left":"160px",height:"50px"}},n=(0,o.Uk)("注册"),p=u((()=>(0,o._)("br",null,null,-1)));function g(e,r,l,u,g,c){const h=(0,o.up)("el-input"),b=(0,o.up)("el-form-item"),F=(0,o.up)("el-radio"),f=(0,o.up)("el-radio-group"),w=(0,o.up)("el-date-picker"),V=(0,o.up)("el-cascader"),W=(0,o.up)("el-form"),_=(0,o.up)("el-button");return(0,o.wg)(),(0,o.iD)("div",a,[(0,o._)("div",t,[(0,o.Wm)(W,{model:g.ruleForm,rules:g.rules,ref:"ruleForm","label-width":"80px",style:{width:"300px","margin-left":"auto","margin-right":"auto"}},{default:(0,o.w5)((()=>[m,s,i,(0,o.Wm)(b,{label:"用户名",prop:"username"},{default:(0,o.w5)((()=>[(0,o.Wm)(h,{modelValue:g.ruleForm.username,"onUpdate:modelValue":r[0]||(r[0]=e=>g.ruleForm.username=e)},null,8,["modelValue"])])),_:1}),(0,o.Wm)(b,{label:"密码",prop:"password"},{default:(0,o.w5)((()=>[(0,o.Wm)(h,{modelValue:g.ruleForm.password,"onUpdate:modelValue":r[1]||(r[1]=e=>g.ruleForm.password=e),type:"password"},null,8,["modelValue"])])),_:1}),(0,o.Wm)(b,{label:"性别"},{default:(0,o.w5)((()=>[(0,o.Wm)(f,{modelValue:g.ruleForm.sex,"onUpdate:modelValue":r[2]||(r[2]=e=>g.ruleForm.sex=e)},{default:(0,o.w5)((()=>[(0,o.Wm)(F,{label:"男"}),(0,o.Wm)(F,{label:"女"})])),_:1},8,["modelValue"])])),_:1}),(0,o.Wm)(b,{label:"邮箱",prop:"email"},{default:(0,o.w5)((()=>[(0,o.Wm)(h,{modelValue:g.ruleForm.email,"onUpdate:modelValue":r[3]||(r[3]=e=>g.ruleForm.email=e)},null,8,["modelValue"])])),_:1}),(0,o.Wm)(b,{label:"手机号",prop:"phoneNum"},{default:(0,o.w5)((()=>[(0,o.Wm)(h,{modelValue:g.ruleForm.phoneNum,"onUpdate:modelValue":r[4]||(r[4]=e=>g.ruleForm.phoneNum=e)},null,8,["modelValue"])])),_:1}),(0,o.Wm)(b,{label:"生日",prop:"birth"},{default:(0,o.w5)((()=>[(0,o.Wm)(w,{modelValue:g.ruleForm.birth,"onUpdate:modelValue":r[5]||(r[5]=e=>g.ruleForm.birth=e),type:"date",placeholder:"Pick a date",style:{width:"100%"}},null,8,["modelValue"])])),_:1}),(0,o.Wm)(b,{label:"地区",prop:"location"},{default:(0,o.w5)((()=>[(0,o.Wm)(V,{size:"large",options:g.options,modelValue:g.ruleForm.location,"onUpdate:modelValue":r[6]||(r[6]=e=>g.ruleForm.location=e)},null,8,["options","modelValue"])])),_:1})])),_:1},8,["model","rules"]),(0,o._)("div",d,[(0,o.Wm)(_,{type:"primary",onClick:c.register},{default:(0,o.w5)((()=>[n])),_:1},8,["onClick"]),p])])])}var c=l(4204),h=l(1626),b=l(5244),F={name:"",data(){return{error:"",options:c.provinceAndCityData,ruleForm:{username:"",password:"",sex:"男",email:"dsw@qq.com",phoneNum:"17866613112",birth:"",location:""},rules:{username:[{required:!0,message:"请输入用户名",trigger:"blur"},{max:12,message:"用户名不能超过12字符",trigger:"blur"},{min:6,message:"用户名不能少于6字符",trigger:"blur"}],password:[{required:!0,message:"请输入密码",trigger:"blur"},{max:12,message:"密码不能超过12字符",trigger:"blur"},{min:6,message:"密码不能少于6字符",trigger:"blur"}],email:[{required:!0,message:"请输入邮箱",trigger:"blur"},{pattern:/^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]+$/,message:"邮箱格式不正确",trigger:"blur"}],phoneNum:[{required:!0,message:"请输入手机号",trigger:"blur"},{pattern:/^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\d{8}$/,message:"手机号格式不正确",trigger:"blur"}],birth:[{required:!0,message:"请选择生日",trigger:"blur"}],location:[{required:!0,message:"请选择地区",trigger:"blur"}]},consumer:{}}},methods:{register(){this.$refs["ruleForm"].validate((e=>{if(e){let e=this.ruleForm.birth,r=e.getFullYear(),l=e.getMonth()+1<10?"0"+(e.getMonth()+1):e.getMonth()+1,o=e.getDate()<10?"0"+e.getDate():e.getDate();this.ruleForm.birth=r+"-"+l+"-"+o,this.ruleForm.sex="男"===this.ruleForm.sex?1:0,this.ruleForm.location=c.CodeToText[this.ruleForm.location[0]]+","+c.CodeToText[this.ruleForm.location[1]],(0,h.YT)(this.ruleForm).then((e=>{e.success&&(b.z8.success("注册成功"),this.$router.push({path:"/login",query:{username:this.ruleForm.username,password:this.ruleForm.password}}))}))}else b.z8.error("不合符规则")}))}}},f=l(3744);const w=(0,f.Z)(F,[["render",g],["__scopeId","data-v-0fc3cf06"]]);var V=w}}]);
//# sourceMappingURL=198.80cd374b.js.map