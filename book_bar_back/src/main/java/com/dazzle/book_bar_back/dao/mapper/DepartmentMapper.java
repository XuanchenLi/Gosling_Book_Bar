package com.dazzle.book_bar_back.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazzle.book_bar_back.controller.response.DepartmentStatsResponse;
import com.dazzle.book_bar_back.dao.entity.Department;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentMapper extends BaseMapper<Department> {
    List<DepartmentStatsResponse> selectDepartmentStats();
}
