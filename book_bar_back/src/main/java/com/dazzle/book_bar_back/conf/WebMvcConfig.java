package com.dazzle.book_bar_back.conf;

import com.dazzle.book_bar_back.response.ResponseResultInterceptor;
import com.dazzle.book_bar_back.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * @ClassName:WebMvcConfig
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/23 下午 5:14
 * Version V1.0
 */
@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 添加自定义拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ResponseResultInterceptor()).addPathPatterns("/**");
   }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File path = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        String p = path.getAbsolutePath() + "\\static\\";
        p = p.replace('\\', '/');
        log.info(p);
        registry.addResourceHandler("/api/static/**")//前端url访问的路径，若有访问前缀，在访问时添加即可，这里不需添加。
                .addResourceLocations("file:" + p);//映射的服务器存放图片目录。
    }
}

