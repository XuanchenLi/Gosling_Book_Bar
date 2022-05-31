package com.dazzle.book_bar_back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dazzle.book_bar_back.dao.entity.DepartmentPositionPair;
import com.dazzle.book_bar_back.dao.entity.Position;
import com.dazzle.book_bar_back.dao.mapper.PositionMapper;
import com.dazzle.book_bar_back.service.EmployeeService;
import com.dazzle.book_bar_back.service.PositionService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName:PositionServiceImpl
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/27 下午 6:04
 * Version V1.0
 */
@Service
public class PositionServiceImpl
        extends ServiceImpl<PositionMapper, Position>
        implements PositionService {
    @Autowired
    private PositionMapper positionMapper;
    @Autowired
    private EmployeeService employeeService;

    @Override
    public List<Position> selectBasicByDepartmentId(Long departmentId) {
        LambdaQueryWrapper<Position> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Position::getId, Position::getName).eq(Position::getDepartmentId, departmentId);
        return positionMapper.selectList(wrapper);
    }

    @Override
    public Integer deleteById(Long id) {
        if (positionMapper.deleteById(id) == 1) {
            List<Long> ids = positionMapper.selectEmployeeIdById(id);
            for (Long item : ids) {
                employeeService.deletePosition(item, id);
            }
            return 1;
        }
        return 0;
    }

    @Override
    public Integer deleteBatchByDepartmentId(Long departmentId) {
        LambdaQueryWrapper<Position> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Position::getId)
                .eq(Position::getDepartmentId, departmentId);
        List<Long> ids =  positionMapper.selectList(wrapper)
                .stream()
                .map(Position::getId)
                .collect(Collectors.toList());
        for (var item : ids) {
            deleteById(item);
        }
        return ids.size();
    }

}
