package com.dazzle.book_bar_back.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName:Position
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/27 下午 5:56
 * Version V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_position")
public class Position {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long departmentId;
    private String name;
    private String description;
    @TableLogic
    private Integer delFlag;
}
