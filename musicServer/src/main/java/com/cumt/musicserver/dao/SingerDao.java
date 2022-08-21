package com.cumt.musicserver.dao;

import com.cumt.musicserver.domain.Singer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mthddhl
 * @since 2022-05-16
 */
public interface SingerDao extends BaseMapper<Singer> {

    Integer singerGetCountByLetter(@Param("letter")String letter,
                                   @Param("sex")Integer sex,
                                   @Param("location")String location);

    List<Singer> singerGetByLetter(@Param("letter") String letter,
                                   @Param("currentPage") Integer currentPage,
                                   @Param("pageSize") Integer pageSize,
                                   @Param("sex")Integer sex,
                                   @Param("location")String location);
}
