package com.dazzle.book_bar_back.dao.mapper;
import org.apache.ibatis.annotations.Param;


import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazzle.book_bar_back.dao.entity.Employee;
import org.springframework.stereotype.Repository;


@Repository
public interface EmployeeMapper extends BaseMapper<Employee> {

}
