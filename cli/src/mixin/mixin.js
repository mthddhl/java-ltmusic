import {ElMessage} from "element-plus";
import {fileIsExist, songAllList, songforSongListGetAll, songListGetSongsCountIncr} from "@/ajax/getAndPost";
import axios from "axios";
import fileDownload from "js-file-download";

export const mixin={
    methods: {
        addAlong(row) {
            if (row.isVip && !this.$store.state.isVip) {
                ElMessage.success("您不是会员，无法播放此歌曲")
                return
            }
            let res = {
                status: 3,
                name: row.name,
                song: {
                    name: row.name,
                    artist: row.singerName,
                    url: this.$store.state.httpFileUrl + row.url,
                    cover: this.$store.state.httpFileUrl + row.pic,
                    lrc: row.lyric
                }
            }
            this.$store.state.songList = res;
        },
        closeSong(row) {
            let res = {
                status: 3,
                name: '',
                song: {
                    name: row.name,
                    artist: row.singerName,
                    url: this.$store.state.httpFileUrl + row.url,
                    cover: this.$store.state.httpFileUrl + row.pic,
                }
            }
            this.$store.state.songList = res;
        },
        addSong(row) {
            let _this = this
            if (row.isVip && !this.$store.state.isVip) {
                ElMessage.success("您不是会员，无法播放此歌曲")
                return
            }
            let res = {
                status: 0,
                songAlone: {
                    name: row.name,
                    artist: row.singerName,
                    url: this.$store.state.httpFileUrl + row.url,
                    cover: this.$store.state.httpFileUrl + row.pic,
                    lrc: row.lyric
                }
            }
            this.$store.state.songList = res;
        },


        downloadSong(row){
            if(row.isVip && !this.$store.state.isVip){
                ElMessage.success("您不是会员无法下载此歌曲")
                return
            }
            fileIsExist({'url':row.url}).then(res=>{
                if(res.success){
                    ElMessage.success("正在下载")
                    row.isDownLoad=true;
                    axios.get(this.$store.state.httpFileUrl+row.url,
                        {
                            timeout:50000000,
                            responseType:'blob',
                            headers: {
                                'Content-Type': 'application/octet-stream'
                            },
                            onDownloadProgress:function (a) {
                                row.isDownLoadNum=(a.loaded/a.total*100).toFixed(2);
                            }
                        }
                    ).then(res=>{
                        fileDownload(res.data,row.name+'.mp3')
                    })
                    // window.location.href=this.$store.state.httpFileUrl+url
                }else{
                    ElMessage.error(res.errorMsg)
                }
            })
        },

        singerPlay(id){
            songAllList({"singerId":id}).then(res=>{
                if(res.success){
                    if(res.data.length===0){
                        ElMessage.error('歌手无歌曲')
                        return
                    }
                    let songs=[];
                    res.data.forEach(each=>{
                        if(!each.isVip || this.$store.state.isVip) {
                            songs.push({
                                name: each.name,
                                artist: each.singerName,
                                url: this.$store.state.httpFileUrl + each.url,
                                cover: this.$store.state.httpFileUrl+each.pic,
                                lrc:each.lyric
                            })
                        }
                    })
                    let r={
                        status:1,
                        song:songs,
                    }
                    this.$store.state.songList=r;
                }
            })
        },

        songListPlay(i){
            songforSongListGetAll({"songListId":i.id}).then(res=>{
                if(res.success){
                    let songs=[];
                    res.data.forEach(each=>{
                        if(!each.isVip || this.$store.state.isVip) {
                            songs.push({
                                name: each.name,
                                artist: each.singer,
                                url: this.$store.state.httpFileUrl + each.url,
                                cover: this.$store.state.httpFileUrl+each.pic,
                                lrc:each.lyric
                            })
                        }
                    })
                    let r={
                        status:2,
                        song:songs,
                    }
                    this.$store.state.songList=r;
                    if(!i.isplay){
                        if(!this.isSongListPlay){
                            songListGetSongsCountIncr({"id":i.id}).then(res=>{
                                if(res.success){
                                    i.count+=1;
                                    i.isplay=true;
                                    ElMessage.success("播放成功")
                                }
                            })
                        }
                    }
                }
            })
        }
    }

}