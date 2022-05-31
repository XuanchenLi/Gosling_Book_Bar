package com.dazzle.book_bar_back.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dazzle.book_bar_back.controller.request.SearchEmployeeBag;
import com.dazzle.book_bar_back.dao.entity.Employee;

import java.util.List;

public interface SearchEmployeeService {
    Page<Employee> search(SearchEmployeeBag bag);

    List<String> recommend(SearchEmployeeBag bag);
}
