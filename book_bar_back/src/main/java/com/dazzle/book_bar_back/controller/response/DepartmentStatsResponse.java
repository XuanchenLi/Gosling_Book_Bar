package com.dazzle.book_bar_back.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName:DepartmentStatsResponse
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/6/13 上午 11:50
 * Version V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentStatsResponse {
    String departmentName;
    String positionName;
    Long number;
    Long totalSalary;
}
