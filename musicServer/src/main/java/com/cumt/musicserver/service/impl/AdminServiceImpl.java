package com.cumt.musicserver.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.cumt.musicserver.domain.Admin;
import com.cumt.musicserver.dao.AdminDao;
import com.cumt.musicserver.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cumt.musicserver.util.Result;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mthddhl
 * @since 2022-05-16
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminDao, Admin> implements IAdminService {

    @Override
    public Result login(Admin admin) {
        String username=admin.getName();
        String password=admin.getPassword();
        if(username==null){
            return Result.fail("请输入用户名");
        }
        Admin one = query().eq("name", username).eq("password", password).one();
        if(one==null){
            return Result.fail("账号或密码错误");
        }
        return Result.ok("登陆成功");

    }
}
