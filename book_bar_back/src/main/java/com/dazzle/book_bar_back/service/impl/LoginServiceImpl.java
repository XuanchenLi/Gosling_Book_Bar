package com.dazzle.book_bar_back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dazzle.book_bar_back.dao.entity.Employee;
import com.dazzle.book_bar_back.dao.entity.LoginEmployee;
import com.dazzle.book_bar_back.dao.mapper.EmployeeMapper;
import com.dazzle.book_bar_back.exception.BaseException;
import com.dazzle.book_bar_back.response.ResponseResult;
import com.dazzle.book_bar_back.service.EmployeeService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

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
    @Autowired
    private EmployeeMapper employeeMapper;

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

    @Override
    public String register(Employee employee) {
        String pswReg = "^(?!\\d+$)(?![a-zA-Z]+$)(?![@$!%*#?&.()]+$)[\\dA-Za-z@$!%*#?&￥{}|^~: ;\\-=+/\\\\.()]{6,20}$";
        String phoneReg = "^1(3\\d|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$";
        String mailReg = "^[A-Za-z\\d_-]+@[a-zA-Z\\d_-]+(\\.[a-zA-Z\\d_-]+)+$";
        if (Objects.isNull(employee.getUsername()) || Objects.equals(employee.getUsername().trim(), ""))
            throw new BaseException("无效用户名");
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        int num = employeeMapper.selectCount(queryWrapper);
        if (num!=0)
            throw new BaseException("用户已存在");
        if (!Pattern.matches(pswReg, employee.getPassword()))
            throw new BaseException("密码格式错误");
        if (!Pattern.matches(phoneReg, employee.getPhone()))
            throw new BaseException("无效号码");
        if (!Pattern.matches(mailReg, employee.getEmail()))
            throw new BaseException("无效邮箱");
        BCryptPasswordEncoder crypt = new BCryptPasswordEncoder();
        employee.setPassword(crypt.encode(employee.getPassword()));
        int res = employeeMapper.insert(employee);
        return res == 0 ? "注册失败" : "注册成功";
    }
}
