package com.cumt.musicpay.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cumt.musicpay.domain.Order;
import com.cumt.musicpay.service.IOrderService;
import com.cumt.musicpay.util.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mthddhl
 * @since 2022-08-07
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private IOrderService orderService;

    @RequestMapping("/getOrderCount")
    @PreAuthorize("hasAuthority('admin')")
    public Result getOrderCount(){
        int count = orderService.count();
        return Result.ok(count);
    }

    @RequestMapping("/getOrderPage")
    @PreAuthorize("hasAuthority('admin')")
    public Result getOrderPage(Integer currentPage,Integer pageSize){
        List<Order> records = orderService.page(new Page<>(currentPage, pageSize)).getRecords();
        return Result.ok(records);
    }

    @GetMapping("/getOrderByConId")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result getOrderByConId(){
        return orderService.getOrderByConId();
    }

    @GetMapping("/payOrderByOrderNo")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result payOrderByOrderNo(String orderNo){
       return orderService.payOrderByOrderNo(orderNo);
    }

    @GetMapping("/closeOrderByOrderNo")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result closeOrderByOrderNo(String orderNo){
        return orderService.closeOrderByOrderNo(orderNo);
    }

    @GetMapping("/flushOrderByOrderNo")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result flushOrderByOrderNo(String orderNo){
        return orderService.flushOrderByOrderNo(orderNo);
    }
}

