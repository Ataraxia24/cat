package cn.config;

import cn.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
/**
 * 配置拦截器
 */
public class InterceptorConfig implements WebMvcConfigurer {

    //拦截器是在spring创建前完成的, 所以注入的接口会为空, 这里将它设置为bean, 提前加载被spring管理
    @Bean
    public HandlerInterceptor handlerInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加要拦截的路径
        String[] addPathPatterns = {
                "/user/**",
                "/admin/**",
                "/article/**",
                "/interaction/**",
                "/tag/**",
                "/speak/**"
        };

        //添加无需拦截的路径
        String[] excludePathPatterns = {
                "/user/login",
                "/user/register",
                "/user/sendMsg",
                "/admin/verifyAdmin"
        };
        registry.addInterceptor(handlerInterceptor()).addPathPatterns(addPathPatterns).excludePathPatterns(excludePathPatterns);
    }
}
