package com.dazzle.book_bar_back.conf;

import com.dazzle.book_bar_back.response.ResponseResultInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName:WebMvcConfig
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/23 下午 5:14
 * Version V1.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 添加自定义拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ResponseResultInterceptor()).addPathPatterns("/**");
   }

}

