package com.dazzle.book_bar_back.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazzle.book_bar_back.dao.entity.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuMapper extends BaseMapper<Menu> {
    List<String> selectPermByUserId(Long userId);
}
