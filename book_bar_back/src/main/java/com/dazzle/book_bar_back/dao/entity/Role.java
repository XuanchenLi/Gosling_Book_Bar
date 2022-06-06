package com.dazzle.book_bar_back.dao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName:role
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/31 下午 4:14
 * Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role")
public class Role implements Serializable {
    @TableId
    private Long id;
    private String roleKey;
    private String name;
    @TableLogic
    private Integer delFlag;
}
