package com.cumt.musicserver.controller;


import com.cumt.musicserver.domain.Consumer;
import com.cumt.musicserver.domain.Singer;
import com.cumt.musicserver.service.ISingerService;
import com.cumt.musicserver.util.FileUtil;
import com.cumt.musicserver.util.Result;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mthddhl
 * @since 2022-05-16
 */
@RestController
@RequestMapping("/singer")
public class SingerController {

    @Resource
    private ISingerService singerService;

    @PostMapping("/insert")
    @PreAuthorize("hasAuthority('admin')")
    public Result insertSinger(Singer singer,@RequestParam(value = "file",required = false) MultipartFile file){
        return singerService.insertSinger(singer,file);
    }
//更新歌手
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('admin')")
    public Result updateSinger(Singer singer,@RequestParam(value = "file",required = false) MultipartFile file){
        return singerService.updateSinger(singer,file);
    }
//删除歌手
    @GetMapping("/delete")
    @PreAuthorize("hasAuthority('admin')")
    public Result deleteSinger(Integer id){
        return singerService.deleteSinger(id);
    }
//    批量删除
    @GetMapping("/deletes")
    @PreAuthorize("hasAuthority('admin')")
    public Result deleteSingerByIds(@RequestParam("ids") List<Integer> ids){
        if(ids==null || ids.size()==0){
            return Result.fail("无选中");
        }
        return singerService.deleteSingerByIds(ids);
    }
    @GetMapping("/getList")
    public Result getList(){
        return singerService.getList();
    }
    @GetMapping("/getLikeName")
    public Result getLikeName(String name){
        return singerService.getLikeName(name);
    }
    @GetMapping("/getLikeNames")
    public Result getLikeNames(String name){
        return singerService.getLikeNames(name);
    }

    @GetMapping("/getSingerByName")
    public Result getSingerByName(String name){
        return singerService.getSingerByName(name);
    }


    @GetMapping("/getSingerPage")
    public Result getSingerPage(String likeName,Integer currentPage,Integer pageSize){
        return singerService.getSingerPage(likeName,currentPage,pageSize);
    }

    @GetMapping("/getBySex")
    public Result getBySex(Integer sex){
        return singerService.getBySex(sex);
    }

    @PostMapping("/changeImg")
    @PreAuthorize("hasAuthority('admin')")
    public Result changeImg(@RequestParam("file") MultipartFile multipartFile,@RequestParam("id") Integer id){
        return singerService.changeImg(multipartFile,id);
    }
    @GetMapping("/restore")
    @PreAuthorize("hasAuthority('admin')")
    public Result restore(){
        return singerService.restore();
    }

    @GetMapping("/getSex")
    public Result getSingerSex(){
        return singerService.getSingerSex();
    }

    @GetMapping("/getSingerCountry")
    public Result getSingerCountry(){
        return singerService.getSingerCountry();
    }
    @GetMapping("/getPartSinger")
    public Result getPartSinger(){
        return singerService.getPartSinger();
    }
    @GetMapping("getSingerById")
    public Result getSingerById(Integer id){
        return singerService.getSingerById(id);
    }
    @GetMapping("/consumerLikeSinger")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result consumerLikeSinger(Integer id){
        return singerService.consumerLikeSinger(id);
    }
    @GetMapping("/consumerGetLikeSinger")
    public Result consumerGetLikeSinger(Integer id){
        return singerService.consumerGetLikeSinger(id);
    }
    @GetMapping("/consumerDeleteLikeSinger")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result consumerDeleteLikeSinger(Integer id){
        return singerService.consumerDeleteLikeSinger(id);
    }

    @GetMapping("/singerGetCountByLetter")
    public Result singerGetCountByLetter(String letter,Integer sex,String location){
        return singerService.singerGetCountByLetter(letter,sex,location);
    }

    @GetMapping("/singerGetByLetter")
    public Result singerGetByLetter(String letter,Integer currentPage,Integer pageSize,Integer sex,String location){
        return singerService.singerGetByLetter(letter,currentPage,pageSize,sex,location);
    }
    @GetMapping("/getLikeSinger")
    public Result getLikeSinger(Integer id){
        return singerService.getLikeSinger(id);
    }
    @GetMapping("/getTogetLikedSingers")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result getTogetLikedSingers(Integer id){
        return singerService.getTogetLikedSingers(id);
    }

}

