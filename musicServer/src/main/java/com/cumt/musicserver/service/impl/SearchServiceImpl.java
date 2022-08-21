package com.cumt.musicserver.service.impl;

import com.cumt.musicserver.dao.SongDao;
import com.cumt.musicserver.domain.Singer;
import com.cumt.musicserver.domain.Song;
import com.cumt.musicserver.domain.SongList;
import com.cumt.musicserver.service.ISearchService;
import com.cumt.musicserver.service.ISingerService;
import com.cumt.musicserver.service.ISongListService;
import com.cumt.musicserver.service.ISongService;
import com.cumt.musicserver.util.Result;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


@Service
public class SearchServiceImpl implements ISearchService {
    @Resource
    private ISongService songService;
    @Resource
    private ISongListService songListService;
    @Resource
    private ISingerService singerService;
    @Resource
    private SongDao songDao;
    @Override
    public Result searchGetAll(String text) {
        List<Singer> singerList = singerService.lambdaQuery()
                .select(Singer::getName).
                likeRight(Singer::getName, text).list();
        List<Song> songList = songService.lambdaQuery().select(Song::getName).
                likeRight(Song::getName, text).list();
        List<SongList> songListList = songListService.lambdaQuery().select(SongList::getTitle).
                likeRight(SongList::getTitle, text).list();
        List<String> list=new ArrayList<>();
        for (Singer singer: singerList) list.add(singer.getName());
        for (Song song: songList) list.add(song.getName());
        for (SongList songList1: songListList) list.add(songList1.getTitle());
        list.sort((Comparator.comparingInt(String::length)));
        if(list.size()>15) {
            list = list.subList(0, 15);
        }
        return Result.ok(list);
    }

    @Override
    public Result searchContent(String text) {
        List<Song> songList=songDao.getSongsByLikeText(text);
        if (!ObjectUtils.isNotEmpty(songList))songService.songLiked(songList);
        List<SongList> songListList=songListService.lambdaQuery().likeRight(SongList::getTitle,text).list();
        if(ObjectUtils.isNotEmpty(songListList))songListService.setCount(songListList);
        List<Singer> singerList=singerService.lambdaQuery().likeRight(Singer::getName,text).list();
        if(ObjectUtils.isNotEmpty(singerList)) singerService.setCount(singerList);
        Map<String,List> map=new HashMap<>();
        map.put("songs",songList);
        map.put("songLists",songListList);
        map.put("singers",singerList);
        return Result.ok(map);
    }
}
