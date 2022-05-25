package com.dazzle.book_bar_back.dao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName:Menu
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/25 下午 10:16
 * Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_menu")
public class Menu implements Serializable {
    @TableId
    private Long id;
    private String menuName;
    private String path;
    @TableLogic
    private Integer delFlag;
    private String perm;
}
