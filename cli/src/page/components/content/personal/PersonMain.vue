<template>
<!--    <el-row>-->
    <!--        <el-col :span="3">-->
    <!--            <Meun></Meun>-->
    <!--        </el-col>-->
    <!--        <el-col :span="21" style="border:1px solid #000000;height:1200px">-->
    <!--            <router-view></router-view>-->
    <!--        </el-col>-->
    <!--    </el-row>-->
    <div style="border:1px solid #000000;">
        <consumer-info></consumer-info>
        <Meun :consumer-id="consumerId"></Meun>
        <router-view></router-view>
        <br><br><br><br><br><br><br><br><br>
    </div>

</template>

<script>
    import Meun from './Meun'
    import ConsumerInfo from './ConsumerInfo'
    export default {
        name: "",
        data(){
            return{
                consumerId:-1
            }
        },
        components:{
            Meun,ConsumerInfo
        },
        created() {
          this.consumerId=this.$route.query.id
        },
        watch: {
            $route(to, from) {
                if (to.query.id !== from.query.id) {
                    this.consumerId = to.query.id;
                }
            },
        },
    }
</script>

<style scoped>
</style>