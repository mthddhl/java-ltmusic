<template>
    <div>
      <div class="head">
        <Head></Head>
        <size></size>
      </div>
      <div class="content-box" :class="{'content-collapse': collapse}">
        <router-view></router-view>
      </div>
    </div>
</template>

<script>
  import Head from "./Head";
  import Size from "./Size";
  import bus from "../assets/js/bus";
  import {consumerIsLogin} from "../api";

  export default {
    name: "",
    components:{
      Head,Size
    },
    data(){
      return{
        collapse:false,
        admin:{}
      }
    },
    created() {
      let hasAuti=false
      if(localStorage.getItem("token")!==null &&localStorage.getItem("token")!==''){
        if(localStorage.getItem("consumer")!==null && localStorage.getItem("consumer")!==''){
          let consumer=JSON.parse(localStorage.getItem("consumer"));
          consumer.roles.forEach(a=>{
            if(a==="admin"){
              hasAuti=true
            }
          })
        }else {
          this.$router.push('/')
        }
      }else {
        this.$router.push('/')
      }
      if(!hasAuti){
        this.$notify.error("您没有权限")
        this.$router.push('/')
      }
      consumerIsLogin().then(res=>{
        if(!res.success){
          this.$router.push('/')
        }
      })


      bus.$on('collapse',msg=>{
        this.collapse=msg;
      })
    }
  }
</script>

<style scoped>

</style>
