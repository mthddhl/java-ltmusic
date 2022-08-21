package com.cumt.musicserver.dao;

import com.cumt.musicserver.domain.ListSong;
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
public interface ListSongDao extends BaseMapper<ListSong> {

    List<ListSong> songforSongListGetAll(Integer id);

    List<ListSong> songforSongListGetPage(@Param("start") Integer start,@Param("pageSize") Integer pageSize, @Param("likeNameOne") String likeNameOne,@Param("id") Integer id);

    List<String> songforSongListGetLikeName(@Param("name") String name, @Param("songListId") Integer songListId);

    @Select("SELECT * \n" +
            "FROM list_song \n" +
            "WHERE \n" +
            "(song_list_id IN(1,2,3,4,5,6,7)) \n" +
            "and song_list_id \n" +
            "not in \n" +
            "(SELECT song_list_id FROM list_song WHERE song_id =36)")
    List<ListSong> test();
}
