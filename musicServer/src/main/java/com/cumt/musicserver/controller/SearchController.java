package com.cumt.musicserver.controller;

import com.cumt.musicserver.service.ISearchService;
import com.cumt.musicserver.util.FileUtil;
import com.cumt.musicserver.util.Result;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/search")
public class SearchController {
    @Resource
    private ISearchService iSearchService;

    @GetMapping("/searchGetAll")
    public Result searchGetAll(String text){
        return iSearchService.searchGetAll(text);
    }

    @GetMapping("/searchContent")
    public Result searchContent(String text){
        if(ObjectUtils.isNotEmpty(text)) {
            return iSearchService.searchContent(text);
        }
        return Result.ok();
    }
    @GetMapping("/fileIsExist")
    public Result fileIsExist(String url){
        if(FileUtil.fileIsExist(url)){
            return Result.ok();
        }else {
            return Result.fail("文件不存在或异常");
        }
    }
}
