package com.dazzle.book_bar_back.dao.mapper;

import com.dazzle.book_bar_back.dao.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmployeeMapperTest {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Test
    public void test(){
        List<Employee> employees = employeeMapper.selectList(null);
        employees.forEach(System.out::println);
    }
}