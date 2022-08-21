<template>
<div>
  <div :id="name" style="width: 500px; height: 500px"></div>
</div>
</template>

<script>
  export default {
    name: "",
    props:{
      chartData: {
        type: Array,
        default: ()=>[]
      },
      name:{
        type: String,
        default: ""
      }
    },
    methods:{
      initEChart(){
        let chart=this.$echarts.init(document.getElementById(this.name))
        chart.setOption({
          title: {
            text: this.name,
            left: 'center'
          },
          tooltip: {
            trigger: 'item'
          },
          legend: {
            orient: 'vertical',
            left: 'left'
          },
          series: [
            {
              name: 'Access From',
              type: 'pie',
              radius: '50%',
              data: this.chartData,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        })
      }
    },
    watch:{
      chartData: {
        handler(newVal,oldVal){
          this.initEChart();
          // console.log(newVal)
        }
      }
    },
    mounted() {
      this.$nextTick(()=>{
        this.initEChart()
      })
    },
  }
</script>

<style scoped>

</style>
