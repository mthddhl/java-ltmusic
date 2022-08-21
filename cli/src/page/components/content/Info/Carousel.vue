<template>
    <div class="carousel">
        <el-carousel v-if="carousels.length>0" :interval="5000" arrow="always" height="400px" style="width: 700px;top: 10px;
        margin: auto" :initial-index="0">
            <el-carousel-item v-for="item in carousels" :key="item">
                  <div class="carouselItem" :style="{ 'background': 'url('+this.$store.state.httpFileUrl+item.pic+')',
                                                    'backgroundSize':'100%,auto'}"  @click="to(item)"></div>
            </el-carousel-item>
        </el-carousel>
    </div>
</template>

<script>
    import {getAllCarousel} from '@/ajax/getAndPost'
    export default {
        name: "",
        data(){
            return{
                carousels:[],
            }
        },
        methods:{
            init(){
                getAllCarousel().then(res=>{
                    if(res.success){
                        this.carousels=res.data
                    }else {
                        this.$notify.error(res.errorMsg)
                    }
                })
            },
            to(item){
                let id=item.toId
                let path='/'+item.type
                console.log(item)
                this.$router.push({path:path,query:{id}})
            }
        },
        created() {
            this.init()
        }
    }
</script>

<style scoped>
    .el-carousel__item h3 {
        color: #097ccc;
        opacity: 0.75;
        line-height: 300px;
        margin: 0;
        text-align: center;
    }

    .el-carousel__item:nth-child(2n) {
        background-color: #114080;
    }

    .el-carousel__item:nth-child(2n + 1) {
        background-color: #ffffff;
    }
    .carousel{
        /*background-color: #ffffff;*/
    }
    .carouselItem{
        width:100%;
        height: 100%;
        cursor: pointer;
        /*background-size: cover;*/
    }
</style>