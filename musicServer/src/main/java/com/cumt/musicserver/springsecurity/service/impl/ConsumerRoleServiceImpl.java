package com.cumt.musicserver.springsecurity.service.impl;

import com.cumt.musicserver.domain.ConsumerRole;
import com.cumt.musicserver.dao.ConsumerRoleDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cumt.musicserver.springsecurity.service.IConsumerRoleService;
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
public class ConsumerRoleServiceImpl extends ServiceImpl<ConsumerRoleDao, ConsumerRole> implements IConsumerRoleService {

}
