<template>

    <div>
        <br><br><br>
        <el-table :data="orders">
            <el-table-column prop="title" label="商品名称" width="100"></el-table-column>
            <el-table-column prop="orderNo" label="订单编号" width="200"></el-table-column>
            <el-table-column label="价格" align="center" width="150">
                <template #default="scope">
                    <span >{{initPrice(scope.row.totalFee)}}元</span>
                </template>
            </el-table-column>
            <el-table-column label="交易状态" width="120">
                <template #default="scope">
                    <span :style="initStatus(scope.row.orderStatus)">{{scope.row.orderStatus}}</span>
                </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="200"></el-table-column>
            <el-table-column label="操作">
                <template #default="scope">
                    <el-button v-if="isOper(scope.row.orderStatus)" @click="payOrder(scope.row)" size="mini">去支付</el-button>
                    <el-button v-if="isOper(scope.row.orderStatus)" @click="closeOrder(scope.row)" size="mini">关闭订单</el-button>
                    <el-button v-if="isOper(scope.row.orderStatus)" @click="flushOrder(scope.row)" size="mini">刷新</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script>
    import {closeOrderByOrderNo, flushOrderByOrderNo, getOrderByConId, payOrderByOrderNo} from "@/ajax/getAndPost";
    import {ElMessage} from "element-plus";

    export default {
        name: "",
        data(){
            return{
                orders:[]
            }
        },
        methods:{
            flushOrder(row){
                if(localStorage.getItem("token")===null || localStorage.getItem("token")===''){
                    ElMessage.success("您还未登录，不能刷新")
                    return;
                }
                let orderNo=row.orderNo
                flushOrderByOrderNo({"orderNo":orderNo}).then(res=>{
                    if(res.success){
                        ElMessage.success(res.data)
                        this.init()
                    }else {
                        ElMessage.error(res.errorMsg)
                    }
                })
            },
            isOper(status){
                return !(status === '已支付' || status === '订单已过期' || status === '订单已关闭');
            },
            initStatus(status){
                let style={};
                if(status==='已支付'){
                    style={
                        "color":'rgba(8,255,0,0.97)'
                    }
                }else if(status==='订单已过期' || status==='订单已关闭'){
                    style={
                        "color":'rgba(255,115,0,0.97)'
                    }
                }else {
                    style={
                        "color":'rgba(255,0,47,0.97)'
                    }
                }
                return style
            },
            init(){
                if(localStorage.getItem("token")===null || localStorage.getItem("token")===''){
                    ElMessage.success("您还未登录，不能查看")
                    return;
                }
                getOrderByConId().then(res=>{
                    this.orders=res.data;
                })
            },
            initPrice(price){
                return (price/100).toFixed(2)
            },
            payOrder(row){
                if(localStorage.getItem("token")===null || localStorage.getItem("token")===''){
                    ElMessage.success("您还未登录，不能支付")
                    return;
                }
                let orderNo=row.orderNo
                payOrderByOrderNo({"orderNo":orderNo}).then(res=>{
                    if(res.success){
                        const divForm = document.getElementsByTagName('divform')
                        if (divForm.length) {
                            document.body.removeChild(divForm[0])
                        }
                        const div = document.createElement('divform')
                        div.innerHTML = res.data // res就是支付宝返回给你的form
                        document.body.appendChild(div)
                        document.forms[0].submit()
                    }else {
                        ElMessage.error(res.errorMsg)
                    }
                })

            },
            closeOrder(row){
                if(localStorage.getItem("token")===null || localStorage.getItem("token")===''){
                    ElMessage.success("您还未登录，不能关闭")
                    return;
                }
                let orderNo=row.orderNo
                closeOrderByOrderNo({"orderNo":orderNo}).then(res=>{
                    if(res.success){
                        this.init()
                    }else {
                        ElMessage.error(res.errorMsg)
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

</style>