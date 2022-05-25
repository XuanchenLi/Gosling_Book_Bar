package com.dazzle.book_bar_back.conf;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName:MybatisPlusConfig
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/25 下午 4:50
 * Version V1.0
 * 分页配置
 */
@Configuration
public class MybatisPlusConfig {
    // 拦截查询功能实现分页
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(
                new PaginationInnerInterceptor(DbType.MYSQL)
        );
        /*
        //添加乐观锁插件
        interceptor.addInnerInterceptor(
        new OptimisticLockerInnerInterceptor()
        );
         */
        return interceptor;
    }


}
