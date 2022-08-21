<template>
  <div>
    <el-table size="mini" border style="width:100%;margin-top: 10px;" :data="order">
      <el-table-column prop="title" label="会员类型" width="150" align="center"></el-table-column>
      <el-table-column prop="orderNo" label="订单编号" width="250" align="center"></el-table-column>
      <el-table-column label="价格" width="100" align="center">
        <template slot-scope="scope">
          {{initPrice(scope.row.totalFee)}}
        </template>
      </el-table-column>
      <el-table-column prop="userId" label="用户ID" width="60" align="center"></el-table-column>
      <el-table-column prop="productId" label="商品ID" width="60" align="center"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="150" align="center"></el-table-column>
      <el-table-column prop="updateTime" label="最后更新时间" width="150" align="center"></el-table-column>
      <el-table-column prop="orderStatus" label="状态" width="100" align="center"></el-table-column>
    </el-table>
    <el-pagination
      @current-change="handleCurrentChange"
      :current-page.sync="currentPage"
      :page-size="pageSize"
      layout="total, prev, pager, next"
      :total="total">
    </el-pagination>
  </div>
</template>

<script>

  import {getOrderCount, getOrderPage} from "../../../api";

  export default {
    name: "",
    data(){
      return{
        order:[],
        total:100,
        pageSize:10,
        currentPage:1,
      }
    },
    methods:{
      initPrice(price){
        return (price/100).toFixed(2)
      },
      handleCurrentChange(val){
        let params=new URLSearchParams();
        params.append("currentPage",val)
        params.append("pageSize",this.pageSize)
        getOrderPage(params).then(res=>{
          this.order=res.data
        })
      },
      init(){
        getOrderCount().then(res=>{
          this.total=res.data
        })
        this.handleCurrentChange(1)
      }
    },
    created() {
      this.init()
    }
  }
</script>

<style scoped>

</style>
