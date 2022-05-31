package com.dazzle.book_bar_back.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dazzle.book_bar_back.dao.entity.Department;

import java.util.List;

public interface DepartmentService extends IService<Department> {
    public List<Department> selectAllBasic();
    public int deleteById(Long id);
}
