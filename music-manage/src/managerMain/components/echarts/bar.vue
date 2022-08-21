<template>
  <div>
    <div :id="name" style="width: 400px; height: 400px"></div>
  </div>
</template>

<script>
  export default {
    name: "",
    data(){
      return{
        x:[],
        y:[],
      }
    },
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
          xAxis: {
            type: 'category',
            data: this.x,
            axisLabel:{
              formatter: function (value, index) {
                var str = value.split("");
                return str.join("\n");
              },
            }
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              data: this.y,
              type: 'bar',
              showBackground: true,
              backgroundStyle: {
                color: 'rgba(180, 180, 180, 0.2)'
              }
            }
          ]
        })
      }
    },
    watch:{
      chartData: {
        handler(newVal,oldVal){
          this.chartData.forEach(each=>{
            this.x.push(each.name)
            this.y.push(each.value)
            }
          )
          console.log(this.x)
          this.initEChart();
        }
      }
    },
    mounted() {
      this.initEChart();
    },
  }
</script>

<style scoped>

</style>
