package com.dazzle.book_bar_back.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dazzle.book_bar_back.dao.entity.Menu;
import com.dazzle.book_bar_back.dao.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role> {
    List<Role> listEmployeeRoleById(Long id);

    Integer addEmployeeRole(Long eId, Long rId);

    Integer removeEmployeeRole(Long eId, Long rId);

    List<Menu> getRoleMenu(Long id);

    List<Menu> getRestRoleMenu(Long id);

    boolean addRoleMenu(Long rId, Long mId);

    boolean removeRoleMenu(Long rId, Long mId);
}
