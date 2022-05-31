package com.dazzle.book_bar_back.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dazzle.book_bar_back.controller.response.AllocateHistoryResponse;
import com.dazzle.book_bar_back.dao.entity.AllocateRecord;
import com.dazzle.book_bar_back.dao.entity.DepartmentPositionPair;
import com.dazzle.book_bar_back.dao.entity.Employee;
import com.dazzle.book_bar_back.dao.entity.Position;
import com.dazzle.book_bar_back.response.ResponseResult;
import com.dazzle.book_bar_back.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName:EmployeeController
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/30 上午 11:43
 * Version V1.0
 */
@RestController
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employee/position_{id}")
    @ResponseResult
    public List<DepartmentPositionPair> getPositionList(@PathVariable("id") Long id) {
        return employeeService.getPositionListById(id);
    }

    @GetMapping("/employee/delete_position/{employee_id}_{position_id}")
    @ResponseResult
    public boolean
    deletePosition(
            @PathVariable("employee_id") Long eId,
            @PathVariable("position_id") Long pId
    ) {
        return employeeService.deletePosition(eId, pId);
    }
    @GetMapping("/employee/add_position/{employee_id}_{position_id}")
    @ResponseResult
    public boolean
    addPosition(
            @PathVariable("employee_id") Long eId,
            @PathVariable("position_id") Long pId
    ) {
        return employeeService.addPosition(eId, pId);
    }

    @GetMapping("/employee/allocate_history/{id}")
    @ResponseResult
    public List<AllocateHistoryResponse>
    getAllocateHistory(@PathVariable("id") Long id) {
        return employeeService.selectAllocateHistory(id);
    }
}
