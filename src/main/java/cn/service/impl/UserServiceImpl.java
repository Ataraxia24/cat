package cn.service.impl;

import cn.common.Code;
import cn.common.Result;
import cn.domain.Users;
import cn.exception.CodeNotFoundException;
import cn.mapper.UserMapper;
import cn.service.UserService;
import cn.utils.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Property;
import org.apache.ibatis.annotations.Results;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, Users> implements UserService {

    @Override
    public Result<String> enterUser(Users user) {
        log.info("user=>{}",user);
        String username = user.getUserEmail();

        String password = DigestUtils.md5DigestAsHex(user.getUserPassword().getBytes());

        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();

        //根据账号查找是否存在
        wrapper.eq(Users::getUserEmail, username);
        Users isExist = this.getOne(wrapper);

        if (isExist != null) {

            //判断密码是否相同
            if (isExist.getUserPassword().equalsIgnoreCase(password)) {
                //登录成功后注册token
                String token = JwtUtil.createToken(isExist);
                log.info("token={}",token);
                return new Result(Result.success("登录成功", Code.OK), token);
            }

            return Result.fail("密码错误", Code.BAD_REQUEST);
        }

        return Result.fail("用户不存在或已被封禁", Code.BAD_REQUEST);
    }

    @Override
    public Result<String> insertUser(HttpSession session, Users user) {
        //判断账户, 昵称, 邮件是否唯一

        LambdaQueryWrapper<Users> wrapperUserEmail = new LambdaQueryWrapper<>();
        wrapperUserEmail.eq(Users::getUserEmail, user.getUserEmail());

        if (isExist(wrapperUserEmail))    return Result.fail("邮箱重复!", Code.BAD_REQUEST);

        LambdaQueryWrapper<Users> wrapperNickName = new LambdaQueryWrapper<>();
        wrapperNickName.eq(Users::getUserNickname, user.getUserNickname());

        if (isExist(wrapperNickName))    return Result.fail("昵称重复!", Code.BAD_REQUEST);

        if (user.getCode() == null)     return Result.fail("验证码不存在!", Code.BAD_REQUEST);

        //校对前后端的验证码
        String BeforeCode = user.getCode();

        //截取邮箱地址的纯数字号码
        String userNumber = "";
        int AtIndex_ = user.getUserEmail().indexOf("@");
        if (AtIndex_ != -1) {
            userNumber = user.getUserEmail().substring(0, AtIndex_);
        }

        //当前端传来验证码, 且后端并无发送验证码, 此时会报空指针异常
        try {
            //解析session中的map
            Map<String, String> map = (Map<String, String>)session.getAttribute("code");
            String AfterCode = map.get(userNumber);

            log.info("前端code->{}, 后端code->{}", BeforeCode, AfterCode);

            if (!BeforeCode.equalsIgnoreCase(AfterCode)) {
                return Result.fail("验证码有误!", Code.BAD_REQUEST);
            }
        }catch (NullPointerException e) {
            throw new CodeNotFoundException("请发送验证码!", Code.BAD_REQUEST, e);
        }

        //加密密码
        user.setUserPassword(DigestUtils.md5DigestAsHex(user.getUserPassword().getBytes()));

        //将剩下持有的数据进行编辑
        //ToDo 默认的用户头像

        //注册时间, 单独对用户时间进行处理
        user.setUserRegisterTime(LocalDateTime.now());

        //根据生日计算年龄
        int age = (int)user.getUserBirthday().until(LocalDate.now(), ChronoUnit.YEARS);
        user.setUserAge(age);

        this.save(user);

        return Result.success("注册成功!", Code.CREATED);
    }

    //封装方法：判断传递的参数在数据库中是否存在
    private boolean isExist(LambdaQueryWrapper<Users> wrapper) {
        Users one = this.getOne(wrapper);
        if (one != null)    return true;

        return false;
    }
}
