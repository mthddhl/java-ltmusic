package com.cumt.musicserver.dao;

import com.cumt.musicserver.domain.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mthddhl
 * @since 2022-06-30
 */
public interface RoleDao extends BaseMapper<Role> {

    List<String> getRolesById(@Param("id") Integer id);
}
