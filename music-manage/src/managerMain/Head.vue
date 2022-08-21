<template>
  <div class="head">
<!--    头部元素-->
    <div class="collapse-btn" @click="collapseChange">
      <i class="el-icon-menu"></i>
    </div>
    <div class="logo">
      music后台管理
    </div>
    <div class="header-right">
      <div class="btn-fullscreen" @click="handleFullScreen">
        <el-tooltip :content="fullscreen? '全屏显示':'非全屏'">
          <i class="el-icon-rank"></i>
        </el-tooltip>
      </div>
      <el-dropdown class="user-name" trigger="click" @command="handleCommand">
        <div class="user-avator">
          <img :src="this.$store.state.httpFileUrl+consumer.avator" alt="无法显示">
        </div>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="logout">退出登录</el-dropdown-item>
<!--          <el-dropdown-item command="remark">重新登录</el-dropdown-item>-->
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
  import bus from "../assets/js/bus";
  import {consumerLogout} from "../api";

  export default {
    name: "",
    data(){
      return{
        collapse:true,
        fullscreen:false,
        // username:''
        consumer:{}
      }
    },
    created() {
      console.log(111)
      console.log(localStorage.getItem("consumer"));
      this.consumer = JSON.parse(localStorage.getItem("consumer"));
    },
    computed:{
      username(){
        return localStorage.getItem('username')
      }
    },
    methods: {
      collapseChange() {
        bus.$emit('collapse', this.collapse)
        this.collapse = !this.collapse
      },
      handleFullScreen() {
        if (this.fullscreen) {
          document.exitFullscreen()
        } else {
          document.documentElement.requestFullscreen();
        }
        this.fullscreen = !this.fullscreen
      },
      handleCommand(command) {
        if (command === 'logout') {
          try {
            if (localStorage.getItem("token") !== null && localStorage.getItem("token") !== "") {
              consumerLogout().then(res => {
                if (res.success) {
                  this.$notify.success("退出成功");
                  localStorage.removeItem("token")
                  localStorage.removeItem("consumer")
                  location.reload()
                }
              })
            }
          } catch (e) {
            this.$notify.error(e)
          }
          localStorage.removeItem("token")
          localStorage.removeItem("consumer")
          this.$router.push("/")
        }
      },
    }
  }
</script>

<style scoped>
.head{
  position: absolute;
  background-color: black;
  box-sizing: border-box;
  width: 100%;
  height: 70px;
  font-size: 22px;
  color: white;
}
.collapse-btn{
  float: left;
  padding: 0 21px;
  cursor: pointer;
  line-height: 70px;
}
.head .logo{
  float: left;
  line-height: 70px;
}
.header-right{
  float: right;
  padding-right: 50px;
  display: flex;
  height: 70px;
  align-items:center;
}
.btn-fullscreen{
  transform: rotate(45deg);
  margin-right: 5px;
  font-size:24px;
}
.user-avator{
  margin-left: 15px;
}
.user-avator img{
  display: block;
  width: 40px;
  height: 40px;
  border-radius:50%;
}

.user-name{
  margin-left:10px;
}
.el-dropdown-link{
  color: #dddddd;
  cursor: pointer;
}
</style>
