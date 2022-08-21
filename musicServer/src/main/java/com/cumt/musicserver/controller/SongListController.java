package com.cumt.musicserver.controller;


import com.cumt.musicserver.domain.SongList;
import com.cumt.musicserver.service.IConsumerService;
import com.cumt.musicserver.service.ISongListService;
import com.cumt.musicserver.util.Result;
import com.cumt.musicserver.util.FileUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.cumt.musicserver.util.StaticString.*;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mthddhl
 * @since 2022-05-16
 */
@RestController
@RequestMapping("/songList")
public class SongListController {
    @Autowired
    private ISongListService iSongListService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/songListGetAll")
    public Result songListGetAll(){
        List<SongList> list = iSongListService.query().list();
//        iSongListService.setCount(list);
        return Result.ok(list);
    }

    @RequestMapping("/songListGetPage")
    public Result songListGetPage(Integer currentPage,Integer pageSize,String likeNameOne){
        return iSongListService.songListGetPage(currentPage,pageSize,likeNameOne);
    }

    @PostMapping("/songListUpload")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result songListUpload(@RequestBody SongList songList){
        if (iSongListService.save(songList)) {
            return Result.ok("添加成功");
        }
        return Result.fail("添加失败");
    }

    @GetMapping("/songListGetLikeName")
    public Result songListGetLikeName(String name){
        List<String> list = new ArrayList<>();
        iSongListService.query().likeRight("title",name).select("title").list().forEach(each -> {
            list.add(each.getTitle());
        });
        return Result.ok(list);
    }

    @GetMapping("/songListGetSelected")
    public Result songListGetSelected(@RequestParam("title") String title){
        List<SongList> title1 = iSongListService.query().eq("title", title).list();
//        iSongListService.setCount(title1);
        return Result.ok(title1);
    }

    @PostMapping("/songListUpdate")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result songListUpdate(SongList songList,
                                 @RequestParam(value = "file",required = false) MultipartFile file){
        String pic=null;
        if(file!=null){
            if(!SONG_LIST_DEFAULT_PIC.equals(songList.getPic())) pic= songList.getPic();
            String s = FileUtil.uploadFile(file, "img", "songList");
            songList.setPic(s);
        }
        boolean b = iSongListService.updateById(songList);
        if(b) {
            if(ObjectUtils.isNotEmpty(pic)){
                FileUtil.deleteFile(Collections.singletonList(songList.getPic()));
            }
            return Result.ok("更新成功");
        }
        return Result.fail("更新失败");
    }

    @GetMapping("/songListDeleteById")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result songListDeleteById(@RequestParam("id") Integer id){
        SongList one = iSongListService.lambdaQuery().select(SongList::getPic).eq(SongList::getId, id).one();
        boolean b = iSongListService.removeById(id);
//        boolean update = iSongListService.update().eq("id", id).update();
        if(b){
            iSongListService.deleteCount(Collections.singletonList(id));
            if(!SONG_LIST_DEFAULT_PIC.equals(one.getPic())) FileUtil.deleteFile(Collections.singletonList(one.getPic()));
            return Result.ok("删除成功");
        }
        return Result.fail("删除失败");
    }
    @GetMapping("/songListDeleteByIds")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result songListDeleteByIds(@RequestParam("ids") List<Integer> list){
        List<SongList> list1 = iSongListService.lambdaQuery().select(SongList::getPic).in(SongList::getId,list).list();
        boolean b = iSongListService.removeByIds(list);
        if(b){
            iSongListService.deleteCount(list);
            List<String> delete=new ArrayList<>();
            list1.forEach(a-> {
                if(!SONG_LIST_DEFAULT_PIC.equals(a.getPic()) && ObjectUtils.isNotEmpty(a.getPic())) delete.add(a.getPic());
            });
            FileUtil.deleteFile(delete);
            return Result.ok("删除成功");
        }
        return Result.fail("删除失败");
    }
    @GetMapping("/songListRestart")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result songListRestart(){
        boolean logic_delete = iSongListService.update().update();
        if(logic_delete) return Result.ok();
        return Result.fail("恢复失败");
    }

    @GetMapping("/getPartSongList")
    public Result getPartSongList(){
        return iSongListService.getPartSongList();
    }

    @GetMapping("/songListGetById")
    public Result getSongListGetById(@RequestParam("id") Integer id){
        return iSongListService.getSongListGetById(id);
    }

    @GetMapping("/songListGetSongsCountIncr")
    public Result songListGetSongsCountIncr(@RequestParam("id") Integer id){
        Double aDouble = stringRedisTemplate.opsForZSet().incrementScore(SONGLIST_PLAY_COUNT, id.toString(), 1);
        if(ObjectUtils.isNotEmpty(aDouble) && aDouble.intValue()>=0) {
            return Result.ok();
        }
        return Result.fail(null);
    }

    @GetMapping("/consumerLikeSongList")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result consumerLikeSongList(@RequestParam("id") Integer id){
        return iSongListService.consumerLikeSongList(id);
    }
    @GetMapping("/consumerGetLikeSingList")
    public Result consumerGetLikeSingList(Integer id){
        return iSongListService.consumerGetLikeSingList(id);
    }
    @GetMapping("/consumerDeleteLikeSongList")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result consumerDeleteLikeSongList(Integer id){
        return iSongListService.consumerDeleteLikeSongList(id);
    }

    @GetMapping("/songListGetCountByLetter")
    public Result songListGetCountByLetter(String letter){
        return iSongListService.songListGetCountByLetter(letter);
    }
    @GetMapping("/songListGetByLetter")
    public Result songListGetByLetter(String letter,Integer currentPage,Integer pageSize){
        return iSongListService.songListGetByLetter(letter,currentPage,pageSize);
    }
    @GetMapping("/getLikeSongList")
    public Result getLikeSongList(Integer id){
        return iSongListService.getLikeSongList(id);
    }

    @GetMapping("/getTogetLikedSongList")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result getTogetLikedSongList(Integer id){
        return iSongListService.getTogetLikedSongList(id);
    }
    @PostMapping("/consumerCreatedSongList")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result consumerCreatedSongList(SongList songList,@RequestParam(value = "file",required = false) MultipartFile file){
        return iSongListService.consumerCreatedSongList(songList,file);
    }

    @GetMapping("/consumerGetCreatedSongList")
    public Result consumerGetCreatedSongList(Integer id){
        return iSongListService.consumerGetCreatedSongList(id);
    }
    @GetMapping("/consumerAddSongGetSongList")
    public Result consumerAddSongGetSongList(Integer consumerId,Integer songId){
        return iSongListService.consumerAddSongGetSongList(consumerId,songId);
    }
}

