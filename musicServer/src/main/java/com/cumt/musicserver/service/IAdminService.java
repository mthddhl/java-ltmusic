package com.cumt.musicserver.service;

import com.cumt.musicserver.domain.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cumt.musicserver.util.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mthddhl
 * @since 2022-05-16
 */
public interface IAdminService extends IService<Admin> {

    Result login(Admin admin);
}
