package com.cumt.musicserver.dao;

import com.cumt.musicserver.domain.Song;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mthddhl
 * @since 2022-05-16
 */
public interface SongDao extends BaseMapper<Song> {

    List<Map<String, Object>> getSongCountForSinger();

    List<Song> getPartSong(@Param("ids") Set<String> ids);

    List<Song> getSongBySongListIdPage(@Param("currentPage") Integer currentPage,
                                       @Param("pageSize") Integer pageSize,
                                       @Param("songListId") Integer songListId);

    List<Song> getConsumerLikeSong(@Param("idList") List<Integer> list);

    List<Song> getSongsByLikeText(String text);

    List<Song> getAllList(Integer singerId);

    List<Song> getSongBySingId(@Param("currentPage") Integer currentPage,
                               @Param("pageSize") Integer pageSize,
                               @Param("singerId") Integer singerId);
}
