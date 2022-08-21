<template>
<div>
    <div id="test"></div>
</div>
</template>

<script>
    import 'aplayer/dist/APlayer.min.css'
    import APlayer from 'aplayer'
    export default {
        name: "",
        props:{
        },
        data(){
            return{
                isShow:true,
                songList:[],
                audio:[
                ],
                ap:{}
            }
        },
        methods:{
            init(){
                this.ap=new APlayer({
                    container: document.getElementById('test'),
                    fixed:true,
                    audio: this.audio
                })
            },
            change(){
                this.ap.list.clear();
                this.ap.list.add([
                    {
                        name: '渐渐被你吸引1',
                        artist: 'zard',
                        url: 'http://music.163.com/song/media/outer/url?id=493735159.mp3',
                        cover: 'https://p1.music.126.net/11Tw_r2wCvMZY--TtTvuGw==/1416170990976003.jpg?',
                    },
                    {
                        name: '渐渐被你吸引2',
                        artist: 'zard',
                        url: 'http://music.163.com/song/media/outer/url?id=493735159.mp3',
                        cover: 'https://p1.music.126.net/11Tw_r2wCvMZY--TtTvuGw==/1416170990976003.jpg?',
                    },
                    {
                        name: '渐渐被你吸引3',
                        artist: 'zard',
                        url: 'http://music.163.com/song/media/outer/url?id=493735159.mp3',
                        cover: 'https://p1.music.126.net/11Tw_r2wCvMZY--TtTvuGw==/1416170990976003.jpg?',
                    },
                ])
                this.ap.play()
            }
        },
        mounted() {
            this.init()
        },
        watch: {
            '$store.state.songList':{
                handler(newNum,oldNum){
                    if(newNum.status===1){
                        this.ap.list.clear();
                        this.ap.list.add(newNum.song)
                        this.ap.play()
                    }else if(newNum.status===3){
                        this.ap.list.clear();
                        this.ap.list.add(newNum.song)
                        if(newNum.name!==''){
                            this.ap.play()
                        }
                    }else if(newNum.status===2){
                        this.ap.list.clear();
                        console.log(newNum.song.length)
                        if(newNum.song.length>0){
                            this.ap.list.add(newNum.song)
                            this.ap.play()
                        }
                    }else if(newNum.status===4){
                        this.ap.list.clear();
                        if(newNum.song.length>0) {
                            this.ap.list.add(newNum.song)
                            this.ap.play()
                        }
                    }
                    else {
                        console.log(newNum.songAlone)
                        this.ap.list.add(newNum.songAlone)
                        this.ap.play()
                    }
                }
            }
        }
    }
</script>

<style scoped>

</style>