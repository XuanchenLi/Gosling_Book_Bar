package com.dazzle.book_bar_back.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dazzle.book_bar_back.dao.entity.Employee;
import com.dazzle.book_bar_back.dao.mapper.EmployeeMapper;
import com.dazzle.book_bar_back.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName:
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/25 下午 3:37
 * Version V1.0
 */
@Service
@Slf4j
public class EmployeeServiceImpl
        extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService {


}
