package com.dazzle.book_bar_back.dao.mapper;
import com.dazzle.book_bar_back.dao.entity.DepartmentPositionPair;
import com.dazzle.book_bar_back.dao.entity.Position;
import org.apache.ibatis.annotations.Param;


import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazzle.book_bar_back.dao.entity.Employee;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface EmployeeMapper extends BaseMapper<Employee> {

    List<DepartmentPositionPair> selectPositionListById(Long id);

    boolean deletePosition(@Param("eId") Long eId,@Param("pId") Long positionId);

    int addPosition(Long eId, Long pId);
}
