package com.cumt.musicserver.controller;


import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.cumt.musicserver.domain.Song;
import com.cumt.musicserver.service.ISongService;
import com.cumt.musicserver.util.Result;
import com.cumt.musicserver.util.FileUtil;
import io.minio.errors.*;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
@RequestMapping("/song")
public class SongController {
    @Resource
    private ISongService songService;

//    @PostMapping("/uploadMusic")
//    @PreAuthorize("hasAuthority('admin')")
//    public Result uploadMusic(@RequestParam("file")MultipartFile multipartFile,@RequestParam("id") Integer id){
//
//        return songService.updateMusic(multipartFile,id);
//    }
//
//    @PostMapping("/uploadImg")
//    @PreAuthorize("hasAuthority('admin')")
//    public Result uploadImg(@RequestParam("file")MultipartFile multipartFile,@RequestParam("id") Integer id){
//        return songService.updateImg(multipartFile,id);
//    }


    @GetMapping("/convertVipStatus")
    @PreAuthorize("hasAuthority('admin')")
    public Result convertVipStatus(Integer id){
        return songService.convertVipStatus(id);
    }


    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('admin')")
    public Result upload(Song song,
                         @RequestParam(value = "musicFile",required = false) MultipartFile musicFile,
                         @RequestParam(value = "picFile",required = false) MultipartFile picFile
                         ){
        return songService.upload(musicFile,picFile,song);
    }

    @GetMapping("/getAllList")
    public Result getAllList(Integer singerId){
        return songService.getAllList(singerId);
    }

    @GetMapping("/getSongPage")
    public Result getSongPage(Integer currentPage,Integer pageSize,Integer singerId){
        return songService.getSongPage(currentPage,pageSize,singerId);
    }

    @GetMapping("/songGetNameAndIntro")
    public Result songGetNameAndIntro(String name,Integer singerId){
        return songService.songGetNameAndIntro(name,singerId);
    }
    @GetMapping("/getSongLikes")
    public Result getSongLikes(String name){
        return songService.getSongLikes(name);
    }

    @PostMapping("/updateSong")
    @PreAuthorize("hasAuthority('admin')")
    public Result updateSong(Song song,
                             @RequestParam(value = "musicFile",required = false) MultipartFile musicFile,
                             @RequestParam(value = "picFile",required = false) MultipartFile picFile
                             ){
        return songService.updateSong(song,musicFile,picFile);
    }
    @GetMapping("/deleteSongById")
    @PreAuthorize("hasAuthority('admin')")
    public Result deleteSongById(String id){
        return songService.deleteSongById(id);
    }
    @GetMapping("/deleteSongByIds")
    @PreAuthorize("hasAuthority('admin')")
    public Result deleteSongByIds(@RequestParam("ids") List<Integer> ids){
        return songService.deleteSongByIds(ids);
    }
    @GetMapping("/restoreSong")
    @PreAuthorize("hasAuthority('admin')")
    public Result restoreSong(){
        return songService.restoreSong();
    }

    @GetMapping("/getSongCountForSinger")
    public Result getSongCountForSinger(){
        return songService.getSongCountForSinger();
    }

    @GetMapping("/getPartSong")
    public Result getPartSong(){
        return songService.getPartSong();
    }

    @GetMapping("/getSongBySongListIdPage")
    public Result getSongBySongListId(Integer currentPage,Integer pageSize,Integer songListId){
            return songService.getSongBySongListId(currentPage,pageSize,songListId);
    }
    @GetMapping("/getSongCountForSingerId")
    public Result getSongCountForSingerId(Integer id){
        Integer count = songService.lambdaQuery().eq(Song::getSingerId, id).count();
        if(count!=null){
            return Result.ok(count);
        }
        return Result.ok(0);
    }
    @GetMapping("/consumerLikeSong")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result consumerLikeSong(@RequestParam("songId") Integer songId,
                                   @RequestParam("isLiked") Boolean isLike){
        return songService.consumerLikeSong(songId,isLike);
    }
    @GetMapping("/getConsumerLikeSong")
    public Result getConsumerLikeSong(@RequestParam("id") Integer id){
        return songService.getConsumerLikeSong(id);
    }
//    @GetMapping("/songDownload")
//    public void songDownload(String name, String url, HttpServletResponse httpServletResponse) throws IOException {
//        try {
//            FileUtil.downloadMP3(name,url,httpServletResponse);
//        } catch (IOException e) {
//            httpServletResponse.setContentType("text/html;charset=utf-8");
//            httpServletResponse.setStatus(500);
//            PrintWriter pw=httpServletResponse.getWriter();
//            Result res = Result.ok("下载错误,文件或已丢失");
//            pw.println(res);
//            pw.close();
//        }
//    }
}

