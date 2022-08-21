package com.cumt.musicserver.dao;

import com.cumt.musicserver.domain.SongList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mthddhl
 * @since 2022-05-16
 */
public interface SongListDao extends BaseMapper<SongList> {

    Integer songListGetCountByLetter(String letter);

    List<SongList> songListGetByLetter(@Param("letter") String letter,
                                       @Param("currentPage") Integer currentPage,
                                       @Param("pageSize") Integer pageSize);

    SongList getSongListGetById(Integer id);

    List<SongList> consumerAddSongGetSongList(
            @Param("consumerId") Integer consumerId,
            @Param("songId") Integer songId);
}
