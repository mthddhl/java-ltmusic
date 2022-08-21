package com.cumt.musicserver.controller;


import com.cumt.musicserver.domain.Admin;
import com.cumt.musicserver.service.IAdminService;
import com.cumt.musicserver.util.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mthddhl
 * @since 2022-05-16
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
//    ConcurrentHashMap

    @Resource
    private IAdminService adminService;

//    @RequestMapping("/test")
//    public String test(){
//        StringBuilder s=new StringBuilder();
//        adminService.list().forEach(each->{
//            s.append(each.toString());
//        });
//        return s.toString();
//    }


    @PostMapping("/login")
    public Result login(Admin admin){
        return adminService.login(admin);
    }



}

