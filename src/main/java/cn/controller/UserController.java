package cn.controller;

import cn.common.BaseContext;
import cn.common.Code;
import cn.common.RandomCode;
import cn.common.Result;
import cn.domain.Users;
import cn.service.SendMailService;
import cn.service.UserService;
import cn.utils.JwtUtil;
//import cn.utils.MailUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user")
@Slf4j
@CrossOrigin
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SendMailService sendMailService;

    @Autowired
    private TemplateEngine templateEngine;

    //登录界面
    @PostMapping("/login")
    public Result<String> login(@RequestBody @Valid Users user) {
        return userService.enterUser(user);
    }

    //注册界面
    @PostMapping("/register")
    public Result<String> register(HttpSession session, @RequestBody @Valid Users user) {
        log.info("service={}",userService);

        //校验传递的账户是否为空
        if (user == null)   return Result.fail("账户为空!", Code.NOT_FOUND);

        return userService.insertUser(session, user);
    }

    //查看所有用户信息, 搜索框根据邮箱查询
    @GetMapping("/list/{pageNum}/{pageSize}")
    public Result<List<Users>> getUserList(@PathVariable Integer pageNum, @PathVariable Integer pageSize, String email) {
        //email对应url中的query, 区别在于无注解不是必须, 有注解必须带参否则异常
        log.info(email);

        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(email), Users::getUserEmail, email);
        wrapper.orderByDesc(Users::getUserRegisterTime);                       //按注册时间升序

        Page<Users> page = new Page(pageNum, pageSize);
        userService.page(page, wrapper);

        return Result.success(page.getRecords(), Code.OK);
    }

    //根据用户id查询当前用户信息, 用于修改时回显信息
    @GetMapping("/list/{id}")
    public Result<Users> selectCurrentUser(@PathVariable Long id) { //TODO 需要给用户添加额外属性集合与留言绑定, 对收藏评论留言可做单独更改
        Users users = userService.getById(id);

        return Result.success(users, Code.OK);
    }

    //封禁用户：更改用户状态
    @GetMapping("/updateStatus/{userId}/{status}")
    public Result<String> updateUserStatus(@PathVariable Long userId, @PathVariable Integer status) {
        LambdaUpdateWrapper<Users> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Users::getUserId, userId);
        wrapper.set(Users::getDeletedFlag, status);

        boolean update = userService.update(wrapper);

        return update ? Result.success("修改成功!", Code.OK) : Result.fail("修改失败!", Code.BAD_REQUEST);
    }

    //修改用户信息
    @PutMapping("/update")
    public Result<String> update(@RequestBody Users users) {
        //获取当前用户id
        Long userId = users.getUserId();
        log.info("currentUserId=>{}",userId);

        //TODO 可更新 ip,头像,生日,昵称,性别, 头像暂时未实现
        boolean isUpdate = userService.updateById(users);
        return isUpdate == true ? Result.success("修改成功!", Code.OK) : Result.fail("修改失败,请稍后再试", Code.BAD_REQUEST);
    }

    @PutMapping("/resetPwd")
    public Result<String> resetPwd(@RequestBody @Valid @NotNull(message = "密码不能为空") String newPassword, @RequestBody  @Valid @NotNull(message = "密码不能为空") String oldPassword) {

        //获取当前用户id
        Long userId = BaseContext.get();
        log.info("currentUserId=>{}",userId);

        //获取该用户的密码并查询旧密码是否正确
        Users user = userService.getById(userId);

        oldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());

        if (!user.getUserPassword().equalsIgnoreCase(oldPassword))             return Result.fail("原始密码错误", Code.BAD_REQUEST);

        //查询旧密码是否与新密码一致, 一致驳回
        newPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());

        //完全一致, 不忽略大小写
        if (oldPassword.equals(newPassword))                                    return Result.fail("新旧密码一致", Code.BAD_REQUEST);

        //通过后修改密码
        user.setUserPassword(newPassword);

        userService.updateById(user);

        return Result.success("更改密码成功", Code.OK);
    }

    //发送验证码
    @GetMapping("/sendMsg")
    public Result<String> sendMsg(HttpSession session, @RequestParam @Valid @Email(message = "非法邮箱!") String emailAddress) {
        log.info("{}", emailAddress);

        //防止前端假数据重新校验
        if (emailAddress == null)       return Result.fail("邮箱为空", Code.BAD_REQUEST);

        //发送到该用户邮箱, 验证是否有@符号, 若没有则添加
        int AtIndex = emailAddress.indexOf("@");
        if (AtIndex == -1) {
            emailAddress = emailAddress + "@qq.com";
        }
        log.info("QQ:{}", emailAddress);

        //ToDo  这里将为验证后进入的网址
        String url = "https://www.baidu.com/";

        //截取qq号, 欢迎该用户访问
        String userNumber = "";
        int AtIndex_ = emailAddress.indexOf("@");
        if (AtIndex_ != -1) {
            userNumber = emailAddress.substring(0, AtIndex_);
        }

        //将号码传入
        String code = storageCode(session, userNumber);

        //设置html可以使用的值
        Context context = new Context();
        context.setVariable("url",url);
        context.setVariable("code", code);
        context.setVariable("user", userNumber);

        String emailTemplate = templateEngine.process("verify", context);

        boolean EmailCode = sendMailService.sendMail(emailAddress, emailTemplate, "个人博客验证码");

        if (EmailCode)      return Result.success("发送成功", Code.OK);

        return Result.fail("发送失败", Code.BAD_REQUEST);
    }

    /**
     * 将生成的code存入session中
     */
    private String storageCode(HttpSession session, String emailAddress) {
        //先将该号码之前发送的验证码清除
        session.removeAttribute("code");

        //使用map将号码与验证码进行绑定
        Map<String, String> map = new HashMap<>();
        String StringCode = RandomCode.getRandomString(5);          //生成长度为5的数字+字母验证码
        log.info("code=>{}"+StringCode);

        map.put(emailAddress, StringCode);

        //存入session
        session.setAttribute("code", map);

        //设置session有效时间, 单位为s
        session.setMaxInactiveInterval(5 * 60);

        //将生成的code返回作为页面显示, 校验时无法接收因为会重置
        return StringCode;
    }

}
