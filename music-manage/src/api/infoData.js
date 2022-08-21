import {get} from './http'
export const getSingerSex = () => get('/singer/getSex')
export const getSingerCountry = () => get('/singer/getSingerCountry')
export const getSongCountForSinger = () => get('/song/getSongCountForSinger')
