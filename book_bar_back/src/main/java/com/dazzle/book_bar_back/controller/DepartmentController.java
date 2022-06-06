package com.dazzle.book_bar_back.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dazzle.book_bar_back.dao.entity.Department;
import com.dazzle.book_bar_back.dao.entity.Position;
import com.dazzle.book_bar_back.response.ResponseResult;
import com.dazzle.book_bar_back.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName:DepartmentController
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/27 下午 5:41
 * Version V1.0
 */
@RestController
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/department/all_basic")
    @PreAuthorize("hasAuthority('department::list')")
    @ResponseResult
    public List<Department> getAllBasic() {
        return departmentService.selectAllBasic();
    }

    @GetMapping("/department/delete_{id}")
    @PreAuthorize("hasAuthority('department::delete')")
    @ResponseResult
    public int deleteDepartmentById(@PathVariable("id") Long id) {
        return departmentService.deleteById(id);
    }

    @GetMapping("/department/get_basic_{id}")
    @PreAuthorize("hasAuthority('department::list')")
    @ResponseResult
    public Department getOneBasicById(@PathVariable("id") Long id) {
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Department::getId, Department::getName);
        return departmentService.getOne(wrapper);
    }
    @GetMapping("/department/get_info_{id}")
    @PreAuthorize("hasAuthority('department::list')")
    @ResponseResult
    public Department getOneById(@PathVariable("id") Long id) {
        return departmentService.getById(id);
    }

    @PostMapping("/department/update_{id}")
    @PreAuthorize("hasAnyAuthority('department::update', 'department::add')")
    @ResponseResult
    public boolean updateById(@PathVariable("id") Long id, @RequestBody Department department) {
        LambdaUpdateWrapper<Department> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Department::getDescription, department.getDescription())
                .set(Department::getName, department.getName())
                .eq(Department::getId, id);
        return departmentService.update(wrapper);
    }

    @PostMapping("/department/save_or_update")
    @PreAuthorize("hasAuthority('department::add')")
    @ResponseResult
    public boolean updateOrSave(@RequestBody Department department) {
        return departmentService.saveOrUpdate(department);
    }
}
