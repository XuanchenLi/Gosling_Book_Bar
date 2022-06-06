package com.dazzle.book_bar_back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dazzle.book_bar_back.controller.request.SearchEmployeeBag;
import com.dazzle.book_bar_back.dao.entity.Employee;
import com.dazzle.book_bar_back.dao.mapper.EmployeeMapper;
import com.dazzle.book_bar_back.service.EmployeeService;
import com.dazzle.book_bar_back.service.SearchEmployeeService;
import com.dazzle.book_bar_back.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName:SearchEmployeeServiceImpl
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/29 下午 3:23
 * Version V1.0
 */

@Service
public class SearchEmployeeServiceImpl implements SearchEmployeeService {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Override
    public Page<Employee> search(SearchEmployeeBag bag) {
        if (StringUtils.isBlank(bag.getQuery()) || Objects.isNull(bag.getQuery()))
        {
            bag.setQuery("%");
        }

        Page<Employee> page = new Page<>(bag.getCurrentPage(), 10);
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Employee::getId, Employee::getUsername,
                Employee::getSex, Employee::getStartDate,
                Employee::getEndDate, Employee::getStatus)
                .ge(Objects.nonNull(bag.getStartDate()),
                        Employee::getStartDate, bag.getStartDate())
                .le(Objects.nonNull(bag.getEndDate()),
                        Employee::getStartDate, bag.getEndDate())
                .eq(bag.getTypeSelect() == 1,
                        Employee::getStatus, 0)
                .eq(bag.getTypeSelect() == 2,
                        Employee::getStatus, 1)
                .like(bag.getRegionSelect() == 0,
                        Employee::getUsername, bag.getQuery())
                .eq(bag.getRegionSelect() == 1,
                        Employee::getId, StringUtil.getLeadingLong(bag.getQuery(), 10));
        employeeMapper.selectPage(page, wrapper);
        return page;
    }

    @Override
    public List<String> recommend(SearchEmployeeBag bag) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Employee::getUsername)
                .eq(bag.getTypeSelect() == 1,
                        Employee::getStatus, 0)
                .eq(bag.getTypeSelect() == 2,
                        Employee::getStatus, 1)
                .ge(Objects.nonNull(bag.getStartDate()),
                        Employee::getStartDate, bag.getStartDate())
                .le(Objects.nonNull(bag.getEndDate()),
                        Employee::getStartDate, bag.getEndDate())
                .like(Employee::getUsername, bag.getQuery())
                .last("limit 5");
        return employeeMapper.selectList(wrapper)
                .stream().map(Employee::getUsername)
                .collect(Collectors.toList());
    }
}
