package cn.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MPConfig{

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        //DbType  数据库类型
        PaginationInnerInterceptor page = new PaginationInnerInterceptor(DbType.MYSQL);
        //单次查询最大的数量   如果我查10条，返回还是5条。
        page.setMaxLimit(5L);
        //溢出总页数后是否做处理（默认不做，true表示做处理，回到首页） false  继续请求
        page.setOverflow(false);

        MybatisPlusInterceptor mp = new MybatisPlusInterceptor();
        mp.addInnerInterceptor(new PaginationInnerInterceptor());

        return mp;
    }
}
