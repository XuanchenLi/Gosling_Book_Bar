package com.dazzle.book_bar_back.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazzle.book_bar_back.controller.response.AllocateHistoryResponse;
import com.dazzle.book_bar_back.dao.entity.AllocateRecord;
import com.dazzle.book_bar_back.dao.entity.DepartmentPositionPair;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllocateRecordMapper extends BaseMapper<AllocateRecord> {
    List<AllocateHistoryResponse> getHistoryById(Long id);
}
