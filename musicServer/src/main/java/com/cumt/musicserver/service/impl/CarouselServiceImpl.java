package com.cumt.musicserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cumt.musicserver.dao.CarouselDao;
import com.cumt.musicserver.domain.Carousel;
import com.cumt.musicserver.service.ICarouselService;
import com.cumt.musicserver.util.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

import static com.cumt.musicserver.util.StaticString.CAROUSEL_REDIS_CACHE;

@Service
public class CarouselServiceImpl extends ServiceImpl<CarouselDao, Carousel> implements ICarouselService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private ObjectMapper objectMapperNoTime=new ObjectMapper();

    @Override
    public Result addSingerToCarousel(Carousel carousel) {
        if(lambdaQuery().count()>5) return Result.fail("数量已满，青清理数量后再次添加");
        Carousel one = lambdaQuery().eq(Carousel::getType, carousel.getType())
                .eq(Carousel::getToId, carousel.getToId()).one();
        if(ObjectUtils.isNotEmpty(one)){
            return Result.fail("已添加，请勿重复添加");
        }
        if(save(carousel)){
            stringRedisTemplate.delete(CAROUSEL_REDIS_CACHE);
            return Result.ok("添加成功");
        }
        return Result.fail("添加失败");
    }

    @Override
    public Result getAllCarousel() {
        if(BooleanUtils.isFalse(stringRedisTemplate.hasKey(CAROUSEL_REDIS_CACHE))) {
            List<Carousel> list = lambdaQuery().list();
            String s;
            try {
                s = objectMapperNoTime.writeValueAsString(list);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            if (ObjectUtils.isNotEmpty(s)) {
                stringRedisTemplate.opsForValue().set(CAROUSEL_REDIS_CACHE, s);
            }
            return Result.ok(list);
        }
        String s = stringRedisTemplate.opsForValue().get(CAROUSEL_REDIS_CACHE);
        List res;
        try {
            res=objectMapperNoTime.readValue(s,List.class);
        } catch (JsonProcessingException e) {
            List<Carousel> list = lambdaQuery().list();
            return Result.ok(list);
        }
        return Result.ok(res);
    }

    @Override
    public Result removeCarousel(Integer id) {
        if(removeById(id)){
            Boolean delete = stringRedisTemplate.delete(CAROUSEL_REDIS_CACHE);
            if(BooleanUtils.isTrue(delete)){
                return Result.ok("移除成功");
            }
        }
        return Result.fail("移除失败");
    }
}
