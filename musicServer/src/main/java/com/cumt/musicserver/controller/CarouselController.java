package com.cumt.musicserver.controller;

import com.cumt.musicserver.domain.Carousel;
import com.cumt.musicserver.service.ICarouselService;
import com.cumt.musicserver.util.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/carousel")
public class CarouselController {
    @Resource
    private ICarouselService carouselService;
    @RequestMapping("/addSingerToCarousel")
    @PreAuthorize("hasAuthority('admin')")
    public Result addSingerToCarousel(Carousel carousel){
        return carouselService.addSingerToCarousel(carousel);
    }

    @RequestMapping("/getAllCarousel")
    public Result getAllCarousel() throws JsonProcessingException {
        return carouselService.getAllCarousel();
    }

    @RequestMapping("/removeCarousel")
    @PreAuthorize("hasAuthority('admin')")
    public Result removeCarousel(Integer id){
        return carouselService.removeCarousel(id);
    }


}
