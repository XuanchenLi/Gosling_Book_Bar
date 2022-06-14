package com.dazzle.book_bar_back.controller;

import com.dazzle.book_bar_back.controller.response.StringDoubleResponse;
import com.dazzle.book_bar_back.dao.entity.BookStats;
import com.dazzle.book_bar_back.response.ResponseResult;
import com.dazzle.book_bar_back.service.BookStatsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName:BookStatsController
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/6/13 下午 9:22
 * Version V1.0
 */
@RestController
@Slf4j
public class BookStatsController {
    @Autowired
    private BookStatsService bookStatsService;

    @GetMapping("book/stats/suggest/{query}")
    @ResponseResult
    @PreAuthorize("hasAuthority('book::stats')")
    public List<BookStats> getSuggest(@PathVariable("query") String query) {
        return bookStatsService.getSuggestion(query);
    }
    @GetMapping("book/stats/{id}/inventory")
    @ResponseResult
    @PreAuthorize("hasAuthority('book::stats')")
    public List<BookStats> getInventoryHistory(@PathVariable("id") Long id) {
        return bookStatsService.getInventoryHistoryById(id);
    }

    @GetMapping("book/stats/{id}/sale")
    @ResponseResult
    @PreAuthorize("hasAuthority('book::stats')")
    public List<BookStats> getSaleHistory(@PathVariable("id") Long id) {
        return bookStatsService.getSaleHistoryById(id);
    }

    @GetMapping("book/stats/all_sale")
    @ResponseResult
    @PreAuthorize("hasAuthority('book::stats')")
    public List<StringDoubleResponse> getSaleAmount() {
        return bookStatsService.getSaleAmount();
    }
    @GetMapping("book/stats/type_sale")
    @ResponseResult
    @PreAuthorize("hasAuthority('book::stats')")
    public List<StringDoubleResponse> getTypeSaleAmount() {
        return bookStatsService.getTypeSaleAmount();
    }
}
