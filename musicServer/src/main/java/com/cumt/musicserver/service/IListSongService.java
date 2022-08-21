package com.cumt.musicserver.service;

import com.cumt.musicserver.domain.ListSong;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cumt.musicserver.util.Result;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mthddhl
 * @since 2022-05-16
 */
public interface IListSongService extends IService<ListSong> {

    Result songforSongListGetAll(Integer id);

    Result songforSongListGetPage(Integer currentPage, Integer pageSize, String likeNameOne,Integer songListId);

    Result songforSongListGetLikeName(String name, Integer songListId);

    Result songListGetSongsCount(Integer id);

    Result consumerAddSongToSongList(Integer addSongId, List<Integer> addSongListIds);

    Result consumerUpdateSongListSongs(Integer id,List<Integer> list);
}
