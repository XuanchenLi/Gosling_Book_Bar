package com.dazzle.book_bar_back.dao.mapper;

import com.dazzle.book_bar_back.dao.entity.Employee;
import com.dazzle.book_bar_back.exception.BaseException;
import com.dazzle.book_bar_back.utils.JWTUtil;
import io.jsonwebtoken.Claims;
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
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwZTUzYzg2OTRiYzM0NDlkOGRkZTAwYWM3OTk3NGVmYyIsInN1YiI6IjIiLCJpc3MiOiJzZyIsImlhdCI6MTY1MzY2MDk2NiwiZXhwIjoxNjUzNjY0NTY2fQ.UN_Lv0mMtcIwn5txzZHUfWBBvhLIRmHL0pHMDXCMSfI";
        try {
            Claims claims = JWTUtil.parseJWT(token);
            String userId = claims.getSubject();
        } catch (Exception e){
            throw new BaseException("token非法");
        }
    }

}
