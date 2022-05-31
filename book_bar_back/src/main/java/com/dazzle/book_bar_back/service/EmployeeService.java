package com.dazzle.book_bar_back.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dazzle.book_bar_back.controller.request.SearchEmployeeBag;
import com.dazzle.book_bar_back.controller.response.AllocateHistoryResponse;
import com.dazzle.book_bar_back.dao.entity.AllocateRecord;
import com.dazzle.book_bar_back.dao.entity.DepartmentPositionPair;
import com.dazzle.book_bar_back.dao.entity.Employee;
import com.dazzle.book_bar_back.dao.entity.Position;

import java.util.List;

public interface EmployeeService extends IService<Employee> {

    List<DepartmentPositionPair> getPositionListById(Long id);

    boolean deletePosition(Long eId, Long pId);

    boolean addPosition(Long eId, Long pId);

    List<AllocateHistoryResponse> selectAllocateHistory(Long id);
}
