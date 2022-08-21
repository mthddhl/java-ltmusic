<template>
<div>
  <br>
  <div>
    <h3>数据分布</h3>
  </div>
  <br><br><br>
  <el-row>
    <el-col :span="8"><pie :chart-data="sexData" :name="name"></pie></el-col>
    <el-col :span="8"><bar :chart-data="country" :name="name1"></bar></el-col>
<!--    <el-col :span="12"><div id="singCounties" style="width: 500px; height: 500px"></div></el-col>-->
  </el-row>
  <el-row>
<!--    <el-col :span="8"><bar :chart-data="songCount" :name="name2"></bar></el-col>-->
  </el-row>
</div>
</template>

<script>
  import {getSingerSex,getSingerCountry,getSongCountForSinger} from "../../api/infoData";
  import pie from './echarts/pie'
  import bar from './echarts/bar'
  export default {
    name: "",
    components:{
      pie,bar
    },
    data(){
      return {
        name:'歌手性别',
        name1:'国籍分布',
        name2:'歌手歌曲数量分布',
        sexData:[],
        country:[],
        // songCount:[]
      }
    },
    mounted() {
    },
    created() {
      getSingerSex().then(res=>{
        if(res.success){
          this.sexData.push({value:res.data.man,name:'男'});
          this.sexData.push({value:res.data.queue,name:'组合'});
          this.sexData.push({value:res.data.woman,name:'女'});
        }
      })
      getSingerCountry().then(res=>{
        if(res.success){
          this.country=res.data;
        }
      })
      // getSongCountForSinger().then(res=>{
      //   if(res.success){
      //     this.songCount=res.data;
      //   }
      // })
    }
  }
</script>

<style scoped>
  .el-row {
    margin-bottom: 20px;
  }
  .el-row:last-child {
    margin-bottom: 0;
  }
  .el-col {
    border-radius: 4px;
  }
  .bg-purple-dark {
    background: #99a9bf;
  }
  .bg-purple {
    background: #d3dce6;
  }
  .bg-purple-light {
    background: #e5e9f2;
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }
  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }
</style>
