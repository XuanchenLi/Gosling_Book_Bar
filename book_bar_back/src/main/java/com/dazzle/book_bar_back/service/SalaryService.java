package com.dazzle.book_bar_back.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dazzle.book_bar_back.controller.request.DatePair;
import com.dazzle.book_bar_back.dao.entity.Salary;
import com.dazzle.book_bar_back.dao.entity.SalaryRecord;

import java.util.List;

public interface SalaryService extends IService<Salary> {
    boolean paySalary(Long eId);

    List<SalaryRecord> getSalaryRecord(Long id, DatePair pair);
}
