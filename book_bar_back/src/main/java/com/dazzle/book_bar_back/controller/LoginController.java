package com.dazzle.book_bar_back.controller;

import com.dazzle.book_bar_back.dao.entity.Employee;
import com.dazzle.book_bar_back.response.ResponseResult;
import com.dazzle.book_bar_back.response.Result;
import com.dazzle.book_bar_back.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @ClassName:LoginController
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/25 下午 8:35
 * Version V1.0
 */
@RestController
@Slf4j
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/employee/login")
    @ResponseResult
    public HashMap<String, String>
    login(@RequestBody Employee employee) throws AuthenticationException {
        HashMap<String, String> res = new HashMap<>();
        res.put("token", loginService.login(employee));
        return res;
    }

    @ResponseResult
    @GetMapping("/employee/logout")
    public String logout(){
        return loginService.logout();
    }
}
