package com.cumt.musicserver.service;

import com.cumt.musicserver.domain.Singer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cumt.musicserver.util.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mthddhl
 * @since 2022-05-16
 */
public interface ISingerService extends IService<Singer> {

    Result insertSinger(Singer singer,MultipartFile file);

    Result updateSinger(Singer singer,MultipartFile file);

    Result deleteSinger(Integer id);

    Result getList();

    Result getLikeName(String name);

    Result getBySex(Integer sex);

    Result getLikeNames(String name);

    Result getSingerByName(String name);

    Result getSingerPage(String likeName, Integer currentPage,Integer pageSize);

    Result deleteSingerByIds(List<Integer> ids);

    Result restore();

    Result getSingerSex();

    Result getSingerCountry();

    Result getPartSinger();

    Result getSingerById(Integer id);

    Result consumerLikeSinger(Integer id);

    Result consumerGetLikeSinger(Integer id);

    Result consumerDeleteLikeSinger(Integer id);

    Result singerGetCountByLetter(String letter,Integer sex,String location);

    Result singerGetByLetter(String letter, Integer currentPage, Integer pageSize,Integer sex,String location);

    Result getLikeSinger(Integer id);

    Result changeImg(MultipartFile multipartFile, Integer id);

    Result getTogetLikedSingers(Integer id);

    void setCount(List<Singer> list);

    void deleteCount(List<Integer> list);
}
