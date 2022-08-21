<template>
<div>
  <el-button @click="dialogIsShown=true" type="primary" size="mini" style="margin-top: 10px;margin-left: 20px">添加会员券</el-button>

  <el-table size="mini" border style="width:100%;margin-top: 10px;" :data="couponsAndPrice">
    <el-table-column prop="title" label="会员类型" width="150" align="center"></el-table-column>
    <el-table-column prop="type" label="优惠类型" width="150" align="center"></el-table-column>
    <el-table-column prop="price" label="价格" width="100" align="center">
      <template slot-scope="scope">
        {{initPrice(scope.row.price)}}
      </template>
    </el-table-column>
    <el-table-column prop="createTime" label="创建时间" width="150" align="center"></el-table-column>
    <el-table-column prop="expireTime" label="过期时间" width="150" align="center"></el-table-column>
    <el-table-column prop="count" label="数量" width="150" align="center"></el-table-column>
    <el-table-column label="操作" align="center" width="100">
      <template slot-scope="scope">
        <el-button size="mini" @click="deleteCNP(scope.row)">移除</el-button>
      </template>
    </el-table-column>
  </el-table>

  <el-dialog title='添加限时优惠会员' :visible.sync="dialogIsShown" width="450px" center>
    <el-form :model="newCAP" label-width="130px" ref="newCAP" :rules="rules">
      <el-form-item label="会员类型" prop="title">
        <el-select v-model="newCAP.title">
          <el-option label="月会员(惠)" value="月会员(惠)"></el-option>
          <el-option label="季会员(惠)" value="季会员(惠)"></el-option>
          <el-option label="年会员(惠)" value="年会员(惠)"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="会员价格(分)" prop="price">
        <el-input v-model="newCAP.price" size="mini"></el-input>
      </el-form-item>
      <el-form-item label="数量" prop="count">
        <el-input v-model="newCAP.count" size="mini"></el-input>
      </el-form-item>
      <el-form-item label="优惠力度" prop="count">
        <el-input v-model="newCAP.type" size="mini" disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="过期时间" prop="expireTime">
        <el-date-picker type="date" placeholder="填写日期" v-model="newCAP.expireTime" style="width: 100%"
                        value-format="yyyy-MM-dd HH:mm:ss"
        ></el-date-picker>
      </el-form-item>
    </el-form>
    <span slot="footer">
      <el-button size="mini" @click="submit">确认</el-button>
      <el-button size="mini" @click="dialogIsShown=false">取消</el-button>
    </span>
  </el-dialog>


</div>
</template>

<script>

  import {addVIPProduct, consumerInsert, getAllProduct, removeVIPProduct} from "../../../api";
  import {CodeToText} from "element-china-area-data";

  export default {
    name: "",
    data(){
      return{
        isUpdate:false,
        dialogIsShown:false,
        couponsAndPrice:[],
        newCAP:{
          type:"限时优惠"
        },
        rules:{
          title:[
            {required:true,message:'请选择会员类型',trigger:['blur','change']}
          ],
          price:[
            {required:true,message:'请输入价格',trigger:'blur'},
            {pattern:/^[1-9]*[1-9][0-9]*$/,message: '请输入大于0的正整数',trigger:'blur'}
          ],
          count:[
            {required:true,message:'请输入数量',trigger:'blur'},
            {pattern:/^[1-9]*[1-9][0-9]*$/,message: '请输入大于0的正整数',trigger:'blur'}
          ],
          expireTime:[
            {required:true,message:'请输入过期时间',trigger:'blur'},
          ],
        },
      }
    },
    methods:{
      deleteCNP(row){
        removeVIPProduct({"id":row.id}).then(res=>{
          if(res.success){
            this.init()
          }else {
            this.$notify.error(res.errorMsg)
          }
        })
      },
      submit(){

        this.$refs['newCAP'].validate(valid=>{
          if(valid){
            addVIPProduct(this.newCAP).then(res=>{
              if(res){
                this.$notify.success(res.data)
                this.init()
              }else {
                this.$notify.error(res.errorMsg)
              }
            })
            this.dialogIsShown=false;
          }
        })
      },
      init(){
        getAllProduct().then(res=>{
          this.couponsAndPrice=res.data
        })
      },
      initPrice(price){
        return (price/100).toFixed(2)
      }
    },
    created() {
      this.init()
    }
  }
</script>

<style scoped>

</style>
