package com.dazzle.book_bar_back.service;

import com.dazzle.book_bar_back.controller.response.StringDoubleResponse;
import com.dazzle.book_bar_back.dao.entity.BookStats;

import java.util.List;

public interface BookStatsService {
    List<BookStats> getSuggestion(String query);

    List<BookStats> getInventoryHistoryById(Long id);

    List<BookStats> getSaleHistoryById(Long id);

    List<StringDoubleResponse> getSaleAmount();

    List<StringDoubleResponse> getTypeSaleAmount();
}
