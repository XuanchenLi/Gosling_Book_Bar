package com.dazzle.book_bar_back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dazzle.book_bar_back.controller.response.AllocateHistoryResponse;
import com.dazzle.book_bar_back.dao.entity.*;
import com.dazzle.book_bar_back.dao.mapper.AllocateRecordMapper;
import com.dazzle.book_bar_back.dao.mapper.EmployeeMapper;
import com.dazzle.book_bar_back.exception.BaseException;
import com.dazzle.book_bar_back.service.EmployeeService;
import com.dazzle.book_bar_back.utils.Constants;
import com.dazzle.book_bar_back.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

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
    @Autowired
    private RedisUtil redisUtil;
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

    @Override
    public boolean editProfile(Employee employee) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginEmployee oldEmployee = (LoginEmployee) authentication.getPrincipal();
        Employee em = oldEmployee.getEmployee();
        em.setEmail(employee.getEmail());
        em.setPhone(employee.getPhone());
        em.setSex(employee.getSex());
        LambdaUpdateWrapper<Employee> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Employee::getId, em.getId());
        employeeMapper.update(em, wrapper);
        oldEmployee.setEmployee(em);
        redisUtil.remove("Employee_login:" + em.getId());
        redisUtil.set("Employee_login:" + em.getId(), oldEmployee, Constants.CACHE_1DAY);
        return true;
    }

    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = ((LoginEmployee) authentication.getPrincipal()).getEmployee().getId();
        wrapper.eq(Employee::getId, id)
                .select(Employee::getPassword);
        String oldPsw = employeeMapper.selectOne(wrapper).getPassword();
        if (!oldPsw.equals(oldPassword))
            throw new BaseException("密码错误！");
        String pswReg = "^(?!\\d+$)(?![a-zA-Z]+$)(?![@$!%*#?&.()]+$)[\\dA-Za-z@$!%*#?&￥{}|^~: ;\\-=+/\\\\.()]{6,20}$";
        if (!Pattern.matches(pswReg, newPassword))
            throw new BaseException("密码格式错误");
        LambdaUpdateWrapper<Employee> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Employee::getId, id)
                .set(Employee::getPassword, newPassword);
        redisUtil.remove("Employee_login:" + id);
        return true;
    }

}
