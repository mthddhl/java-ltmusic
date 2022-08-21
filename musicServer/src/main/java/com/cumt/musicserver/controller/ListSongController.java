package com.cumt.musicserver.controller;


import com.cumt.musicserver.service.IListSongService;
import com.cumt.musicserver.util.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mthddhl
 * @since 2022-05-16
 */
@RestController
@RequestMapping("/listSong")
public class ListSongController {
    @Resource
    private IListSongService listSongService;

    @GetMapping("/songforSongListGetAll")
    public Result songforSongListGetAll(@RequestParam("songListId") Integer id){
        return listSongService.songforSongListGetAll(id);
    }

    @GetMapping("/songforSongListGetPage")
    public Result songforSongListGetPage(@RequestParam("currentPage") Integer currentPage,
                                         @RequestParam("pageSize") Integer pageSize,
                                         @RequestParam("likeNameOne") String likeNameOne,Integer songListId){
        Integer start=(currentPage-1)*pageSize;
        return listSongService.songforSongListGetPage(start,pageSize,likeNameOne,songListId);
    }

    @GetMapping("/songforSongListGetLikeName")
    public Result songforSongListGetLikeName(String name,@RequestParam("songListid") Integer songListId){
        return listSongService.songforSongListGetLikeName(name,songListId);
    }

    @GetMapping("/songforSongListdeleteById")
    public Result songforSongListdeleteById(@RequestParam("id") Integer id){
        boolean update = listSongService.update().eq("id", id).set("delete_logic",false).update();
        return update? Result.ok("移除成功"):Result.fail("移除失败");
    }

    @GetMapping("/songforSongListdeleteByIds")
    public Result songforSongListdeleteByIds(@RequestParam("ids") List<Integer> ids){
        boolean update = listSongService.update().in("id", ids).set("delete_logic", false).update();
        return update ? Result.ok("移除成功"):Result.fail("移除失败");
    }
    @GetMapping("/songListGetSongsCount")
    public Result songListGetSongsCount(@RequestParam("id") Integer id){
        return listSongService.songListGetSongsCount(id);
    }
    @GetMapping("/consumerAddSongToSongList")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result consumerAddSongToSongList(@RequestParam("songId") Integer songId,@RequestParam("songListIds") List<Integer> songListIds){
        return listSongService.consumerAddSongToSongList(songId,songListIds);
    }
    
    @GetMapping("/consumerUpdateSongListSongs")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result consumerUpdateSongListSongs(Integer songListId,@RequestParam List<Integer> songs){
        return listSongService.consumerUpdateSongListSongs(songListId,songs);
    }
}

