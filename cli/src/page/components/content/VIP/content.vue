<template>
    <div>
        <div style="margin-left: 30px;">
            <el-container>
                <el-aside width="700px">
                    <br><br><br>
                    <span style="font-size: 20px;color: #00de46">欢迎加入会员大家庭中，在这里您可以享受到</span>
                    <br><br><br>
                    <ul>
                        <li v-for="item in privileges" class="privileges" :key="item"><span>{{item}}</span></li>
                    </ul>
                </el-aside>
                <el-main>
                    <br><br><br><br>
                    <div v-if="isVIP.isVip">
                        <span style="font-size: 24px;color: #00dea3">您已经是会员家庭的一员了</span><br><br><br>
                        <span style="font-size: 18px;color: #000000;margin-left: 80px">会员到期时间：{{isVIP.vipExpireTime}}</span>
                    </div>
                    <div v-else>
                        <span style="font-size: 24px;color: #00dea3">您还未加入会员</span><br>
                    </div>
                    <br><br><br><br><br>
                    您可以用一下账号进行购买 <br><br>
                    账户：tbimcw3131@sandbox.com<br><br>
                    密码：111111
                </el-main>
            </el-container>
        </div>
        <br><br>
        <div>
            <div v-if="discountVip.length>0">
                <span style="font-size: 15px">优惠专区/每个用户仅能购买一个</span>
                <el-divider  style="margin-top: 5px"/><br>
                <el-row :gutter="12">
                    <el-col v-for="item in discountVip" :key="item" :span="7" style="background-color: rgba(248,2,162,0.05);height:100px;margin-left: 35px">
                        <el-container>
                            <el-aside width="140px" style="margin-left: -5px">
                                <img src="../../../../img/VIP.jpeg" style="height: 100px;float: left;">
                            </el-aside>
                            <el-main>
                                <div style="font-size: 18px;margin-top: -18px">
                                    <span >{{item.title}}</span>
                                    <el-button @click="pay(item.id)" type="danger" style="margin-left: 120px;height: 30px;margin-top: -30px" size="mini">购买</el-button>
                                </div>
                                <div style="font-size: 15px;margin-top: -10px">价格：{{initPrice(item.price)}}元</div>
                                <div style="font-size: 15px;margin-top: 5px">数量：{{item.count}}</div>
                                <div style="font-size: 15px;margin-top: 5px">还有：{{initTime(item.expireTime)}}</div>
                            </el-main>
                        </el-container>
                    </el-col>
                </el-row>
                <br><br><br><br><br>
            </div>
            <div>
                <span style="font-size: 15px">全额专区/购买后请重新刷新会员状态</span>
                <el-divider  style="margin-top: 5px"/><br>
                <el-row :gutter="12">
                    <el-col v-for="item in VIPIndex" :key="item" :span="7" style="background-color: rgba(248,2,162,0.05);height:100px;margin-left: 35px">
                        <el-container>
                            <el-aside width="140px" style="margin-left: -5px">
                                <img src="../../../../img/VIP.jpeg" style="height: 100px;float: left;">
                            </el-aside>
                            <el-main>
                                <div style="font-size: 18px;">
                                    <span >{{item.title}}</span>
                                    <el-button type="danger"
                                               style="margin-left: 50px;height: 30px;margin-top: -5px"
                                               size="mini"
                                               @click="pay(item.id)"
                                    >购买</el-button>
                                </div>
                                <br>
                                <div style="font-size: 15px;margin-top: 5px">价格：{{initPrice(item.price)}}元</div>
                            </el-main>
                        </el-container>
                    </el-col>
                </el-row>
            </div>
        </div>
        <br><br><br><br><br>
    </div>
</template>

<script>
    import {createdOrder, getAllVIPProduct} from "@/ajax/getAndPost";
    import {ElMessage} from "element-plus";

    export default {
        name: "",
        data(){
            return{
                privileges:['可畅听会员专属音乐','可随意下载会员专属音乐','可享受专属标识符','可享有XXXXXXXXXX'],
                discountVip:[],
                VIPIndex:[],
                isVIP:{}
            }
        },
        methods:{
            init(){
                getAllVIPProduct().then(res=>{
                    this.discountVip=res.data.discountVip
                    this.VIPIndex=res.data.VIPIndex;
                })
                if(localStorage.getItem("consumer")!==null && localStorage.getItem("consumer")!==''){
                    let consumer=JSON.parse(localStorage.getItem("consumer"));
                    let vipExpireTime=consumer.vipExpireTime;
                    let now=new Date().getTime();
                    if(now>new Date(vipExpireTime).getTime()){
                        this.isVIP.isVip=false
                    }else {
                        this.isVIP.isVip=true;
                        this.isVIP.vipExpireTime=new Date(vipExpireTime).toLocaleDateString()
                    }
                }
            },
            initPrice(price){
                return (price/100).toFixed(2)
            },
            initTime(item){
                let nowTime=Date.now();
                let time=new Date(item).getTime();
                let res=time-nowTime+1;
                let result=''

                if(res/(60*60*1000*24)>0){
                    result=result+((res/(60*60*1000*24)).toFixed(0))+'天'
                    res=res%(60*60*1000*24);
                }
                if(res/(60*60*1000)>0){
                    result=result+((res/(60*60*1000)).toFixed(0))+'时'
                    res=res%(60*60*1000);
                }
                if(res/(60*1000)>0){
                    result=result+((res/(60*1000)).toFixed(0))+'分'
                }
                return result
            },
            pay(id){
                if(localStorage.getItem("token")===null || localStorage.getItem("token")===''){
                    ElMessage.success("您还未登录，不能购买")
                    return;
                }
                createdOrder({"produceId":id}).then(res=>{
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
.privileges{
    font-size: 18px;
    color: #8d8d8d;
    height: 50px
}
</style>