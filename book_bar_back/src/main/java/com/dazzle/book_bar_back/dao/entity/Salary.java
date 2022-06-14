package com.dazzle.book_bar_back.dao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName:Salary
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/6/1 下午 8:36
 * Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_salary")
public class Salary implements Serializable {
    @TableId
    private Long id;
    private Long employeeId;
    private double basePay;
    private double insurancePay;
    private double meritPay;
    private double subsidyPay;
    private double overtimePay;
    @TableLogic
    private int delFlag;
}
