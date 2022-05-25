package com.dazzle.book_bar_back.service.impl;

import com.dazzle.book_bar_back.dao.entity.Employee;
import com.dazzle.book_bar_back.dao.entity.LoginEmployee;
import com.dazzle.book_bar_back.response.ResponseResult;
import com.dazzle.book_bar_back.service.LoginService;
import com.dazzle.book_bar_back.utils.Constants;
import com.dazzle.book_bar_back.utils.JWTUtil;
import com.dazzle.book_bar_back.utils.RedisUtil;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

/**
 * @ClassName:LoginServiceImpl
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/25 下午 8:36
 * Version V1.0
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String login(Employee employee) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                employee.getUsername(), employee.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authentication)) {
            throw new AuthenticationException("用户名或密码错误");
        }
        //id生成token
        LoginEmployee loginEmployee = (LoginEmployee) authentication.getPrincipal();
        String userId = loginEmployee.getEmployee().getId().toString();
        String jwt = JWTUtil.createJWT(userId);
        //存入redis
        redisUtil.set("Employee_login:" + userId, loginEmployee, Constants.CACHE_1DAY);
        //返回前端
        return jwt;
    }

    @Override
    public String logout() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder
                        .getContext().getAuthentication();
        LoginEmployee loginEmployee = (LoginEmployee) authenticationToken.getPrincipal();
        redisUtil.remove("Employee_login:" + loginEmployee.getEmployee().getId());
        return "登出成功";
    }
}
