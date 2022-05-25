package com.dazzle.book_bar_back;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ServletComponentScan
@EnableCaching
@EnableSwagger2
@MapperScan("com.dazzle.book_bar_back.**.mapper")//扫描接口所在路径
public class BookBarBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookBarBackApplication.class, args);
    }

}
