package com.dazzle.book_bar_back.dao.mapper;

import com.dazzle.book_bar_back.controller.response.StringDoubleResponse;
import com.dazzle.book_bar_back.dao.entity.BookStats;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookStatsMapper {
    List<BookStats> selectSuggestion(String query);

    List<BookStats> selectInventoryHistoryById(Long id);

    List<BookStats> selectSaleHistoryById(Long id);

    List<StringDoubleResponse> selectSaleAmount();

    List<StringDoubleResponse> selectTypeSaleAmount();
}
