package com.cumt.musicpay.service.impl;

import com.cumt.musicpay.domain.Product;
import com.cumt.musicpay.dao.ProductDao;
import com.cumt.musicpay.service.IProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cumt.musicpay.util.Result;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.cumt.musicpay.util.OrderStatus.VIP_PRODUCT_COUNT;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mthddhl
 * @since 2022-08-07
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductDao, Product> implements IProductService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result addVIPProduct(Product product) {

        product.setCreateTime(LocalDateTime.now());

        product.setUpdateTime(LocalDateTime.now());

        if(save(product)){
            long seconds = Duration.between(LocalDateTime.now(), product.getExpireTime()).getSeconds();
            stringRedisTemplate.opsForValue().set(VIP_PRODUCT_COUNT+product.getId(),
                    product.getCount().toString(),seconds, TimeUnit.SECONDS);
            return Result.ok("添加成功");
        }
        return Result.fail("添加失败");
    }

    public final static String ALL_COST_VIP="全款会员";
    public final static String DISCOUNT_VIP="限时优惠";

    @Override
    public Result getAllVIPProduct() {
        List<Product> VIPIndex=lambdaQuery().eq(Product::getType,ALL_COST_VIP).list();
        List<Product> discountIndex=lambdaQuery().eq(Product::getType,DISCOUNT_VIP).list();
        getAndRemove(discountIndex);
        Map<String,List<Product>> res=new HashMap<>();
        res.put("VIPIndex",VIPIndex);
        res.put("discountVip",discountIndex);
        return Result.ok(res);
    }

    @Override
    public Result getAllProduct() {
        List<Product> list = list();
        getAndRemove(list);
        return Result.ok(list);
    }

    public void getAndRemove(List<Product> list){
        List<Long> removeList=new ArrayList<>();
        for (Product p:list){
            if(LocalDateTime.now().isAfter(p.getExpireTime())){
                removeList.add(p.getId());
                list.remove(p);
            }
        }
        removeByIds(removeList);
    }
}
