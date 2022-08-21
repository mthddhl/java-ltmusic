package com.cumt.musicserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cumt.musicserver.domain.Carousel;
import com.cumt.musicserver.util.Result;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ICarouselService extends IService<Carousel> {
    Result addSingerToCarousel(Carousel carousel);

    Result getAllCarousel() throws JsonProcessingException;

    Result removeCarousel(Integer id);
}
