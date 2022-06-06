package com.dazzle.book_bar_back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dazzle.book_bar_back.controller.request.DatePair;
import com.dazzle.book_bar_back.dao.entity.Salary;
import com.dazzle.book_bar_back.dao.entity.SalaryRecord;
import com.dazzle.book_bar_back.dao.mapper.SalaryMapper;
import com.dazzle.book_bar_back.dao.mapper.SalaryRecordMapper;
import com.dazzle.book_bar_back.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName:SalaryServiceImpl
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/6/1 下午 8:42
 * Version V1.0
 */
@Service
public class SalaryServiceImpl
        extends ServiceImpl<SalaryMapper, Salary>
        implements SalaryService {

    @Autowired
    private SalaryRecordMapper salaryRecordMapper;
    @Autowired
    private SalaryMapper salaryMapper;

    @Override
    public boolean paySalary(Long eId) {
        Salary salary = salaryMapper.selectById(eId);
        SalaryRecord salaryRecord = new SalaryRecord(salary, new Date());
        return salaryRecordMapper.insert(salaryRecord) == 1;
    }

    @Override
    public List<SalaryRecord> getSalaryRecord(Long id, DatePair pair) {
        LambdaQueryWrapper<SalaryRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SalaryRecord::getEmployeeId, id)
                .ge(Objects.nonNull(pair.getStartDate()), SalaryRecord::getPayTime, pair.getStartDate())
                .le(Objects.nonNull(pair.getEndDate()), SalaryRecord::getPayTime, pair.getEndDate())
                .orderByDesc(SalaryRecord::getPayTime);
        return salaryRecordMapper.selectList(wrapper);

    }
}
