package com.cumt.musicserver.service;

import com.cumt.musicserver.domain.Song;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cumt.musicserver.util.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mthddhl
 * @since 2022-05-16
 */
public interface ISongService extends IService<Song> {

    Result upload(MultipartFile musicFile,MultipartFile picFile,Song song);

    Result getAllList(Integer singerId);

    Result getSongPage(Integer currentPage, Integer pageSize, Integer singerId);

    Result songGetNameAndIntro(String name,Integer singerId);

    Result getSongLikes(String name);

    Result updateSong(Song song,MultipartFile musicFile,MultipartFile picFile);


    Result deleteSongById(String id);

    Result deleteSongByIds(List<Integer> ids);

    Result restoreSong();

    Result getSongCountForSinger();

    Result getPartSong();

    Result getSongBySongListId(Integer currentPage, Integer pageSize, Integer songListId);

    Result consumerLikeSong(Integer songId, Boolean isLike);

    Result getConsumerLikeSong(Integer id);

    void songLiked(List<Song> list);

    Result convertVipStatus(Integer id);
}
