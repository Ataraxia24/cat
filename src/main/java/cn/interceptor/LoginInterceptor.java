package cn.interceptor;

import ch.qos.logback.core.boolex.Matcher;
import cn.common.BaseContext;
import cn.common.Code;
import cn.domain.Admin;
import cn.domain.Users;
import cn.exception.AuthorizationException;
import cn.service.AdminService;
import cn.utils.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.AntPathMatcher;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminService adminService;

    public static final AntPathMatcher MATCHER = new AntPathMatcher();

    //注册拦截器
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        //获取请求头部信息
        String token = request.getHeader("Authorization");
        log.info("token-{}", token);

        //对前后台的请求进行分别处理, 如果是管理员登录前后台则开放所有权限
        //区分出前后台的不同接口, 当前台用户触碰到后台地址时拦截
        String uri = request.getRequestURI();

        //后台接口
        String[] backendApi = {"/admin/**"};

        //判断有无token
        if (token != null) {
            //判断token是否正确
            try{
                Users parse = JwtUtil.parse(token);
                BaseContext.set(parse.getUserId());

                log.info("解析token返回的对象{}",parse);

                //判断当前用户是否在管理表中
                LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Admin::getUserId, parse.getUserId());

                Admin admin = adminService.getOne(wrapper);
                log.info("admin:{}", admin);

                if (admin != null){
                    return true;
                }
                else {
                    //若非管理员访问后台接口, 强行拦截
                    for (String backendUrl : backendApi) {
                        boolean match = MATCHER.match(backendUrl, uri);
                        if (match) {
                            return false;
                        }
                    }
                }
            } catch(Exception e) {
                System.out.println(e.getMessage());
                throw new AuthorizationException("账户已过期,请重新登录!");
            }
        } else {
            throw new AuthorizationException("未授权,请登录!");
        }
        return true;
    }
}
