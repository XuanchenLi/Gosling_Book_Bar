package com.dazzle.book_bar_back.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName:DepartmentPositionPair
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/30 下午 2:27
 * Version V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentPositionPair {

    private Long departmentId;

    private Long positionId;

    private String departmentName;

    private String positionName;
}
