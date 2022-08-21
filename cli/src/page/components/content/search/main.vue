<template>
    <div>
        <br><br><br>
        <div class="comm">
            <span style="margin-left: 10px">歌曲搜索结果</span>
        </div>
        <el-divider style="margin-top: 5px"/>
        <search-song :songs="songs"></search-song>
        <div class="comm">
            <span style="margin-left: 10px">歌手搜索结果</span>
        </div>
        <el-divider style="margin-top: 5px"/>
        <search-singer :text="text" :singers="singers"></search-singer>
        <div class="comm">
            <span style="margin-left: 10px">歌单搜索结果</span>
        </div>
        <el-divider style="margin-top: 5px;margin-bottom:10px"/>
        <search-song-list :text="text" :song-lists="songLists"></search-song-list>
    </div>

</template>

<script>
    import searchSinger from './search_singer'
    import searchSong from './search_song'
    import searchSongList from './search_songList'
    import {searchContent} from "@/ajax/getAndPost";
    export default {
        name: "",
        data(){
            return{
                text:"",
                songs:[],
                singers:[],
                songLists:[]
            }
        },
        components:{
            searchSinger,searchSong,searchSongList
        },
        methods:{
          init(){
              searchContent({'text':this.text}).then(res=>{
                  if(res.success){
                      console.log(res)
                      this.songs=res.data.songs;
                      this.singers=res.data.singers;
                      this.songLists=res.data.songLists
                  }
              })
          }
        },
        watch: {
            $route(to, from) {
                if (to.query.text!== from.query.text) {
                    this.text=this.$route.query.text
                    this.init()
                }
            }
        },
        created() {
            this.text=this.$route.query.text
            this.init()
        }
    }
</script>

<style scoped>
    .comm{
        /*position:absolute;*/
        text-align: left;
        margin-top: 30px;
        color: #2aa3ef;
    }
</style>