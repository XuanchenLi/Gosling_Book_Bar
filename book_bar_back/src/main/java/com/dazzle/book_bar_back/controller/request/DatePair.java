package com.dazzle.book_bar_back.controller.request;

import com.alibaba.druid.filter.AutoLoad;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName:
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/6/2 上午 9:33
 * Version V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatePair {
    private Date startDate;
    private Date endDate;
}
