package com.dazzle.book_bar_back.controller.response;

import com.alibaba.fastjson.annotation.JSONField;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName:AllocateHistoryResponse
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/30 下午 11:08
 * Version V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllocateHistoryResponse {
    private String positionName;
    private String departmentName;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date allocateDate;
    private Integer type;
}
