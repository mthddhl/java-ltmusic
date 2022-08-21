<template>
    <div>
        <left class="left"></left>
        <right class="right"></right>
    </div>
</template>

<script>
    import left from './left'
    import right from './right'
    export default {
        name: "",
        data(){
            return{
                songListId:-1,
            }
        },
        components:{
            left,right
        },
        created() {
            this.songListId=this.$route.query.id
        }
    }
</script>

<style scoped>
    .left{
        width: 800px;
        /*height: 2500px;*/
        /*background-color: #cecee1;*/
        float: left;
        border: 1px solid #8d8d8d;
    }
    .right{
        width:396px;
        height: 1500px;
        background-color: #c6c6d5;
        float: right;
        border: 1px solid #8d8d8d;
    }
</style>