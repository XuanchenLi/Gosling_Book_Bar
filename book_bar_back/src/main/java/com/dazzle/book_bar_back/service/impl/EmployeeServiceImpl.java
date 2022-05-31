package com.dazzle.book_bar_back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dazzle.book_bar_back.controller.response.AllocateHistoryResponse;
import com.dazzle.book_bar_back.dao.entity.AllocateRecord;
import com.dazzle.book_bar_back.dao.entity.DepartmentPositionPair;
import com.dazzle.book_bar_back.dao.entity.Employee;
import com.dazzle.book_bar_back.dao.entity.Position;
import com.dazzle.book_bar_back.dao.mapper.AllocateRecordMapper;
import com.dazzle.book_bar_back.dao.mapper.EmployeeMapper;
import com.dazzle.book_bar_back.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private AllocateRecordMapper allocateRecordMapper;
    @Override
    public List<DepartmentPositionPair> getPositionListById(Long id) {
        return employeeMapper.selectPositionListById(id);
    }

    @Override
    public boolean deletePosition(Long eId, Long pId) {
        if (employeeMapper.deletePosition(eId, pId)) {
            return allocateRecordMapper.insert(
                    new AllocateRecord(null, eId, pId, 1, new Date())
            ) == 1;
        }
        return false;
    }

    @Override
    public boolean addPosition(Long eId, Long pId) {
        if (employeeMapper.addPosition(eId, pId) == 1) {
            return allocateRecordMapper.insert(
                    new AllocateRecord(null, eId, pId, 0, new Date())
            ) == 1;
        }
        return false;
    }

    @Override
    public List<AllocateHistoryResponse> selectAllocateHistory(Long id) {
        return allocateRecordMapper.getHistoryById(id);
    }
}
