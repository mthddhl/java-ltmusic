import {get, post} from './request'

// 歌单====================================================
export const getPartSongList = () => get('/songList/getPartSongList',)
export const songforSongListGetAll = (params) => get('/listSong/songforSongListGetAll', params)
export const songListGetById = (params) => get('/songList/songListGetById',params)
export const getLikeSongList=(params)=>get('/songList/getLikeSongList',params)
// export const songListGetSongsCount = (params) => get('/listSong/songListGetSongsCount', params)
export const songListGetSongsCountIncr = (params) => get('/songList/songListGetSongsCountIncr', params)
export const consumerLikeSongList = (params) => get('/songList/consumerLikeSongList',params)
export const consumerGetLikeSingList=(params)=>get("/songList/consumerGetLikeSingList",params)
export const consumerDeleteLikeSongList=(param)=>get("songList/consumerDeleteLikeSongList",param)
export const songListGetByLetter=(params)=>get("/songList/songListGetByLetter",params)
export const songListGetCountByLetter=(params)=>get("/songList/songListGetCountByLetter",params)
export const getTogetLikedSongList=(params)=>get('/songList/getTogetLikedSongList',params)
export const consumerCreatedSongList=(params)=>post('/songList/consumerCreatedSongList',params)
export const consumerGetCreatedSongList=(params)=>get('/songList/consumerGetCreatedSongList',params)
export const consumerAddSongToSongList=(params)=>get("/listSong/consumerAddSongToSongList",params)
export const consumerAddSongGetSongList=(params)=>get('/songList/consumerAddSongGetSongList',params)
export const songListUpdate=(param)=>post('/songList/songListUpdate',param)
export const consumerUpdateSongListSongs=(param)=>get('/listSong/consumerUpdateSongListSongs',param)

// 歌手======================================================
// 查询歌手++++++++++++++++++++++++++++++++++++++++++++++++++
export const getPartSinger = () => get('/singer/getPartSinger',)
export const songAllList = (params) => get('/song/getAllList', params)
export const getSingerById=(params) => get('/singer/getSingerById', params)
export const getAllList=(params)=>get('/song/getAllList',params)
export const getSongPage=(params)=>get('/song/getSongPage',params)
export const consumerLikeSinger=(param)=>get('/singer/consumerLikeSinger',param)
export const consumerGetLikeSinger=(param)=>get('/singer/consumerGetLikeSinger',param)
export const consumerDeleteLikeSinger=(param)=>get('/singer/consumerDeleteLikeSinger',param)
export const singerGetByLetter=(params)=>get("/singer/singerGetByLetter",params)
export const singerGetCountByLetter=(params)=>get("/singer/singerGetCountByLetter",params)
export const getLikeSinger=(params)=>get('singer/getLikeSinger',params)
export const getTogetLikedSingers=(params)=>get('singer/getTogetLikedSingers',params)
// 歌曲============================================
export const getPartSong = () => get('/song/getPartSong',)
export const getSongBySongListIdPage = (params) => get('/song/getSongBySongListIdPage', params)
export const consumerLikeSong=(params)=>get("/song/consumerLikeSong",params)
export const getConsumerLikeSong=(params)=>get("/song/getConsumerLikeSong",params)
export const songDownload=(params)=>get('/song/songDownload',params)
// 评论================================================
export const subComments = (params) => post('/songListComment/subComments', params)
export const getCommentsBySongListId = (params) => get('/songListComment/getCommentsBySongListId', params)
export const getCommentCount = (params) => get('/songListComment/getCommentCount', params)
export const consumerLikeComment = (params) => get('/songListComment/likeOfConsumer', params)
export const consumerDeleteComment=(params) =>get('/songListComment/consumerDeleteComment',params)


// 用户操作
export const getLoginStatus=(params)=>post("/loginController/login",params)
// export const getConsumerInfo = (params) => get('/consumer/getConsumerInfo',params)
export const consumerLogout = () => get('/loginController/consumerLogout')
export const consumerIsLogin=()=>get("/loginController/consumerIsLogin")
export const consumerGetInfo=(params)=>get('/consumer/consumerGetInfo',params)
export const consumerInsert=(params)=>post('/consumer/consumerInsert',params)
export const consumerUpdate=(params)=>post('/consumer/consumerUpdate',params)
export const consumerUpdateAvator=(param)=>post('/consumer/consumerUpdateAvator',param)

// 搜索========================================================================
export const searchGetAll=(params)=>get('/search/searchGetAll',params)
export const searchContent=(params)=>get('/search/searchContent',params)

// 文件===========================================================
export const fileIsExist=(params)=>get('/search/fileIsExist',params)

// 走马灯================================
export const getAllCarousel = () => get('/carousel/getAllCarousel')

// VIP================================================
export const getAllVIPProduct=()=>get('/product/getAllVIPProduct')
export const createdOrder=(param)=>get('/aliPayApi/createdOrder',param)
export const getOrderByConId=()=>get('/order/getOrderByConId')
export const payOrderByOrderNo=(param)=>get('/order/payOrderByOrderNo',param)
export const closeOrderByOrderNo=(param)=>get('/order/closeOrderByOrderNo',param)
export const flushOrderByOrderNo=(param)=>get("/order/flushOrderByOrderNo",param)
