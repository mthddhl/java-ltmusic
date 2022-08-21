<template>
<div>
  <div >
    <el-table size="mini" border style="width:100%" :data="carousels">
      <el-table-column label="用户图片" width="130" align="center">
        <template slot-scope="scope">
          <div class="consumer-img">
            <img :src="getUrl(scope.row.pic)" style="width:100%">
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="toId" label="id" width="50" align="center"></el-table-column>
      <el-table-column prop="title" label="title" width="200" align="center"></el-table-column>
      <el-table-column label="操作" align="center" width="100">
        <template slot-scope="scope">
          <el-button size="mini" @click="deleteC(scope.row)" >移除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</div>
</template>

<script>
  import {getAllCarousel, removeCarousel} from "../../api";
  import {mixin} from "../../mixins";
  export default {
    name: "",
    mixins:[mixin],
    data(){
      return{
        carousels:[]
      }
    },
    methods:{
      deleteC(row){
        let id=row.id
        removeCarousel({"id":id}).then(res=>{
          if(res.success){
            this.$notify.success(res.data)
            for (let i = 0; i < this.carousels.length; i++) {
              if(this.carousels[i]===row){
                this.carousels.splice(i,1)
                break
              }
            }
          }else {
            this.$notify.error(res.errorMsg)
          }
        })
      },
      init(){
        getAllCarousel().then(res=>{
          if(res.success){
            this.carousels=res.data
          }else {
            this.$notify.error(res.errorMsg)
          }
        })
      }
    },
    created() {
      this.init()
    }
  }
</script>

<style scoped>
  .consumer-img{
    width: 90%;
    height:120px;
    border-radius:5px;
    margin-bottom: 5px;
    overflow: hidden;
  }
</style>
