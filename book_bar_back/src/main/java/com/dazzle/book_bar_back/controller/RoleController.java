package com.dazzle.book_bar_back.controller;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazzle.book_bar_back.dao.entity.Menu;
import com.dazzle.book_bar_back.dao.entity.Role;
import com.dazzle.book_bar_back.exception.BaseException;
import com.dazzle.book_bar_back.response.ResponseResult;
import com.dazzle.book_bar_back.service.RoleService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName:RoleController
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/31 下午 4:25
 * Version V1.0
 */
@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/role/get_all")
    @PreAuthorize("hasAuthority('role::list')")
    @ResponseResult
    public List<Role> getAllRole() {
        return roleService.list();
    }

    @GetMapping("/employee/role/{id}")
    @PreAuthorize("hasAuthority('employee::role::list')")
    @ResponseResult
    public List<Role> getEmployeeRoleById(@PathVariable("id") Long id) {
        return roleService.listEmployeeRoleById(id);
    }
    @GetMapping("/employee/role/add/{eId}_{rId}")
    @PreAuthorize("hasAuthority('employee::role::add')")
    @ResponseResult
    public Integer addEmployeeRole(@PathVariable("eId") Long eId, @PathVariable("rId") Long rId) {
        return roleService.addEmployeeRole(eId, rId);
    }

    @GetMapping("/employee/role/delete/{eId}_{rId}")
    @PreAuthorize("hasAuthority('employee::role::delete')")
    @ResponseResult
    public Integer removeEmployeeRole(@PathVariable("eId") Long eId, @PathVariable("rId") Long rId) {
        return roleService.removeEmployeeRole(eId, rId);
    }

    @PostMapping("/role/save_or_update")
    @PreAuthorize("hasAnyAuthority('role::add', 'role::update')")
    @ResponseResult
    public boolean updateOrSave(@RequestBody Role role) {
        return roleService.saveOrUpdate(role);
    }

    @GetMapping("/role/delete/{id}")
    @PreAuthorize("hasAuthority('role::delete')")
    @ResponseResult
    public boolean deleteRole(@PathVariable("id") Long id) {
        if (id == 1)
            throw new BaseException("不可删除一号超级管理员");
        return roleService.removeById(id);
    }

    @GetMapping("/role/get_menu/{id}")
    @PreAuthorize("hasAuthority('authority::control')")
    @ResponseResult
    public List<Menu> getRoleMenu(@PathVariable("id") Long id) {
        return roleService.getRoleMenu(id);
    }

    @GetMapping("/role/get_rest_menu/{id}")
    @ResponseResult
    @PreAuthorize("hasAuthority('authority::control')")
    public List<Menu> getRestRoleMenu(@PathVariable("id") Long id) {
        return roleService.getRestRoleMenu(id);
    }

    @GetMapping("/role/add_menu/{rId}_{mId}")
    @ResponseResult
    @PreAuthorize("hasAuthority('authority::control')")
    public boolean addRoleMenu(@PathVariable("rId") Long rId, @PathVariable("mId") Long mId) {
        return roleService.addRoleMenu(rId, mId);
    }

    @GetMapping("/role/delete_menu/{rId}_{mId}")
    @ResponseResult
    @PreAuthorize("hasAuthority('authority::control')")
    public boolean removeRoleMenu(@PathVariable("rId") Long rId, @PathVariable("mId") Long mId) {
        return roleService.removeRoleMenu(rId, mId);
    }
}
