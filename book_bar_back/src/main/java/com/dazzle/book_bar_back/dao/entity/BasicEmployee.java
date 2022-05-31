package com.dazzle.book_bar_back.dao.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName:BasicEmployee
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/29 下午 4:51
 * Version V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicEmployee {
    private Long id;
    private String username;
    private Integer sex;
    @JSONField(format="yyyy-MM-dd")
    private Date startDate;
    @JSONField(format="yyyy-MM-dd")
    private Date endDate;

    public BasicEmployee (Employee employee) {
        id = employee.getId();
        username = employee.getUsername();
        sex = employee.getSex();
        startDate = employee.getStartDate();
        endDate = employee.getEndDate();
    }
}
