package cn.service.impl;

import cn.common.Code;
import cn.common.Result;
import cn.domain.Admin;
import cn.domain.Users;
import cn.mapper.AdminMapper;
import cn.service.AdminService;
import cn.service.UserService;
import cn.utils.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private UserService userService;

    @Override
    public Result<String> enterUser(Admin admin) {
        String username = admin.getAdminUsername();
        String password = admin.getAdminPassword();

        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getAdminUsername, username);

        Admin one = this.getOne(wrapper);

        if (one == null)    return Result.fail("账户不存在!", Code.BAD_REQUEST);

        if (!password.equalsIgnoreCase(one.getAdminPassword()))     return Result.fail("密码错误!", Code.BAD_REQUEST);

        //成功后发送该管理员对应的用户token
        Users user = userService.getById(one.getUserId());
        if (user == null) {     //管理员开放前后台所有权限, 找到管理表却用户表为null, 说明已注销当前用户
            return Result.success("请先注册用户信息!", Code.BAD_REQUEST);
        }
        String token = JwtUtil.createToken(user);

        return new Result<>(Result.success("登录成功!", Code.OK), token);
    }

    @Override
    public Result<String> insertUser(Admin admin) {
        //判断账号是否重复
        LambdaQueryWrapper<Admin> wrapperUsername = new LambdaQueryWrapper<>();
        wrapperUsername.eq(Admin::getAdminUsername, admin.getAdminUsername());
        Admin adminUsername = this.getOne(wrapperUsername);

        if (adminUsername != null)          return Result.fail("用户已存在!", Code.BAD_REQUEST);

        //对密码进行加密处理
        admin.setAdminPassword(DigestUtils.md5DigestAsHex(admin.getAdminPassword().getBytes()));

        this.save(admin);
        return Result.success("注册成功!", Code.CREATED);
    }
}
