package com.dazzle.book_bar_back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dazzle.book_bar_back.dao.entity.Department;
import com.dazzle.book_bar_back.dao.mapper.DepartmentMapper;
import com.dazzle.book_bar_back.service.DepartmentService;
import com.dazzle.book_bar_back.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName:
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/27 下午 5:37
 * Version V1.0
 */
@Service
public class DepartmentServiceImpl
        extends ServiceImpl<DepartmentMapper, Department>
        implements DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private PositionService positionService;
    public List<Department> selectAllBasic() {
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Department::getId, Department::getName);
        return departmentMapper.selectList(wrapper);
    }

    @Override
    public int deleteById(Long id) {
        int res = 0;
        if ((res = departmentMapper.deleteById(id)) > 0) {
            positionService.deleteBatchByDepartmentId(id);
        }
        return res;
    }

}
