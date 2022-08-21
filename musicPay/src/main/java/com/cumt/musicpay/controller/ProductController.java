package com.cumt.musicpay.controller;


import com.cumt.musicpay.domain.Product;
import com.cumt.musicpay.service.IProductService;
import com.cumt.musicpay.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mthddhl
 * @since 2022-08-07
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    private IProductService productService;

    @RequestMapping("/getAllProduct")
    public Result getAllProduct(){
        return productService.getAllProduct();
    }

    @PostMapping("/addVIPProduct")
    public Result addVIPProduct(@RequestBody Product product){
        return productService.addVIPProduct(product);
    }

    @GetMapping("/removeVIPProduct")
    public Result removeVIPProduct(Integer id){
        if(id==1 || id==2 || id==3){
            return Result.fail("此产品不可删除");
        }
        if(productService.removeById(id)){
            return Result.ok();
        }
        return Result.fail("移除失败");
    }

    @GetMapping("/getAllVIPProduct")
    public Result getAllVIPProduct(){
        return productService.getAllVIPProduct();
    }


}

