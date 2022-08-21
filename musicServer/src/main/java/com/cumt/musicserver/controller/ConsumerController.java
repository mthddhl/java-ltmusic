package com.cumt.musicserver.controller;

import com.cumt.musicserver.domain.Consumer;
import com.cumt.musicserver.service.IConsumerService;
import com.cumt.musicserver.util.Result;
import com.cumt.musicserver.util.FileUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
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
@RequestMapping("/consumer")
public class ConsumerController {
    @Resource
    private IConsumerService iConsumerService;


    @RequestMapping("/consumerListGetAll")
    public Result consumerListGetAll(){
        Integer count = iConsumerService.count();
        return Result.ok(count);
    }
    @RequestMapping("/consumerListGetPage")
    public Result consumerListGetPage(String likeName,Integer currentPage,Integer pageSize){
        return iConsumerService.consumerListGetPage(likeName,currentPage,pageSize);
    }

    @RequestMapping("/consumerGetLikeName")
    public Result consumerGetLikeName(String name){
        return iConsumerService.consumerGetLikeName(name);
    }

    @RequestMapping("/consumerInsert")
    public Result consumerInsert(@RequestBody Consumer consumer){
        return iConsumerService.consumerInsert(consumer);
    }

    @PostMapping("/consumerUpdate")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result consumerUpdate(@RequestBody Consumer consumer){
        return iConsumerService.consumerUpdate(consumer);
    }

    @PostMapping("/changeImg")
    public Result changeImg(@RequestParam("file") MultipartFile multipartFile, Integer id, HttpServletResponse httpServletResponse){
        return iConsumerService.changeImg(multipartFile,id,httpServletResponse);
    }

    @GetMapping("/consumerDelete")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result consumerDelete(@RequestParam("id") Integer id){
        return iConsumerService.consumerDelete(id);
//        return iConsumerService.removeById(id)? Result.ok("删除成功") : Result.fail("删除失败");
    }
    @GetMapping("/consumerDeleteIds")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result consumerDeleteIds(@RequestParam("ids")List<Integer> list){
        return iConsumerService.consumerDeleteIds(list);
//        return iConsumerService.removeByIds(list)? Result.ok("删除成功") : Result.fail("删除失败");
    }

    @GetMapping("/consumerRestore")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result consumerRestore() {
        return iConsumerService.consumerRestore();
    }

    @GetMapping("/getConsumerInfo")
    public Result getConsumerInfo(@RequestParam("consumerId") Integer id){
        return iConsumerService.getConsumerInfo(id);
    }

    @GetMapping("/consumerGetInfo")
    public Result consumerGetInfo(Integer id){
        return iConsumerService.consumerGetInfo(id);
    }

}

