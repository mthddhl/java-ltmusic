package com.cumt.musicserver.springsecurity.service.impl;

import com.cumt.musicserver.domain.Role;
import com.cumt.musicserver.dao.RoleDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cumt.musicserver.springsecurity.service.IRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mthddhl
 * @since 2022-06-30
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements IRoleService {

}
