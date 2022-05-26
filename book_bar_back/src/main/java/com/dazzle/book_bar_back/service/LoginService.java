package com.dazzle.book_bar_back.service;

import com.dazzle.book_bar_back.dao.entity.Employee;
import com.dazzle.book_bar_back.response.ResponseResult;
import org.apache.tomcat.websocket.AuthenticationException;

public interface LoginService {
    public String login(Employee employee) throws AuthenticationException;
    public String logout();
    public String register(Employee employee);
}
