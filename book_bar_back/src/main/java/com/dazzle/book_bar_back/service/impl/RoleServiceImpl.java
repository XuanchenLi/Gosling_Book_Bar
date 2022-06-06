package com.dazzle.book_bar_back.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dazzle.book_bar_back.dao.entity.Menu;
import com.dazzle.book_bar_back.dao.entity.Role;
import com.dazzle.book_bar_back.dao.mapper.MenuMapper;
import com.dazzle.book_bar_back.dao.mapper.RoleMapper;
import com.dazzle.book_bar_back.exception.BaseException;
import com.dazzle.book_bar_back.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName:RoleServiceImpl
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/31 下午 4:18
 * Version V1.0
 */
@Service
public class RoleServiceImpl
        extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Role> listEmployeeRoleById(Long id) {
        return roleMapper.selectEmployeeRole(id);
    }

    @Override
    public Integer addEmployeeRole(Long eId, Long rId) {
        return roleMapper.insertEmployeeRole(eId, rId);
    }

    @Override
    public Integer removeEmployeeRole(Long eId, Long rId) {
        return roleMapper.deleteEmployeeRole(eId, rId);
    }

    @Override
    public List<Menu> getRoleMenu(Long id) {
        return roleMapper.selectRoleMenuById(id);

    }

    @Override
    public List<Menu> getRestRoleMenu(Long id) {
        return roleMapper.selectRestRoleMenu(id);
    }

    @Override
    public boolean addRoleMenu(Long rId, Long mId) {
        return roleMapper.insertRoleMenu(rId, mId);
    }

    @Override
    public boolean removeRoleMenu(Long rId, Long mId) {
        Menu menu = menuMapper.selectById(mId);
        if (rId == 1 && menu.getPerm().equals("authority::control"))
            throw new BaseException("不可移除一号超级管理员的权限控制权");
        return roleMapper.removeRoleMenu(rId, mId);
    }
}
