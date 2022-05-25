package com.dazzle.book_bar_back.dao.mapper;

import com.dazzle.book_bar_back.dao.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

/**
 * @ClassName:CryptTest
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/25 下午 11:15
 * Version V1.0
 */
@SpringBootTest
@Slf4j
public class CryptTest {
    @Test
    public void test(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        log.info(encoder.encode("123"));
    }

}
