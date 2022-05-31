package com.dazzle.book_bar_back.controller.response;

import com.dazzle.book_bar_back.dao.entity.BasicEmployee;
import com.dazzle.book_bar_back.dao.entity.Employee;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName:searchEmployeeResponse
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/29 下午 4:19
 * Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchEmployeeResponse implements Serializable {
    private List<BasicEmployee> employeeList;
    private Long currentPage;
    private Long totalNum;
    private Long totalPage;
    private boolean hasPrevious;
    private boolean hasNext;
}
