package com.dazzle.book_bar_back.dao.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


/**
 * @ClassName:Employee
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/24 下午 11:08
 * Version V1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_employee")
public class Employee implements Serializable {
    @TableId  //指定主键字段，主键名若为id则可省略
    private Long id;
    private String username;
    private String password;
    @TableLogic  //指定逻辑删除字段
    private Integer delFlag;
    private String email;
    private String phone;
    private String avatar;
    private Integer type;
    private Integer status;
    private Integer sex;
    @JSONField(format="yyyy-MM-dd")
    private Date startDate;
    @JSONField(format="yyyy-MM-dd")
    private Date endDate;
}
