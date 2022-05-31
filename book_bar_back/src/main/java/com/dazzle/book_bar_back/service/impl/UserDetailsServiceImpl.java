package com.dazzle.book_bar_back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dazzle.book_bar_back.dao.entity.Employee;
import com.dazzle.book_bar_back.dao.entity.LoginEmployee;
import com.dazzle.book_bar_back.dao.mapper.EmployeeMapper;
import com.dazzle.book_bar_back.dao.mapper.MenuMapper;
import com.dazzle.book_bar_back.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName:UserDetailsServiceImpl
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/25 下午 9:51
 * Version V1.0
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, username);
        Employee employee = employeeMapper.selectOne(queryWrapper);
        // log.info(username);
        if (Objects.isNull(employee)) {
            throw new BaseException("用户名或密码错误");
        }
        List<String> list = menuMapper.selectPermByUserId(employee.getId());
        return new LoginEmployee(employee, list);
    }
}
