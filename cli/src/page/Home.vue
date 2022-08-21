<template>
    <Head></Head>
    <div class="content">
        <router-view></router-view>
    </div>
    <musicPlayer></musicPlayer>
</template>

<script>
    import Head from './components/Head'
    import musicPlayer from './music/musicPlay'
    import {consumerIsLogin} from '@/ajax/getAndPost'
    import {ElMessage} from "element-plus";
    export default {
        name: "Home",
        data(){
            return{
                show:true
            }
        },
        components:{
            Head,musicPlayer
        },
        computed:{
            key(){
                return this.$route.name? this.$route.name+ +new Date():this.$route+ +new Date();
            }
        },
        created() {
            // if(localStorage.getItem("token")!==null && localStorage.getItem("token")!==''){
            //     consumerIsLogin().then(res=>{
            //         if(!res.success){
            //             localStorage.setItem("token",'')
            //             localStorage.setItem("consumer",'')
            //             ElMessage.error(res.data)
            //         }
            //     })
            // }
        }
    }
</script>

<style scoped>
    .content{
        margin-top: 5px;
        width: 1200px;
        margin-left: auto;
        margin-right: auto;
    }
    .footer{
        height:100px;
        width:100%;
        background-color: #2aa3ef;
        display: block;
        position: fixed;
        top: 70%;
        margin-left: 0px;
        z-index: 9;
        color: black;
    }
</style>