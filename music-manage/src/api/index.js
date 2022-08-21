import {get, post} from './http'
export const getLoginStatus = (params) => post('/loginController/login', params)
export const insertSinger = (params) => post('/singer/insert', params)
export const getSingerList = () => get('/singer/getList')
export const getSingerLikeName = (param) => get('/singer/getLikeNames', param)
// 根据名字查询歌手
export const getSingerByName = (param) => get('/singer/getSingerByName', param)

// 分页查询
export const getSingerPage = (param) => get('/singer/getSingerPage', param)
// 更新歌手
export const updateSinger = (param) => post('/singer/update', param)
// 删除歌手
export const deleteSingerById = (param) => get('/singer/delete', param)

// 删除歌手
export const deleteSingerByIds = (param) => get('/singer/deletes', param)

// 恢复歌手
export const SingerRestore = () => get('/singer/restore')

// 歌手for歌曲管理环节====================================================================================
// ===========================================================================================================
// 歌曲上传
export const songUpload = (param) => post('/song/upload', param)
// 全部歌曲查询
export const songAllList = (params) => get('/song/getAllList', params)
// 按页查询歌曲
export const songGetPage = (params) => get('/song/getSongPage', params)
// 搜索歌曲名与专辑
export const songGetNameAndIntro = (params) => get('/song/songGetNameAndIntro', params)
// 按照歌曲名或者专辑搜个歌曲
export const songGetLikes = (params) => get('/song/getSongLikes', params)

// 更新歌曲
export const songUpdate = (params) => post('/song/updateSong', params)

// 删除歌曲
export const songDeleteById = (params) => get('/song/deleteSongById', params)
export const convertVipStatus = (param) => get('/song/convertVipStatus', param)

// 删除多个歌曲
export const songDeleteByIds = (params) => get('/song/deleteSongByIds', params)
// 恢复歌曲
export const restoreSong = () => get('/song/restoreSong')

// 歌单管理===================================================================================
// 获取全部歌单
export const songListGetAll = () => get('/songList/songListGetAll')
// 按页获取歌单
export const songListGetPage = (params) => get('/songList/songListGetPage', params)
// 上传歌单
// export const songListUpload = (params) => post('/songList/songListUpload', params)
export const consumerCreatedSongList = (params) => post('/songList/consumerCreatedSongList', params)
// 获取相似歌单标题
export const songListGetLikeName = (params) => get('/songList/songListGetLikeName', params)
// 更新歌单
export const songListUpdate = (params) => post('/songList/songListUpdate', params)
// 删除歌单
export const songListdeleteById = (params) => get('/songList/songListDeleteById', params)
// 批量删除歌单
export const songListdeleteByIds = (params) => get('/songList/songListDeleteByIds', params)
// 回复全部歌单
export const songListRestart = () => get('/songList/songListRestart')

// 歌单for歌曲+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// 获取全部歌单歌曲
export const songforSongListGetAll = (params) => get('/listSong/songforSongListGetAll', params)

// 按页获取歌单歌曲
export const songforSongListGetPage = (params) => get('/listSong/songforSongListGetPage', params)
// 获取歌单相似歌曲名字
export const songforSongListGetLikeName = (params) => get('/listSong/songforSongListGetLikeName', params)
// 根据名字获取歌曲
export const songforSongListGetByName = (params) => get('/listSong/songforSongListGetByName', params)
// 歌曲移除歌单
export const songforSongListdeleteById = (params) => get('/listSong/songforSongListdeleteById', params)
// 选中歌曲移除歌单
export const songforSongListdeleteByIds = (params) => get('/listSong/songforSongListdeleteByIds', params)

// 用户管理+++++++++====================================================================
// 获取全部用户
export const consumerListGetAll = () => get('/consumer/consumerListGetAll')
// 登出==============================================
export const consumerLogout = () => get('/loginController/consumerLogout')
// 是否登录===============================================================
export const consumerIsLogin = () => get('/loginController/consumerIsLogin')
// 按页获取用户名
export const consumerListGetPage = (params) => get('/consumer/consumerListGetPage', params)
// 获取用户名相似歌曲名字
export const consumerGetLikeName = (params) => get('/consumer/consumerGetLikeName', params)
// 插入用户
export const consumerInsert = (params) => post('/consumer/consumerInsert', params)
// 更新用户
export const consumerUpdate = (params) => post('/consumer/consumerUpdate', params)
// 删除用户
export const consumerDelete = (params) => get('/consumer/consumerDelete', params)
// 删除选中
export const consumerDeleteIds = (params) => get('/consumer/consumerDeleteIds', params)
// 恢复
export const consumerRestore = () => get('/consumer/consumerRestore')

// 走马灯===========================================================
export const addSingerToCarousel = (params) => get('/carousel/addSingerToCarousel', params)

export const getAllCarousel = () => get('/carousel/getAllCarousel')

export const removeCarousel = (param) => get('/carousel/removeCarousel', param)

// 会员========================================================================
export const getAllProduct = () => get('/product/getAllProduct')
export const addVIPProduct = (params) => post('/product/addVIPProduct', params)
export const removeVIPProduct = (param) => get('/product/removeVIPProduct', param)
export const getOrderCount = () => get('/order/getOrderCount')
export const getOrderPage = (param) => get('/order/getOrderPage', param)
