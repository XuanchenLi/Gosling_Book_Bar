package com.dazzle.book_bar_back.dao.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName:AlocateRecord
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/30 下午 5:33
 * Version V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_allocate_record")
public class AllocateRecord {
    @TableId
    private Long id;
    private Long employeeId;
    private Long positionId;
    private Integer type;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date allocateDate;
}
