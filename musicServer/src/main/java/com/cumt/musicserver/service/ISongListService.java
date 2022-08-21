package com.cumt.musicserver.service;

import com.cumt.musicserver.domain.SongList;
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
public interface ISongListService extends IService<SongList> {

    Result songListGetPage(Integer currentPage, Integer pageSize,String likeNameOne);

    Result changeImg(MultipartFile multipartFile, Integer id);

    Result getPartSongList();

    Result consumerLikeSongList(Integer id);

    Result getSongListGetById(Integer id);

    Result consumerGetLikeSingList(Integer id);

    Result consumerDeleteLikeSongList(Integer id);

    Result songListGetCountByLetter(String letter);

    Result songListGetByLetter(String letter, Integer currentPage, Integer pageSize);

    Result getLikeSongList(Integer id);

    Result getTogetLikedSongList(Integer id);

    void setCount(List<SongList> list1);

    void deleteCount(List<Integer> list);

    Result consumerCreatedSongList(SongList songList,MultipartFile file);

    Result consumerGetCreatedSongList(Integer id);

    Result consumerAddSongGetSongList(Integer consumerId,Integer songId);
}
