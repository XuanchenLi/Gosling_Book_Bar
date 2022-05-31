package com.dazzle.book_bar_back.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazzle.book_bar_back.dao.entity.DepartmentPositionPair;
import com.dazzle.book_bar_back.dao.entity.Position;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionMapper extends BaseMapper<Position> {

    // DepartmentPositionPair selectPairById(Long id);
    List<Long> selectEmployeeIdById(Long id);
}
