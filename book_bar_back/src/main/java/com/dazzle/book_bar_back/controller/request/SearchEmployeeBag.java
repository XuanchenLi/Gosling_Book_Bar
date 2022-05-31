package com.dazzle.book_bar_back.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName:SearchBag
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/29 下午 3:47
 * Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchEmployeeBag implements Serializable {
    private Integer regionSelect;
    private Integer typeSelect;
    private Date startDate;
    private Date endDate;
    private String query;
    private Integer currentPage;
}
