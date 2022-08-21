<template>
    <div class="head">
        <el-menu
                :default-active="activeIndex"
                class="el-menu-demo"
                mode="horizontal"
                router
                @select="handleSelectRouter"
        >
            <div style="width: 200px"></div>
            <el-menu-item v-for="item in router" :index="item.path" :key="item.path">{{item.content}}</el-menu-item>
        </el-menu>
        <div class="search">
            <el-autocomplete
                v-model="state2"
                :fetch-suggestions="querySearch"
                :trigger-on-focus="false"
                clearable
                class="inline-input w-50"
                placeholder="歌曲/歌手/歌单"
                @select="handleSelect"
            />
            <el-icon class="searchIcon" @click="searchClick"><Search /></el-icon>
        </div>
        <div class="logo" @click="this.$router.push('/info');">
            <el-icon style="transform:scale(3);margin-left: 30px"><Headset /></el-icon>
            <span class="homeTitle">聆听音乐</span>
        </div>
        <div class="avatar block">
            <div v-if="consumer.login">
                <el-dropdown>
                    <div>
                        <el-avatar :size="40" :src="circleUrl" />
                    </div>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item @click="logout(consumer.consumer.id)">登出</el-dropdown-item>
                            <el-dropdown-item @click="getConInfo(consumer.consumer.id)">个人信息</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </div>
            <div v-if="!consumer.login">
                <el-button @click="this.$router.push('/login')">登录</el-button>
                <el-button @click="this.$router.push('/register')">注册</el-button>
            </div>
        </div>
    </div>
</template>

<script>
    import {consumerIsLogin, consumerLogout, searchGetAll} from '@/ajax/getAndPost'
    import jwtDecode from "jwt-decode";
    import {ElMessage} from "element-plus";
    export default {
        name: "",
        data() {
            return {
                consumer:{
                    login:false,
                    consumer:{
                        id:null,
                        avator:null,
                    },
                },
                circleUrl: '',
                state2:"",
                router:[
                    {path:'/info',content:'首页'},
                    {path:'/info/songList',content:'歌单'},
                    {path:'/info/singer',content:'歌手'},
                    {path:'/info/MV',content:"精彩MV"},
                    {path:'/info/VIP',content:'我的会员'},
                ]
            };
        },
        methods: {
            searchClick(){
                let text={'value':this.state2}
                this.handleSelect(text)
            },
            handleSelectRouter(key, keyPath) {
                console.log(key, keyPath);
            },
            handleSelect(input){
                let text=input.value
                this.$router.push({path:'/search',query:{text}})
            },
            querySearch(queryString,cb){
                let result=[]
                searchGetAll({"text":queryString}).then(res=>{
                    if(res.success){
                        res.data.map(each=>{
                            result.push({'value' : each})
                        })
                    }
                })
                setTimeout(()=>{
                    cb(result);
                },200)

            },
            getConInfo(){
                let id=this.consumer.consumer.id
                this.$router.push({path:'/consumer',query:{id}})
            },
            logout(){
                try{
                    if(localStorage.getItem("token")!==null && localStorage.getItem("token")!==""){
                        consumerLogout().then(res=>{
                            if(res.success){
                                ElMessage.success("退出成功");
                                localStorage.removeItem("token")
                                localStorage.removeItem("consumer")
                                this.$store.state.isVip=false
                                location.reload()
                            }
                        })
                    }
                }catch (e) {
                    ElMessage.error(e)
                }
                localStorage.removeItem("token")
                localStorage.removeItem("consumer")
                this.consumer.login=false;
            },
        },
        computed:{
            activeIndex(){
                return this.$route.path.replace('//','')
            }
        },
        created() {
            if(localStorage.getItem("token")!==null && localStorage.getItem("token")!==''){
                consumerIsLogin().then(res=>{
                    if(res.success){
                        localStorage.setItem("token",res.data)
                        let consumer=jwtDecode(res.data)
                        if(new Date().getTime()<new Date(consumer.vipExpireTime).getTime()){
                            this.$store.state.isVip=true
                        }else {
                            this.$store.state.isVip=false
                        }
                        localStorage.setItem("consumer",JSON.stringify(consumer));
                    }
                })
            }
            if(localStorage.getItem("token")!==null &&localStorage.getItem("token")!==''){
                if(localStorage.getItem("consumer")!==null && localStorage.getItem("consumer")!==''){
                    // let consumerAvator=JSON.parse(localStorage.getItem("consumer")).avator;
                    let consumer=JSON.parse(localStorage.getItem("consumer"));
                    this.consumer.login=true
                    this.$store.state.isVip=false
                    this.circleUrl=this.$store.state.httpFileUrl+consumer.avator
                    this.consumer.consumer.id=consumer.id
                }
            }
        }
    }
</script>

<style scoped>
    .logo{
        cursor:pointer;
        position: absolute;
        margin-top: -40px;
    }
    .head{
        left: 0;
        /*position: absolute;*/
        /*background-color: #230307;*/
        /*box-sizing: border-box;*/
        width: 100%;
        /*height: 70px;*/
        /*font-size: 22px;*/
        /*color: #1e0202;*/
        margin-top: -8px;
    }
    .avatar{
        position:absolute;
        margin-top:-45px;
        right: 60px;
    }
    .example-showcase .el-dropdown-link {
        cursor: pointer;
        color: var(--el-color-primary);
        display: flex;
        align-items: center;
    }
    .search{
        position:absolute;
        margin-top:-45px;
        right: 260px;
        width: 180px;
    }
    .searchIcon{
        background-color: #2aa3ef;
        height:22px;
        width: 30px;
        top: -27px;
        transform: scale(1.4);
        float: left;
        left: 180px;
        border-radius: 2px;
        cursor: pointer;
    }
    .homeTitle{
        margin-left: 25px;
        font-family: 方正粗黑宋简体;
        font-size: 22px;
    }
</style>