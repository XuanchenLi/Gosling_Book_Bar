package com.dazzle.book_bar_back.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dazzle.book_bar_back.controller.request.DatePair;
import com.dazzle.book_bar_back.dao.entity.LoginEmployee;
import com.dazzle.book_bar_back.dao.entity.Salary;
import com.dazzle.book_bar_back.dao.entity.SalaryRecord;
import com.dazzle.book_bar_back.exception.BaseException;
import com.dazzle.book_bar_back.response.ResponseResult;
import com.dazzle.book_bar_back.service.SalaryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.log4j.jmx.LayoutDynamicMBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName:SalaryController
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/6/1 下午 8:41
 * Version V1.0
 */
@RestController
@Slf4j
public class SalaryController {
    @Autowired
    private SalaryService salaryService;

    @GetMapping("/salary/personal")
    @ResponseResult
    @PreAuthorize("hasAuthority('salary::list::personal')")
    public Salary getEmployeeSalary() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginEmployee employee = (LoginEmployee) authentication.getPrincipal();
        // log.info(employee.getEmployee().getId().toString());
        LambdaQueryWrapper<Salary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Salary::getEmployeeId, employee.getEmployee().getId());
        return salaryService.getOne(wrapper);
    }

    @PostMapping("/salary/save_or_update")
    @ResponseResult
    @PreAuthorize("hasAuthority('salary::add')")
    public boolean saveOrUpdateSalary(@RequestBody Salary salary) {
        return salaryService.saveOrUpdate(salary);
    }

    @GetMapping("/salary/pay_salary/{id}")
    @ResponseResult
    @PreAuthorize("hasAuthority('salary::pay')")
    public boolean paySalary(@PathVariable("id") Long eId) {
        return salaryService.paySalary(eId);
    }

    @PostMapping("/salary/personal_history")
    @ResponseResult
    @PreAuthorize("hasAuthority('salary::list::personal')")
    public List<SalaryRecord> getSalaryHistory(@RequestBody DatePair pair) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginEmployee employee = (LoginEmployee) authentication.getPrincipal();

        return salaryService.getSalaryRecord(employee.getEmployee().getId(), pair);
    }

    @GetMapping("/salary/{id}")
    @ResponseResult
    @PreAuthorize("hasAuthority('salary::list')")
    public Salary getSalaryByEmployeeId(@PathVariable("id") Long id) {
        LambdaQueryWrapper<Salary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Salary::getEmployeeId, id);
        return salaryService.getOne(wrapper);
    }

}
