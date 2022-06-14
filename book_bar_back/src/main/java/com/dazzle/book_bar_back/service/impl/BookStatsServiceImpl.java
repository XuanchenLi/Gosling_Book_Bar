package com.dazzle.book_bar_back.service.impl;

import com.dazzle.book_bar_back.controller.response.StringDoubleResponse;
import com.dazzle.book_bar_back.dao.entity.BookStats;
import com.dazzle.book_bar_back.dao.mapper.BookStatsMapper;
import com.dazzle.book_bar_back.exception.BaseException;
import com.dazzle.book_bar_back.service.BookStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName:BookStatsServiceImpl
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/6/13 下午 9:23
 * Version V1.0
 */
@Service
public class BookStatsServiceImpl implements BookStatsService {
    @Autowired
    private BookStatsMapper bookStatsMapper;

    @Override
    public List<BookStats> getSuggestion(String query) {
        if (Objects.isNull(query) || "".equals(query))
            throw new BaseException("查询不可为空");
        query = "%" + query + "%";
        return bookStatsMapper.selectSuggestion(query);
    }

    @Override
    public List<BookStats> getInventoryHistoryById(Long id) {
        return bookStatsMapper.selectInventoryHistoryById(id);
    }

    @Override
    public List<BookStats> getSaleHistoryById(Long id) {
        return bookStatsMapper.selectSaleHistoryById(id);
    }

    @Override
    public List<StringDoubleResponse> getSaleAmount() {
        return bookStatsMapper.selectSaleAmount();
    }

    @Override
    public List<StringDoubleResponse> getTypeSaleAmount() {
        return bookStatsMapper.selectTypeSaleAmount();
    }
}
