package com.dazzle.book_bar_back.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dazzle.book_bar_back.dao.entity.DepartmentPositionPair;
import com.dazzle.book_bar_back.dao.entity.Position;

import java.util.List;

public interface PositionService extends IService<Position> {
    List<Position> selectBasicByDepartmentId(Long departmentId);
    Integer deleteById(Long id);
    Integer deleteBatchByDepartmentId(Long departmentId);


}
