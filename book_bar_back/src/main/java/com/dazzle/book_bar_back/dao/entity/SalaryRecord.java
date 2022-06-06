package com.dazzle.book_bar_back.dao.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.DateTimeException;
import java.util.Date;

/**
 * @ClassName:SalaryRecord
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/6/1 下午 8:39
 * Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_salary_record")
public class SalaryRecord implements Serializable {
    @TableId
    private Long id;
    private Long employeeId;
    private double basePay;
    private double insurancePay;
    private double meritPay;
    private double subsidyPay;
    private double overtimePay;
    private double sumPay;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    public SalaryRecord(Salary salary, Date date) {
        setId(null);
        setEmployeeId(salary.getEmployeeId());
        setBasePay(salary.getBasePay());
        setInsurancePay(salary.getInsurancePay());
        setMeritPay(salary.getMeritPay());
        setSubsidyPay(salary.getSubsidyPay());
        setOvertimePay(salary.getOvertimePay());
        setSumPay(
                salary.getBasePay() + salary.getInsurancePay()
                + salary.getMeritPay() + salary.getSubsidyPay()
                + salary.getOvertimePay()
        );
        setPayTime(date);
    }
}
