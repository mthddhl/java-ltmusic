package com.cumt.musicserver.service.impl;

import com.cumt.musicserver.domain.Consumer;
import com.cumt.musicserver.domain.ListSong;
import com.cumt.musicserver.dao.ListSongDao;
import com.cumt.musicserver.domain.Song;
import com.cumt.musicserver.service.IListSongService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cumt.musicserver.util.Result;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.cumt.musicserver.util.StaticString.CONSUMER_LIKE_SONGS;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mthddhl
 * @since 2022-05-16
 */
@Service
public class ListSongServiceImpl extends ServiceImpl<ListSongDao, ListSong> implements IListSongService {
    @Resource
    private ListSongDao listSongDao;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result songforSongListGetAll(Integer id) {
        List<ListSong> listSongs = listSongDao.songforSongListGetAll(id);
        return Result.ok(listSongs);
    }

    @Override
    public Result songforSongListGetPage(Integer start, Integer pageSize, String likeNameOne,Integer songListId) {
        List<ListSong> listSongs = listSongDao.songforSongListGetPage(start, pageSize, likeNameOne,songListId);
        return Result.ok(listSongs);
//        return null;
    }

    @Override
    public Result songforSongListGetLikeName(String name, Integer songListId) {
        List<String> list = listSongDao.songforSongListGetLikeName(name, songListId);
        return Result.ok(list);
    }

    @Override
    public Result songListGetSongsCount(Integer id) {
        Integer count = lambdaQuery().eq(ListSong::getSongListId, id).count();
        return Result.ok(count);
    }

    @Override
    public Result consumerAddSongToSongList(Integer addSongId, List<Integer> addSongListIds) {
        List<ListSong> listSongs=new ArrayList<>();
        addSongListIds.forEach(a->{
            listSongs.add(new ListSong(null,addSongId,a));
        });
        if(saveBatch(listSongs)) return Result.ok("添加成功");
        return Result.fail("添加失败");
    }

    @Override
    @Transactional
    public Result consumerUpdateSongListSongs(Integer id, List<Integer> list) {
        if(ObjectUtils.isEmpty(list))return Result.ok("移除成功");

        List<ListSong> list1 = lambdaQuery().select(ListSong::getId).eq(ListSong::getSongListId, id)
                .in(ListSong::getSongId, list).list();
        if(ObjectUtils.isEmpty(list1)) return Result.ok("移除成功");
        List<Integer> list2 = new ArrayList<>();
        list1.forEach(a->list2.add(a.getId()));
        if(removeByIds(list2)) return Result.ok("移除成功");
        return Result.ok("移除失败");
    }
}
