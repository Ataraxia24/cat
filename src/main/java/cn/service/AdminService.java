package cn.service;

import cn.common.Result;
import cn.domain.Admin;
import cn.domain.Users;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;

public interface AdminService extends IService<Admin> {
    //登录
    Result<String> enterUser(Admin admin);

    //注册
    Result<String> insertUser(Admin admin);

}
