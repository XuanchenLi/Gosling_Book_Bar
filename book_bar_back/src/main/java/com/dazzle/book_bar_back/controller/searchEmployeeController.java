package com.dazzle.book_bar_back.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dazzle.book_bar_back.controller.request.SearchEmployeeBag;
import com.dazzle.book_bar_back.controller.response.SearchEmployeeResponse;
import com.dazzle.book_bar_back.dao.entity.BasicEmployee;
import com.dazzle.book_bar_back.dao.entity.Employee;
import com.dazzle.book_bar_back.response.ResponseResult;
import com.dazzle.book_bar_back.service.EmployeeService;
import com.dazzle.book_bar_back.service.SearchEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName:searchEmployeeController
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/29 下午 3:22
 * Version V1.0
 */
@RestController
@Slf4j
public class searchEmployeeController {
    @Autowired
    private SearchEmployeeService searchEmployeeService;
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/search/employee")
    @ResponseResult
    public SearchEmployeeResponse searchEmployee(@RequestBody SearchEmployeeBag bag) {
        Page<Employee> page = searchEmployeeService.search(bag);
        List<BasicEmployee> list = page.getRecords()
                .stream().map(BasicEmployee::new)
                .collect(Collectors.toList());
        SearchEmployeeResponse response = new SearchEmployeeResponse(
                list,
                page.getCurrent(),
                page.getTotal(),
                page.getPages(),
                page.hasPrevious(),
                page.hasNext()
        );
        return response;
    }

    @PostMapping("/search/employee/recommend")
    @ResponseResult
    public List<String> getRecommend(@RequestBody SearchEmployeeBag bag) {
        return searchEmployeeService.recommend(bag);
    }

    @GetMapping("/search/employee/{id}")
    @ResponseResult
    public Employee getEmployeeInfo(@PathVariable("id") Long id) {
        return employeeService.getById(id);
    }
}
