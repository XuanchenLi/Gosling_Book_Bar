package com.dazzle.book_bar_back.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazzle.book_bar_back.dao.entity.Menu;
import com.dazzle.book_bar_back.dao.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper extends BaseMapper<Role> {
    List<Role> selectEmployeeRole(Long id);

    Integer insertEmployeeRole(Long eId, Long rId);

    Integer deleteEmployeeRole(Long eId, Long rId);

    List<Menu> selectRoleMenuById(Long id);

    List<Menu> selectRestRoleMenu(Long id);

    boolean insertRoleMenu(Long rId, Long mId);

    boolean removeRoleMenu(Long rId, Long mId);
}
