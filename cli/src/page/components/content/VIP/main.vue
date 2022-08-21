<template>
    <div>
        <el-menu
                :default-active="activeIndex"
                class="el-menu-demo"
                mode="horizontal"
                @select="handleSelect"
        >
            <el-menu-item v-for="i in index" :index="i.index" :key="i.index" class="menu">
                {{i.name}}
            </el-menu-item>
        </el-menu>
        <router-view></router-view>
    </div>
</template>

<script>
    import axios from "axios";

    export default {
        name: "",
        data(){
            return{
                consumerId:3,
                index:[
                    {index:'/VIP/content',name:'会员主页'},
                    {index:'/VIP/BuyAndCost',name:'购买记录'},
                ]
            }
        },
        methods:{
            handleSelect(key, keyPath) {
                // let id=this.consumerId
                this.$router.push(key)
            },
        },
        computed:{
            activeIndex(){
                return this.$route.path.replace('//','')
            }
        },
    }
</script>

<style scoped>

</style>