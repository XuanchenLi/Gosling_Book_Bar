package com.dazzle.book_bar_back.dao.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName:SimpleBook
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/6/13 下午 9:16
 * Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookStats {
    Long id;
    String bookLabel;
    String bookName;
    String category;
    Long inventory;
    String inventoryTime;
    Long inNum;
    Long sale;
    String saleTime;
    Long saleNum;
}
