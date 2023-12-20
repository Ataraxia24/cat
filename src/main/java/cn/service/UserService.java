package cn.service;

import cn.common.Result;
import cn.domain.Users;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;

public interface UserService extends IService<Users> {
    //登录
    Result<String> enterUser(Users user);

    //注册
    Result<String> insertUser(HttpSession session, Users user);

}
